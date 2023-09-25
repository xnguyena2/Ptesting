package com.example.heroku.services;

import com.example.heroku.firebase.MyFireBase;
import com.example.heroku.model.ProductOrder;
import com.example.heroku.model.ProductUnit;
import com.example.heroku.model.ProductUnitOrder;
import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.repository.*;
import com.example.heroku.request.order.OrderSearchResult;
import com.example.heroku.request.beer.PackageOrderData;
import com.example.heroku.util.Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class BeerOrder {

    @Autowired
    SeverEventAdapterImpl severEventAdapter;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    PackageOrderRepository packageOrderRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    BeerUnitOrderRepository beerUnitOrderRepository;

    @Autowired
    BeerUnitRepository beerUnitRepository;

    @Autowired
    ShippingProvider shippingProviderAPI;

    @Autowired
    Voucher voucherServices;

    @Autowired
    UserPackageRepository userPackageRepository;

    @Autowired
    MyFireBase myFireBase;

    private Flux<ProductWithVoucher> getUserVoucherAndProductName(PackageOrderData packageOrderData) {
        return Flux.just(packageOrderData.getBeerOrders())
                .flatMap(beerOrderData ->
                        beerRepository.findBySecondIDCanOrder(packageOrderData.getPackageOrder().getGroup_id(), beerOrderData.getProductOrder().getProduct_second_id())
                                .map(beerOrderData::UpdateName)
                ).map(beerOrderData -> ProductWithVoucher.builder().productOrder(beerOrderData).build())
                .flatMap(packaBeerOrderData ->
                        voucherServices.getDeviceVoucher(packageOrderData.getPackageOrder().getGroup_id(),
                                        packaBeerOrderData.getProductOrder().getProductOrder().getVoucher_second_id(),
                                        packageOrderData.getPackageOrder().getUser_device_id(),
                                        packaBeerOrderData.getProductOrder().getProductOrder().getProduct_second_id()
                                )
                                .switchIfEmpty(Mono.just(com.example.heroku.model.Voucher.builder().group_id(packageOrderData.getPackageOrder().getGroup_id()).build()))
                                .map(voucher -> {
                                    packaBeerOrderData.setVoucher(voucher);
                                    return packaBeerOrderData;
                                })
                )
                .concatWith(Mono.just(packageOrderData.getPackageOrder())
                        .filter(packageOrder -> packageOrder.getVoucher_second_id() != null)
                        .flatMap(
                                packageOrder ->
                                        voucherServices.getPackageVoucher(packageOrder.getGroup_id(), packageOrder.getVoucher_second_id(), packageOrder.getUser_device_id())
                                                .map(voucher ->
                                                        ProductWithVoucher.builder().voucher(voucher).build()
                                                )
                        )
                );
    }

    private Mono<VoucherMerge> caculatorVoucherOfProduct(Timestamp currentTime, com.example.heroku.model.Voucher vouchers, String device, ProductOrder productOrder) {
        return Mono.just(vouchers)
                .map(voucher -> {
                    AtomicReference<Float> discount = new AtomicReference<>((float) 0);
                    AtomicReference<Float> amount = new AtomicReference<>((float) 0);
                    if (Util.getInstance().DiffirentDays(voucher.getDate_expire(), currentTime) >= 0) {
                        voucher.comsumeVoucherSync(vouchers.getGroup_id() + device, (shareReuse) -> {
                            if (voucher.getDiscount() > 0) {
                                discount.updateAndGet(v -> (v + voucher.getDiscount()));
                                System.out.println("--------> comsume only BeerOrder: " + productOrder.getProduct_second_id() + ", voucher: " + voucher.getVoucher_second_id() + ", discount: " + voucher.getDiscount() + "%");
                            }
                            if (voucher.getAmount() > 0) {
                                amount.updateAndGet(v -> (v + voucher.getAmount()));
                                System.out.println("--------> comsume only BeerOrder: " + productOrder.getProduct_second_id() + ", voucher: " + voucher.getVoucher_second_id() + ", amount: " + voucher.getAmount());
                            }
                        });
                    }
                    return VoucherMerge.builder().amount(amount.get()).discount(discount.get()).build();
                });
    }

    private Mono<VoucherMerge> calculatorVoucherForAllBeer(Flux<ProductWithVoucher> vouchers, Timestamp currentTime, String device) {
        return vouchers
                .filter(productWithVoucher -> productWithVoucher.getProductOrder() == null)
                .map(ProductWithVoucher::getVoucher)
                .map(voucher ->
                {
                    VoucherMerge voucherMerge = VoucherMerge.builder().amount(0).discount(0).build();
                    if (Util.getInstance().DiffirentDays(voucher.getDate_expire(), currentTime) >= 0) {
                        voucher.comsumeVoucherSync(voucher.getGroup_id() + device, (shareReuse) -> {
                            if (voucher.getAmount() > 0) {
                                voucherMerge.setAmount(voucher.getAmount());
                                System.out.println("--------> allbeer voucher: " + voucher.getVoucher_second_id() + ", amount: " + voucher.getAmount());
                            }
                            if (voucher.getDiscount() > 0) {
                                voucherMerge.setDiscount(voucher.getDiscount());
                                System.out.println("--------> allbeer voucher: " + voucher.getVoucher_second_id() + ", discount: " + voucher.getDiscount() + "%");
                            }
                        });
                    }
                    return voucherMerge;
                })
                .reduce(VoucherMerge.builder().amount(0).discount(0).build(),
                        (x1, x2) ->
                                VoucherMerge.builder()
                                        .amount(x1.getAmount() + x2.getAmount())
                                        .discount(x1.getDiscount() + x2.getDiscount())
                                        .build()
                );
    }

    private Mono<WeightAndVolumetricContainer> calculatorProductPrice(ProductUnitOrder productUnitOrder, ProductUnit productUnit, PackageOrderData packageOrderData) {
        float price = productUnitOrder.getNumber_unit() * productUnit.getPrice();
        if (price < 0) {
            price = 0;
        }
        System.out.println("calculate beer unit: " + productUnit.getProduct_unit_second_id() + ", price: " + price);
        productUnitOrder.setPrice(price);
        WeightAndVolumetricContainer result = WeightAndVolumetricContainer
                .builder()
                .price(price)
                .weight(productUnitOrder.getNumber_unit() * productUnit.getWeight())
                .volumetric(productUnitOrder.getNumber_unit() * productUnit.getVolumetric())
                .build();
        //save beer order unit;
        if (!packageOrderData.isPreOrder()) {
            return beerUnitOrderRepository.save(productUnitOrder.AutoFill(packageOrderData.getPackageOrder().getPackage_order_second_id()))
                    .then(Mono.just(result));
        }
        return Mono.just(result);
    }

    private Mono<Object> cleanPackage(PackageOrderData packageOrderData, PackageOrderData.BeerOrderData beerOrderData) {
        if (packageOrderData.isPreOrder()) {
            return Mono.empty();
        }
        return Flux.just(beerOrderData.getProductUnitOrders())
                .flatMap(beerUnitOrder -> userPackageRepository.DeleteProductByBeerUnit(packageOrderData.getPackageOrder().getGroup_id(),
                        packageOrderData.getPackageOrder().getUser_device_id(), beerUnitOrder.getProduct_unit_second_id())
                ).then(Mono.empty());
    }

    public Mono<PackageOrder> createOrder(PackageOrderData packageOrderData) {
        System.out.println("New request from: " + packageOrderData.getPackageOrder().getUser_device_id());
        String groupID = packageOrderData.getPackageOrder().getGroup_id();
        packageOrderData.getPackageOrder().AutoFill(packageOrderData.isPreOrder());
        Timestamp currentTime = Util.getInstance().Now();
        Mono<ShippingProvider.GHN> shippingProvider = shippingProviderAPI.GetGHNShippingProvider(groupID);
        return Mono.just(getUserVoucherAndProductName(packageOrderData))
                .flatMap(vouchers ->
                        vouchers.map(vc ->
                                        Util.getInstance().obserVoucher(vc.getVoucher().getGroup_id() + packageOrderData.getPackageOrder().getUser_device_id() + vc.getVoucher().getVoucher_second_id())
                                )
                                .then(
                                        vouchers
                                                .filter(productWithVoucher -> productWithVoucher.getProductOrder() != null)
                                                .flatMap(productWithVoucher ->
                                                        Flux.just(productWithVoucher.getProductOrder().getProductUnitOrders())
                                                                //calculate unit price
                                                                .flatMap(beerUnitOrder ->
                                                                        beerUnitRepository.findByBeerUnitIDCanOrder(groupID, beerUnitOrder.getProduct_unit_second_id())
                                                                                .map(ProductUnit::CheckDiscount)
                                                                                .map(beerUnitOrder::UpdateName)
                                                                                .map(ProductUnit::UpdateToRealPrice)
                                                                                .flatMap(beerUnit ->
                                                                                        calculatorProductPrice(beerUnitOrder, beerUnit, packageOrderData)
                                                                                )
                                                                )
                                                                //sum all unit price
                                                                .reduce(WeightAndVolumetricContainer.builder().volumetric(0.0f).weight(0.0f).price(0.0f).build(),
                                                                        (x1, x2) ->
                                                                                WeightAndVolumetricContainer
                                                                                        .builder()
                                                                                        .volumetric(x1.getVolumetric() + x2.getVolumetric())
                                                                                        .weight(x1.getWeight() + x2.getWeight())
                                                                                        .price(x1.getPrice() + x2.getPrice())
                                                                                        .build()
                                                                )
                                                                // apply voucher
                                                                .flatMap(weightAndVolumetricContainer ->
                                                                        caculatorVoucherOfProduct(currentTime, productWithVoucher.getVoucher(), packageOrderData.getPackageOrder().getUser_device_id(), productWithVoucher.getProductOrder().getProductOrder())
                                                                                .map(voucherMerge -> {
                                                                                    float price = weightAndVolumetricContainer.getPrice();
                                                                                    price *= (100 - voucherMerge.getDiscount()) / 100;
                                                                                    price -= voucherMerge.getAmount();
                                                                                    weightAndVolumetricContainer.setPrice(price);
                                                                                    return weightAndVolumetricContainer;
                                                                                })
                                                                )
                                                                .flatMap(weightAndVolumetricContainer -> {
                                                                            float price = weightAndVolumetricContainer.getPrice();
                                                                            System.out.println("BeerOrder: " + productWithVoucher.getProductOrder().getProductOrder().getProduct_second_id() + ", Total price: " + price);
                                                                            productWithVoucher.getProductOrder().getProductOrder().setTotal_price(price);
                                                                            //save beer order
                                                                            if (!packageOrderData.isPreOrder()) {
                                                                                return beerOrderRepository.save(productWithVoucher.getProductOrder().getProductOrder().AutoFill(packageOrderData.getPackageOrder().getPackage_order_second_id()))
                                                                                        .then(Mono.just(weightAndVolumetricContainer));
                                                                            }
                                                                            return Mono.just(weightAndVolumetricContainer);
                                                                        }
                                                                )
                                                )
                                                .reduce(WeightAndVolumetricContainer.builder().volumetric(0.0f).weight(0.0f).price(0.0f).build(),
                                                        (x1, x2) ->
                                                                WeightAndVolumetricContainer
                                                                        .builder()
                                                                        .volumetric(x1.getVolumetric() + x2.getVolumetric())
                                                                        .weight(x1.getWeight() + x2.getWeight())
                                                                        .price(x1.getPrice() + x2.getPrice())
                                                                        .build()
                                                )
                                )
                                // apply voucher for whole package
                                .flatMap(weightAndVolumetricContainer ->
                                        calculatorVoucherForAllBeer(vouchers, currentTime, packageOrderData.getPackageOrder().getUser_device_id())
                                                .map(voucherMerge -> {
                                                    float price = weightAndVolumetricContainer.getPrice();
                                                    if (voucherMerge.discount > 0) {
                                                        price *= (100 - voucherMerge.discount) / 100;
                                                    }
                                                    if (voucherMerge.amount > 0) {
                                                        price -= voucherMerge.amount;
                                                    }
                                                    if (price < 0) {
                                                        price = 0;
                                                    }
                                                    weightAndVolumetricContainer.setRealPrice(price);
                                                    return weightAndVolumetricContainer;
                                                })
                                )
                                .flatMap(weightAndVolumetricContainerMono ->
                                        shippingProvider.map(ghn -> {
                                                    float real_price = weightAndVolumetricContainerMono.getRealPrice();
                                                    float total_price = weightAndVolumetricContainerMono.getPrice();
                                                    System.out.println("Total Price: " + real_price);

                                                    if (real_price == 0) {
                                                        return Mono.just(packageOrderData.getPackageOrder());
                                                    }

                                                    float ship_price = ghn.CalculateShipPrice(weightAndVolumetricContainerMono.getWeight(), weightAndVolumetricContainerMono.getVolumetric(), packageOrderData.getPackageOrder().getRegion_id(), packageOrderData.getPackageOrder().getDistrict_id());
                                                    System.out.println("Total Ship Price: " + ship_price);

                                                    packageOrderData.getPackageOrder().setReal_price(real_price);
                                                    packageOrderData.getPackageOrder().setTotal_price(total_price);
                                                    packageOrderData.getPackageOrder().setShip_price(ship_price);

                                                    return packageOrderData;
                                                }
                                        )
                                )
                                .thenMany(
                                        vouchers
                                                .filter(productWithVoucher -> Util.getInstance().CleanMap(productWithVoucher.getVoucher().getGroup_id() + packageOrderData.getPackageOrder().getUser_device_id() + productWithVoucher.getVoucher().getVoucher_second_id()) && !packageOrderData.isPreOrder())
                                                .map(productWithVoucher ->
                                                        voucherServices
                                                                .ForceSaveVoucher(productWithVoucher.getVoucher().getGroup_id(), productWithVoucher.getVoucher().getVoucher_second_id(), packageOrderData.getPackageOrder().getUser_device_id(),
                                                                        Util.getInstance().CleanReuse(productWithVoucher.getVoucher().getGroup_id() + packageOrderData.getPackageOrder().getUser_device_id() + productWithVoucher.getVoucher().getVoucher_second_id())
                                                                )
                                                                .subscribe()
                                                )
                                )
                                .thenMany(
                                        vouchers
                                                .filter(productWithVoucher -> productWithVoucher.getProductOrder() != null)
                                                .flatMap(productWithVoucher ->
                                                        cleanPackage(packageOrderData, productWithVoucher.getProductOrder())
                                                )
                                )
                                .then(
                                        Mono.just(packageOrderData.getPackageOrder())
                                                .flatMap(packageOrder -> {
                                                    if (!packageOrderData.isPreOrder()) {
                                                        //send to admin
                                                        myFireBase.SendNotification2Admin(packageOrder.getReciver_fullname(), "Mua: " + packageOrderData.getPackageOrder().getReal_price());
                                                        severEventAdapter.SendEvent(new OrderSearchResult.PackageOrderData(packageOrder));
                                                        return packageOrderRepository.save(packageOrder);
                                                    } else {
                                                        return Mono.just(packageOrder);
                                                    }
                                                })
                                )
                );
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class WeightAndVolumetricContainer {
        private float weight;
        private float volumetric;
        private float realPrice;
        private float price;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class VoucherMerge {
        private float amount;
        private float discount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class ProductWithVoucher {
        private PackageOrderData.BeerOrderData productOrder;
        private com.example.heroku.model.Voucher voucher;
    }
}
