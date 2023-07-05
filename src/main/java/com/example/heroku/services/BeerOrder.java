package com.example.heroku.services;

import com.example.heroku.firebase.MyFireBase;
import com.example.heroku.model.BeerUnit;
import com.example.heroku.model.BeerUnitOrder;
import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.UserPackage;
import com.example.heroku.model.repository.*;
import com.example.heroku.request.Order.OrderSearchResult;
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
import java.util.Date;
import java.util.Objects;
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

    private Flux<com.example.heroku.model.Voucher> getUserVoucherAndEmpty(PackageOrderData packageOrderData) {
        return Flux.just(packageOrderData.getBeerOrders())
                .flatMap(packaBeerOrderData ->
                            voucherServices.getDeviceVoucher(packaBeerOrderData.getBeerOrder().getVoucher_second_id(), packageOrderData.getPackageOrder().getUser_device_id(), packageOrderData.getPackageOrder().getPhone_number_clean(), packaBeerOrderData.getBeerOrder().getBeer_second_id())
                )
                .distinct(
                        com.example.heroku.model.Voucher::getVoucher_second_id
                )
                .switchIfEmpty(Flux.just(com.example.heroku.model.Voucher.builder().build()));
    }

    private Mono<VoucherMerge> caculatorVoucherOfProduct(Timestamp currentTime, Flux<com.example.heroku.model.Voucher> vouchers, String device, com.example.heroku.model.BeerOrder beerOrder) {
        return vouchers.filter(voucher -> voucher.IsOnlyApplyForProduct(beerOrder.getVoucher_second_id()))
                .switchIfEmpty(Mono.just(com.example.heroku.model.Voucher.builder().reuse(0).build()))
                .map(voucher -> {
                    AtomicReference<Float> discount = new AtomicReference<>((float) 0);
                    AtomicReference<Float> amount = new AtomicReference<>((float) 0);
                    if (voucher.getId() != null && Util.getInstance().DiffirentDays(voucher.getDate_expire(), currentTime) >= 0) {
                        System.out.println("Begin comsume voucher for each beer!: " + voucher.getVoucher_second_id());
                        voucher.comsumeVoucherSync(device, (shareReuse) -> {
                            if (voucher.getDiscount() > 0) {
                                discount.updateAndGet(v -> (v + voucher.getDiscount()));
                                System.out.println("--------> comsume only BeerOrder: " + beerOrder.getBeer_second_id() + ", voucher: " + voucher.getVoucher_second_id() + ", discount: " + voucher.getDiscount() + "%");
                            }
                            if (voucher.getAmount() > 0) {
                                amount.updateAndGet(v -> (v + voucher.getAmount()));
                                System.out.println("--------> comsume only BeerOrder: " + beerOrder.getBeer_second_id() + ", voucher: " + voucher.getVoucher_second_id() + ", amount: " + voucher.getAmount());
                            }
                        });
                    }
                    return VoucherMerge.builder().amount(amount.get()).discount(discount.get()).build();
                })
                .reduce(VoucherMerge.builder().build(), (x1, x2) -> VoucherMerge.builder()
                        .discount(x1.getDiscount() + x2.getDiscount())
                        .amount(x1.getAmount() + x2.getAmount())
                        .build());
    }

    private Mono<VoucherMerge> calculatorVoucherForAllBeer(Timestamp currentTime, Flux<com.example.heroku.model.Voucher> vouchers, String device) {
        return vouchers.filter(com.example.heroku.model.Voucher::isFor_all_beer)
                .switchIfEmpty(Mono.just(com.example.heroku.model.Voucher.builder().reuse(0).build()))
                .flatMap(voucher ->
                {
                    VoucherMerge voucherMerge = VoucherMerge.builder().amount(0).discount(0).build();
                    if (voucher.getId() != null && Util.getInstance().DiffirentDays(voucher.getDate_expire(), currentTime) >= 0) {
                        voucher.comsumeVoucherSync(device, (shareReuse) -> {
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
                    return Mono.just(voucherMerge);
                })
                .reduce(VoucherMerge.builder().amount(0).discount(0).build(),
                        (x1, x2) ->
                                VoucherMerge.builder()
                                        .amount(x1.getAmount() + x2.getAmount())
                                        .discount(x1.getDiscount() + x2.getDiscount())
                                        .build()
                );
    }

    private Mono<WeightAndVolumetricContainer> calculatorProductPrice(VoucherMerge voucherMerge, BeerUnitOrder beerUnitOrder, BeerUnit beerUnit, PackageOrderData packageOrderData){
        float price = beerUnitOrder.getNumber_unit() * beerUnit.getPrice();
        price *= (100 - voucherMerge.getDiscount()) / 100;
        price -= voucherMerge.getAmount();
        if (price < 0) {
            price = 0;
        }
        System.out.println("calculate beer unit: " + beerUnit.getBeer_unit_second_id() + ", price: " + price);
        beerUnitOrder.setPrice(price);
        WeightAndVolumetricContainer result = WeightAndVolumetricContainer
                .builder()
                .price(price)
                .weight(beerUnitOrder.getNumber_unit() * beerUnit.getWeight())
                .volumetric(beerUnitOrder.getNumber_unit() * beerUnit.getVolumetric())
                .build();
        //save beer order unit;
        if (!packageOrderData.isPreOrder()) {
            return beerUnitOrderRepository.save(beerUnitOrder.AutoFill(packageOrderData.getPackageOrder().getPackage_order_second_id()))
                    .then(Mono.just(result));
        }
        return Mono.just(result);
    }

    private Flux<PackageOrderData.BeerOrderData> getProductOrderDataModel(PackageOrderData packageOrderData) {
        return Flux.just(packageOrderData.getBeerOrders())
                .flatMap(beerOrderData ->
                        beerRepository.findBySecondIDCanOrder(beerOrderData.getBeerOrder().getBeer_second_id())
                                .map(beerOrderData::UpdateName)
                );
    }

    private Mono<Object> cleanPackage(PackageOrderData packageOrderData, Flux<PackageOrderData.BeerOrderData> beerOrderDataFlux) {
        if (packageOrderData.isPreOrder()) {
            return Mono.empty();
        }
        return beerOrderDataFlux
                .flatMap(beerOrder ->
                        Flux.just(beerOrder.getBeerUnitOrders())

                ).flatMap(beerUnitOrder -> {
                            System.out.println("Clean Package: " + packageOrderData.getPackageOrder().getUser_device_id() + ", " + beerUnitOrder.getBeer_unit_second_id());
                            return userPackageRepository.DeleteProductByBeerUnit(packageOrderData.getPackageOrder().getUser_device_id(), beerUnitOrder.getBeer_unit_second_id());
                        }
                ).then(Mono.empty());
    }

    public Mono<PackageOrder> createOrder(PackageOrderData packageOrderData) {
        System.out.println("New request from: " + packageOrderData.getPackageOrder().getUser_device_id());
        packageOrderData.getPackageOrder().AutoFill(packageOrderData.isPreOrder());
        Timestamp currentTime = new Timestamp(new Date().getTime());
        Mono<ShippingProvider.GHN> shippingProvider = shippingProviderAPI.GetGHNShippingProvider();
        Flux<PackageOrderData.BeerOrderData> beerOrderDataFlux = getProductOrderDataModel(packageOrderData);
        return Mono.just(getUserVoucherAndEmpty(packageOrderData))
                .flatMap(vouchers ->
                        vouchers.map(vc -> Util.getInstance().obserVoucher(packageOrderData.getPackageOrder().getUser_device_id() + vc.getVoucher_second_id()))
                                .then(shippingProvider
                                        .flatMap(ghn ->
                                                beerOrderDataFlux
                                                        .flatMap(beerOrder ->
                                                                Flux.just(beerOrder.getBeerUnitOrders())
                                                                        .flatMap(beerUnitOrder ->
                                                                                beerUnitRepository.findByBeerUnitIDCanOrder(beerUnitOrder.getBeer_unit_second_id())
                                                                                        .map(BeerUnit::CheckDiscount)
                                                                                        .map(beerUnitOrder::UpdateName)
                                                                                        .map(BeerUnit::UpdateToRealPrice)
                                                                                        .flatMap(beerUnit ->
                                                                                                caculatorVoucherOfProduct(currentTime, vouchers, packageOrderData.getPackageOrder().getUser_device_id(), beerOrder.getBeerOrder())
                                                                                                        .flatMap(voucherMerge ->
                                                                                                                calculatorProductPrice(voucherMerge, beerUnitOrder, beerUnit, packageOrderData))
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
                                                                        .flatMap(beerOrderWeightAndVolumetricContainer -> {
                                                                                    float price = beerOrderWeightAndVolumetricContainer.getPrice();
                                                                                    System.out.println("BeerOrder: " + beerOrder.getBeerOrder().getBeer_second_id() + ", Total price: " + price);
                                                                                    beerOrder.getBeerOrder().setTotal_price(price);
                                                                                    //save beer order
                                                                                    if (!packageOrderData.isPreOrder()) {
                                                                                        return beerOrderRepository.save(beerOrder.getBeerOrder().AutoFill(packageOrderData.getPackageOrder().getPackage_order_second_id()))
                                                                                                .then(Mono.just(beerOrderWeightAndVolumetricContainer));
                                                                                    }
                                                                                    return Mono.just(beerOrderWeightAndVolumetricContainer);
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
                                                        .flatMap(weightAndVolumetricContainer ->
                                                                calculatorVoucherForAllBeer(currentTime, vouchers, packageOrderData.getPackageOrder().getUser_device_id())
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
                                                                        }))
                                                        .flatMap(weightAndVolumetricContainerMono -> {
                                                            float real_price = weightAndVolumetricContainerMono.realPrice;
                                                            float total_price = weightAndVolumetricContainerMono.price;
                                                            System.out.println("Total Price: " + real_price);

                                                            if (real_price == 0) {
                                                                return Mono.just(packageOrderData.getPackageOrder());
                                                            }

                                                            float ship_price = ghn.CalculateShipPrice(weightAndVolumetricContainerMono.getWeight(), weightAndVolumetricContainerMono.getVolumetric(), packageOrderData.getPackageOrder().getRegion_id(), packageOrderData.getPackageOrder().getDistrict_id());
                                                            System.out.println("Total Ship Price: " + ship_price);

                                                            packageOrderData.getPackageOrder().setReal_price(real_price);
                                                            packageOrderData.getPackageOrder().setTotal_price(total_price);
                                                            packageOrderData.getPackageOrder().setShip_price(ship_price);

                                                            return vouchers.filter(voucher -> Util.getInstance().CleanMap(packageOrderData.getPackageOrder().getUser_device_id() + voucher.getVoucher_second_id()) && !packageOrderData.isPreOrder())
                                                                    .map(voucher -> {
                                                                        voucherServices
                                                                                .ForceSaveVoucher(voucher.getVoucher_second_id(), packageOrderData.getPackageOrder().getUser_device_id(),
                                                                                        Util.getInstance().CleanReuse(packageOrderData.getPackageOrder().getUser_device_id() + voucher.getVoucher_second_id()))
                                                                                .subscribe();
                                                                        return voucher;
                                                                    })
                                                                    .then(
                                                                            cleanPackage(packageOrderData, beerOrderDataFlux).then(
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
                                                        })
                                        )
                                )
                );
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class WeightAndVolumetricContainer{
        private float weight;
        private float volumetric;
        private float realPrice;
        private float price;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class VoucherMerge{
        private float amount;
        private float discount;
    }
}
