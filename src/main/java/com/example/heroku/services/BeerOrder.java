package com.example.heroku.services;

import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.repository.*;
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

@Component
public class BeerOrder {

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

    public Mono<PackageOrder> createOrder(PackageOrderData packageOrderData) {
        packageOrderData.getPackageOrder().AutoFill(packageOrderData.isPreOrder());
        Timestamp currentTime = new Timestamp(new Date().getTime());
        return Mono.just(voucherServices.getAllMyVoucher(packageOrderData.getPackageOrder().getUser_device_id()))
                .flatMap(vouchers ->
                        shippingProviderAPI.GetGHNShippingProvider()
                                .flatMap(ghn ->
                                        Flux.just(packageOrderData.getBeerOrders())
                                                .flatMap(beerOrder ->
                                                        Flux.just(beerOrder.getBeerUnitOrders())
                                                                .flatMap(beerUnitOrder ->
                                                                        beerUnitRepository.findByBeerUnitID(beerUnitOrder.getBeer_unit_second_id())
                                                                                .flatMap(beerUnit -> {
                                                                                    float price = beerUnitOrder.getNumber_unit() * beerUnit.getPrice();
                                                                                    if (beerUnit.getDiscount() > 0) {
                                                                                        if (Util.getInstance().DiffirentDays(beerUnit.getDate_expire(), currentTime) >= 0) {
                                                                                            price -= price * beerUnit.getDiscount() / 100;
                                                                                            System.out.println("--------> beer unit: " + beerUnit.getBeer_unit_second_id() + ", discount: " + beerUnit.getDiscount() + "%");
                                                                                        }
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
                                                                                })
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
                                                                .flatMap(beerOrderWeightAndVolumetricContainer ->
                                                                        vouchers.filter(voucher -> voucher.getVoucher_second_id().equals(beerOrder.getBeerOrder().getVoucher_second_id()))
                                                                                .switchIfEmpty(Mono.just(com.example.heroku.model.Voucher.builder().reuse(0).build()))
                                                                                .flatMap(voucher ->
                                                                                {
                                                                                    VoucherMerge voucherMerge = VoucherMerge.builder().amount(0).discount(0).build();
                                                                                    if (voucher.getReuse() > 0 && Util.getInstance().DiffirentDays(voucher.getDate_expire(), currentTime) >= 0) {
                                                                                        if (voucher.getAmount() > 0) {
                                                                                            voucherMerge.setAmount(voucher.getAmount());
                                                                                            System.out.println("--------> BeerOrder: " + beerOrder.getBeerOrder().getBeer_second_id() + ", voucher: " + voucher.getVoucher_second_id() + ", amount: " + voucher.getAmount());
                                                                                        } else if (voucher.getDiscount() > 0) {
                                                                                            voucherMerge.setDiscount(voucher.getDiscount());
                                                                                            System.out.println("--------> BeerOrder: " + beerOrder.getBeerOrder().getBeer_second_id() + ", voucher: " + voucher.getVoucher_second_id() + ", discount: " + voucher.getDiscount() + "%");
                                                                                        }
                                                                                        voucher.setReuse(voucher.getReuse() - 1);
                                                                                        //comsume voucher here
                                                                                        if (!packageOrderData.isPreOrder()) {
                                                                                            return voucherServices.comsumeVoucher(voucher.getVoucher_second_id(), packageOrderData.getPackageOrder().getUser_device_id(), beerOrder.getBeerOrder().getBeer_second_id())
                                                                                                    .then(Mono.just(voucherMerge));
                                                                                        }
                                                                                    }
                                                                                    return Mono.just(voucherMerge);
                                                                                })
                                                                                .reduce(VoucherMerge.builder().amount(0).discount(0).build(),
                                                                                        (x1, x2) ->
                                                                                                VoucherMerge.builder()
                                                                                                        .amount(x1.getAmount() + x2.getAmount())
                                                                                                        .discount(x1.getDiscount() + x2.getDiscount())
                                                                                                        .build()
                                                                                )
                                                                                .flatMap(voucherMerge -> {
                                                                                    float price = beerOrderWeightAndVolumetricContainer.getPrice();
                                                                                    if (voucherMerge.discount > 0) {
                                                                                        price -= price * voucherMerge.discount / 100;
                                                                                    }
                                                                                    if (voucherMerge.amount > 0) {
                                                                                        price -= voucherMerge.amount;
                                                                                    }
                                                                                    System.out.println("BeerOrder: " + beerOrder.getBeerOrder().getBeer_second_id() + ", Total price: " + price);
                                                                                    beerOrder.getBeerOrder().setTotal_price(price);
                                                                                    beerOrderWeightAndVolumetricContainer.setPrice(price);
                                                                                    //save beer order
                                                                                    if (!packageOrderData.isPreOrder()) {
                                                                                        return beerOrderRepository.save(beerOrder.getBeerOrder().AutoFill(packageOrderData.getPackageOrder().getPackage_order_second_id()))
                                                                                                .then(Mono.just(beerOrderWeightAndVolumetricContainer));
                                                                                    }
                                                                                    return Mono.just(beerOrderWeightAndVolumetricContainer);
                                                                                })
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
                                                .flatMap(weightAndVolumetricContainerMono -> {
                                                            float ship_price = ghn.CalculateShipPrice(weightAndVolumetricContainerMono.getWeight(), weightAndVolumetricContainerMono.getVolumetric(), packageOrderData.getPackageOrder().getRegion_id(), packageOrderData.getPackageOrder().getDistrict_id());
                                                            System.out.println("Total Ship Price: " + ship_price);
                                                            float total_price = weightAndVolumetricContainerMono.price;
                                                            System.out.println("Total Price: " + total_price);
                                                            packageOrderData.getPackageOrder().setTotal_price(total_price);
                                                            packageOrderData.getPackageOrder().setShip_price(ship_price);
                                                            if (!packageOrderData.isPreOrder()) {
                                                                return packageOrderRepository.save(packageOrderData.getPackageOrder());
                                                            }
                                                            return Mono.just(packageOrderData.getPackageOrder());
                                                        }
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
