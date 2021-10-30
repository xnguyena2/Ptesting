package com.example.heroku;

import com.example.heroku.model.Beer;
import com.example.heroku.model.BeerUnit;
import com.example.heroku.model.BeerUnitOrder;
import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.repository.BeerUnitRepository;
import com.example.heroku.request.beer.BeerInfo;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.PackageOrderData;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.voucher.VoucherData;
import com.example.heroku.services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderPackageTest extends TestConfig {

    private static final int testcase = 30;

    @Autowired
    BeerUnitRepository beerUnitRepository;

    @Autowired
    UserDevice userDeviceAPI;

    @Autowired
    BeerOrder beerOrder;

    @Autowired
    ShippingProvider shippingProvider;

    @Autowired
    com.example.heroku.services.Beer beerAPI;

    @Autowired
    Voucher voucherAPI;

    @Autowired
    com.example.heroku.services.PackageOrder packageOrder;

    @Autowired
    com.example.heroku.services.Buyer buyer;


    @Autowired
    StatisticServices statisticServices;

    @Test
    public void testVoucher(){
        VoucherTest.builder().voucherAPI(voucherAPI).build().VoucherTest();
    }


    @Test
    public void OrderTest() throws Exception {
        createDevice();
        createProvider();
        createBeer();
        createVoucher();

        Thread[] threads = new Thread[testcase];

        for (int i = 0; i < threads.length; i++) {
            final int i1 = i;
            threads[i] = new Thread(() -> {
                System.out.println("Thread Running: " + (i1 + 1));
                orderTest();
            });
            //Thread.sleep(5000);
            threads[i].start();
        }
        for (Thread t :
                threads) {
            t.join();
        }

        voucherAPI.getAllMyVoucher("iphone")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    System.out.println(voucher.getVoucher_second_id());
                    System.out.println(voucher.getReuse());
                })
                .consumeNextWith(voucher -> {
                    System.out.println(voucher.getVoucher_second_id());
                    System.out.println(voucher.getReuse());
                })
                .consumeNextWith(voucher -> {
                    System.out.println(voucher.getVoucher_second_id());
                    System.out.println(voucher.getReuse());
                })
                .consumeNextWith(voucher -> {
                    System.out.println(voucher.getVoucher_second_id());
                    System.out.println(voucher.getReuse());
                })
                .consumeNextWith(voucher -> {
                    System.out.println(voucher.getVoucher_second_id());
                    System.out.println(voucher.getReuse());
                })
                .verifyComplete();

        beerAPI.CountSearchBeer(SearchQuery.builder().query("tigerrrrr").page(0).size(2).filter(SearchQuery.Filter.SOLD_NUM.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(3);
                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("beer_order1");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("beer_order2");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        beerAPI.CountSearchBeer(SearchQuery.builder().query("beer&tigerrrrr").page(0).size(2).filter(SearchQuery.Filter.SOLD_NUM.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(3);
                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("beer_order1");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("beer_order2");
                            })
                            .verifyComplete();
                })
                .verifyComplete();
        AtomicReference<String> orderDoneID = new AtomicReference<>();
        AtomicReference<String> orderCancelID = new AtomicReference<>();
        packageOrder.CountGetAllOrder(SearchQuery.builder().query(PackageOrder.Status.ORDER.getName()).page(0).size(200).build())
                .as(StepVerifier::create)
                .consumeNextWith(orderSearchResult -> {
                    orderDoneID.set(orderSearchResult.getResult().get(0).getPackage_order_second_id());
                    orderCancelID.set(orderSearchResult.getResult().get(1).getPackage_order_second_id());
                    System.out.println("Done order value: " + orderSearchResult.getResult().get(0).getReal_price());
                    System.out.println("Cancel order value: " + orderSearchResult.getResult().get(1).getReal_price());
                    assertThat(orderSearchResult.getCount()).isEqualTo(testcase * 8);
                    assertThat(orderSearchResult.getResult().size()).isEqualTo(Math.min(200, testcase * 8));
                })
                .verifyComplete();

        packageOrder.UpdateStatus(orderDoneID.get(), PackageOrder.Status.DONE).block();

        packageOrder.UpdateStatus(orderCancelID.get(), PackageOrder.Status.CANCEL).block();

        packageOrder.CountGetAllOrder(SearchQuery.builder().query(PackageOrder.Status.ORDER.getName()).page(0).size(300).build())
                .as(StepVerifier::create)
                .consumeNextWith(orderSearchResult -> {
                    assertThat(orderSearchResult.getCount()).isEqualTo(testcase * 8 - 2);
                    assertThat(orderSearchResult.getResult().size()).isEqualTo(testcase * 8 - 2);
                })
                .verifyComplete();

        packageOrder.getOrderDetail(orderDoneID.get())
                .as(StepVerifier::create)
                .consumeNextWith(packageOrderData -> {
                    assertThat(packageOrderData.getStatus()).isEqualTo(PackageOrder.Status.DONE);
                })
                .verifyComplete();

        packageOrder.getOrderDetail(orderCancelID.get())
                .as(StepVerifier::create)
                .consumeNextWith(packageOrderData -> {
                    assertThat(packageOrderData.getStatus()).isEqualTo(PackageOrder.Status.CANCEL);
                })
                .verifyComplete();

        buyer.GetAll(SearchQuery.builder().filter(PackageOrder.Status.ORDER.getName()).page(0).size(300).build())
                .as(StepVerifier::create)
                .consumeNextWith(buyerData -> {
                    assertThat(buyerData.getPhone_number_clean()).isEqualTo("1234567890");
                    assertThat(buyerData.getTotal_price()).isEqualTo(59935f - 106f * 2);
                })
                .verifyComplete();

        buyer.GetAll(SearchQuery.builder().filter(PackageOrder.Status.DONE.getName()).page(0).size(300).build())
                .as(StepVerifier::create)
                .consumeNextWith(buyerData -> {
                    assertThat(buyerData.getPhone_number_clean()).isEqualTo("1234567890");
                    assertThat(buyerData.getTotal_price()).isEqualTo(106f);
                })
                .verifyComplete();

        buyer.GetAll(SearchQuery.builder().filter(PackageOrder.Status.CANCEL.getName()).page(0).size(300).build())
                .as(StepVerifier::create)
                .consumeNextWith(buyerData -> {
                    assertThat(buyerData.getPhone_number_clean()).isEqualTo("1234567890");
                    assertThat(buyerData.getTotal_price()).isEqualTo(106f);
                })
                .verifyComplete();

        statisticServices.getTotal(SearchQuery.builder().filter("30").query(PackageOrder.Status.DONE.getName()).page(0).size(300).build())
                .as(StepVerifier::create)
                .consumeNextWith(totalOrder -> {
                    assertThat(totalOrder.getReal_price()).isEqualTo(106f);
                    assertThat(totalOrder.getTotal_price()).isEqualTo(106f);
                })
                .verifyComplete();

    }

    void orderTest() {

        AtomicReference<BeerUnit> beerUnit1= new AtomicReference<BeerUnit>();
        AtomicReference<BeerUnit> beerUnit2 = new AtomicReference<BeerUnit>();

        AtomicReference<BeerUnit> beerUnit3= new AtomicReference<BeerUnit>();
        AtomicReference<BeerUnit> beerUnit4 = new AtomicReference<BeerUnit>();

        AtomicReference<BeerUnit> beerUnitsold_out1= new AtomicReference<BeerUnit>();
        AtomicReference<BeerUnit> beerUnitsold_out2 = new AtomicReference<BeerUnit>();

        AtomicReference<BeerUnit> beerUnitsold_out21= new AtomicReference<BeerUnit>();
        AtomicReference<BeerUnit> beerUnitsold_out22 = new AtomicReference<BeerUnit>();

        AtomicReference<BeerUnit> beerUnithide1= new AtomicReference<BeerUnit>();
        AtomicReference<BeerUnit> beerUnithide2 = new AtomicReference<BeerUnit>();

        beerUnitRepository.findByBeerID("beer_order1")
                .as(StepVerifier::create)
                .consumeNextWith(beerUnit1::set)
                .consumeNextWith(beerUnit2::set)
        .verifyComplete();

        beerUnitRepository.findByBeerID("beer_order2")
                .as(StepVerifier::create)
                .consumeNextWith(beerUnit3::set)
                .consumeNextWith(beerUnit4::set)
                .verifyComplete();

        beerUnitRepository.findByBeerID("beer_order_sold_out1")
                .as(StepVerifier::create)
                .consumeNextWith(beerUnitsold_out1::set)
                .consumeNextWith(beerUnitsold_out2::set)
                .verifyComplete();

        beerUnitRepository.findByBeerID("beer_order_sold_out2")
                .as(StepVerifier::create)
                .consumeNextWith(beerUnitsold_out21::set)
                .consumeNextWith(beerUnitsold_out22::set)
                .verifyComplete();

        beerUnitRepository.findByBeerID("beer_order_hide2")
                .as(StepVerifier::create)
                .consumeNextWith(beerUnithide1::set)
                .consumeNextWith(beerUnithide2::set)
                .verifyComplete();

        if(beerUnit2.get().getName().equals("thung")){
            BeerUnit temp = beerUnit2.get();
            beerUnit2.set(beerUnit1.get());
            beerUnit1.set(temp);
        }

        if(beerUnit4.get().getName().equals("thung")){
            BeerUnit temp = beerUnit4.get();
            beerUnit4.set(beerUnit3.get());
            beerUnit3.set(temp);
        }

        if(beerUnitsold_out2.get().getName().equals("thung")){
            BeerUnit temp = beerUnitsold_out2.get();
            beerUnitsold_out2.set(beerUnitsold_out1.get());
            beerUnitsold_out1.set(temp);
        }

        if(beerUnitsold_out22.get().getName().equals("thung")){
            BeerUnit temp = beerUnitsold_out22.get();
            beerUnitsold_out22.set(beerUnitsold_out21.get());
            beerUnitsold_out21.set(temp);
        }

        if(beerUnithide2.get().getName().equals("thung")){
            BeerUnit temp = beerUnithide2.get();
            beerUnithide2.set(beerUnithide1.get());
            beerUnithide1.set(temp);
        }

        PackageOrderData packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .region_id(294)
                                .district_id(484)
                                .phone_number("1234567890")
                                .reciver_fullname("Nguyen Phong")
                                .reciver_address("232 bau cat, tan binh")
                                .user_device_id("android")
                                .build()
                )
                .preOrder(false)
                .beerOrders(
                        new PackageOrderData.BeerOrderData[]{
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .beerOrder(
                                                com.example.heroku.model.BeerOrder
                                                        .builder()
                                                        .beer_second_id("beer_order1")
                                                        .build()
                                        )
                                        .beerUnitOrders(
                                                new BeerUnitOrder[]{
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit1.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit1.get().getBeer())
                                                                .number_unit(10)
                                                                .build(),
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit2.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit2.get().getBeer())
                                                                .number_unit(1)
                                                                .build()
                                                }
                                        )
                                        .build()
                        }
                )
                .build();

        beerOrder.createOrder(packageOrderData)
                .as(StepVerifier::create)
                .consumeNextWith(packageOrder -> {
                    assertThat(packageOrder.getReal_price()).isEqualTo(106);// 10*10*0.9 + 20*1*0.8
                    assertThat(packageOrder.getShip_price()).isEqualTo(32000);// inside region and weight is 0!!
                })
                .verifyComplete();

        packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .region_id(294)
                                .district_id(484)
                                .phone_number("1234567890")
                                .reciver_fullname("Nguyen Phong")
                                .reciver_address("232 bau cat, tan binh")
                                .user_device_id("android")
                                .build()
                )
                .preOrder(false)
                .beerOrders(
                        new PackageOrderData.BeerOrderData[]{
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .beerOrder(
                                                com.example.heroku.model.BeerOrder
                                                        .builder()
                                                        .beer_second_id("beer_order1")
                                                        .voucher_second_id("ORDER_GIAM_5K")
                                                        .build()
                                        )
                                        .beerUnitOrders(
                                                new BeerUnitOrder[]{
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit1.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit1.get().getBeer())
                                                                .number_unit(10)
                                                                .build(),
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit2.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit2.get().getBeer())
                                                                .number_unit(2)
                                                                .build()
                                                }
                                        )
                                        .build()
                        }
                )
                .build();

        beerOrder.createOrder(packageOrderData)
                .as(StepVerifier::create)
                .consumeNextWith(packageOrder -> {
                    System.out.println("Using order ORDER_GIAM_5K: "+ packageOrder.getReal_price());
                    //assertThat(packageOrder.getTotal_price()).isEqualTo(117);// 10*10*0.9 + 20*1*0.8
                    //assertThat(packageOrder.getShip_price()).isEqualTo(42000);// inside region and weight is 0!!
                })
                .verifyComplete();

        packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .region_id(294)
                                .district_id(484)
                                .phone_number("1234567890")
                                .reciver_fullname("Nguyen Phong")
                                .reciver_address("232 bau cat, tan binh")
                                .user_device_id("android")
                                .build()
                )
                .preOrder(false)
                .beerOrders(
                        new PackageOrderData.BeerOrderData[]{
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .beerOrder(
                                                com.example.heroku.model.BeerOrder
                                                        .builder()
                                                        .beer_second_id("beer_order1")
                                                        .voucher_second_id("ORDER_GIAM_5Kk")
                                                        .build()
                                        )
                                        .beerUnitOrders(
                                                new BeerUnitOrder[]{
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit1.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit1.get().getBeer())
                                                                .number_unit(10)
                                                                .build(),
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit2.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit2.get().getBeer())
                                                                .number_unit(2)
                                                                .build()
                                                }
                                        )
                                        .build()
                        }
                )
                .build();

        beerOrder.createOrder(packageOrderData)
                .as(StepVerifier::create)
                .consumeNextWith(packageOrder -> {
                    assertThat(packageOrder.getReal_price()).isEqualTo(122);// 10*10*0.9 + 20*1*0.8
                    assertThat(packageOrder.getShip_price()).isEqualTo(42000);// inside region and weight is 0!!
                })
                .verifyComplete();

        packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .region_id(294)
                                .district_id(484)
                                .phone_number("1234567890")
                                .reciver_fullname("Nguyen Phong")
                                .reciver_address("232 bau cat, tan binh")
                                .user_device_id("iphone")
                                .build()
                )
                .preOrder(false)
                .beerOrders(
                        new PackageOrderData.BeerOrderData[]{
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .beerOrder(
                                                com.example.heroku.model.BeerOrder
                                                        .builder()
                                                        .beer_second_id("beer_order1")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .beerUnitOrders(
                                                new BeerUnitOrder[]{
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit1.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit1.get().getBeer())
                                                                .number_unit(10)
                                                                .build(),
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit2.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit2.get().getBeer())
                                                                .number_unit(2)
                                                                .build()
                                                }
                                        )
                                        .build()
                                ,
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .beerOrder(
                                                com.example.heroku.model.BeerOrder
                                                        .builder()
                                                        .beer_second_id("beer_order2")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .beerUnitOrders(
                                                new BeerUnitOrder[]{
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit3.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit3.get().getBeer())
                                                                .number_unit(10)
                                                                .build(),
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit4.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit4.get().getBeer())
                                                                .number_unit(5)
                                                                .build()
                                                }
                                        )
                                        .build()
                        }
                )
                .build();

        beerOrder.createOrder(packageOrderData)
                .as(StepVerifier::create)
                .consumeNextWith(packageOrder -> {
                    //assertThat(packageOrder.getTotal_price()).isEqualTo(365.4f);// 10*10*0.9 + 20*1*0.8
                    //assertThat(packageOrder.getShip_price()).isEqualTo(42000);// inside region and weight is 0!!
                })
                .verifyComplete();

        packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .region_id(294)
                                .district_id(484)
                                .phone_number("1234567890")
                                .reciver_fullname("Nguyen Phong")
                                .reciver_address("232 bau cat, tan binh")
                                .user_device_id("iphone")
                                .build()
                )
                .preOrder(false)
                .beerOrders(
                        new PackageOrderData.BeerOrderData[]{
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .beerOrder(
                                                com.example.heroku.model.BeerOrder
                                                        .builder()
                                                        .beer_second_id("beer_order1")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .beerUnitOrders(
                                                new BeerUnitOrder[]{
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit1.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit1.get().getBeer())
                                                                .number_unit(10)
                                                                .build(),
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit2.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit2.get().getBeer())
                                                                .number_unit(2)
                                                                .build()
                                                }
                                        )
                                        .build()
                                ,
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .beerOrder(
                                                com.example.heroku.model.BeerOrder
                                                        .builder()
                                                        .beer_second_id("beer_order2")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .beerUnitOrders(
                                                new BeerUnitOrder[]{
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit3.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit3.get().getBeer())
                                                                .number_unit(10)
                                                                .build(),
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit4.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit4.get().getBeer())
                                                                .number_unit(5)
                                                                .build()
                                                }
                                        )
                                        .build()
                        }
                )
                .build();

        beerOrder.createOrder(packageOrderData)
                .as(StepVerifier::create)
                .consumeNextWith(packageOrder -> {
                    //assertThat(packageOrder.getTotal_price()).isEqualTo(485.4f);// 10*10*0.9 + 20*1*0.8
                    //assertThat(packageOrder.getShip_price()).isEqualTo(42000);// inside region and weight is 0!!
                })
                .verifyComplete();


        packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .region_id(294)
                                .district_id(484)
                                .phone_number("1234567890")
                                .reciver_fullname("Nguyen Phong")
                                .reciver_address("232 bau cat, tan binh")
                                .user_device_id("iphone")
                                .build()
                )
                .preOrder(false)
                .beerOrders(
                        new PackageOrderData.BeerOrderData[]{
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .beerOrder(
                                                com.example.heroku.model.BeerOrder
                                                        .builder()
                                                        .beer_second_id("beer_order1")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .beerUnitOrders(
                                                new BeerUnitOrder[]{
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit1.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit1.get().getBeer())
                                                                .number_unit(10)
                                                                .build(),
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit2.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit2.get().getBeer())
                                                                .number_unit(2)
                                                                .build()
                                                }
                                        )
                                        .build()
                                ,
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .beerOrder(
                                                com.example.heroku.model.BeerOrder
                                                        .builder()
                                                        .beer_second_id("beer_order2")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .beerUnitOrders(
                                                new BeerUnitOrder[]{
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit3.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit3.get().getBeer())
                                                                .number_unit(10)
                                                                .build(),
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit4.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit4.get().getBeer())
                                                                .number_unit(5)
                                                                .build()
                                                }
                                        )
                                        .build()
                        }
                )
                .build();

        beerOrder.createOrder(packageOrderData)
                .as(StepVerifier::create)
                .consumeNextWith(packageOrder -> {
                    //assertThat(packageOrder.getTotal_price()).isEqualTo(522f);// 10*10*0.9 + 20*1*0.8
                    //assertThat(packageOrder.getShip_price()).isEqualTo(42000);// inside region and weight is 0!!
                })
                .verifyComplete();


        packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .region_id(294)
                                .district_id(484)
                                .phone_number("1234567890")
                                .reciver_fullname("Nguyen Phong")
                                .reciver_address("232 bau cat, tan binh")
                                .user_device_id("iphone")
                                .build()
                )
                .preOrder(false)
                .beerOrders(
                        new PackageOrderData.BeerOrderData[]{
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .beerOrder(
                                                com.example.heroku.model.BeerOrder
                                                        .builder()
                                                        .beer_second_id("beer_order1")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .beerUnitOrders(
                                                new BeerUnitOrder[]{
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit1.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit1.get().getBeer())
                                                                .number_unit(10)
                                                                .build(),
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit2.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit2.get().getBeer())
                                                                .number_unit(2)
                                                                .build()
                                                }
                                        )
                                        .build()
                                ,
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .beerOrder(
                                                com.example.heroku.model.BeerOrder
                                                        .builder()
                                                        .beer_second_id("beer_order_sold_out1")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .beerUnitOrders(
                                                new BeerUnitOrder[]{
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnitsold_out1.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnitsold_out1.get().getBeer())
                                                                .number_unit(10)
                                                                .build(),
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnitsold_out2.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnitsold_out2.get().getBeer())
                                                                .number_unit(5)
                                                                .build()
                                                }
                                        )
                                        .build()
                        }
                )
                .build();

        beerOrder.createOrder(packageOrderData)
                .as(StepVerifier::create)
                .consumeNextWith(packageOrder -> {
                    //assertThat(packageOrder.getTotal_price()).isEqualTo(522f);// 10*10*0.9 + 20*1*0.8
                    //assertThat(packageOrder.getShip_price()).isEqualTo(42000);// inside region and weight is 0!!
                })
                .verifyComplete();


        packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .region_id(294)
                                .district_id(484)
                                .phone_number("1234567890")
                                .reciver_fullname("Nguyen Phong")
                                .reciver_address("232 bau cat, tan binh")
                                .user_device_id("iphone")
                                .build()
                )
                .preOrder(false)
                .beerOrders(
                        new PackageOrderData.BeerOrderData[]{
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .beerOrder(
                                                com.example.heroku.model.BeerOrder
                                                        .builder()
                                                        .beer_second_id("beer_order1")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .beerUnitOrders(
                                                new BeerUnitOrder[]{
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit1.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit1.get().getBeer())
                                                                .number_unit(10)
                                                                .build(),
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnit2.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnit2.get().getBeer())
                                                                .number_unit(2)
                                                                .build()
                                                }
                                        )
                                        .build()
                                ,
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .beerOrder(
                                                com.example.heroku.model.BeerOrder
                                                        .builder()
                                                        .beer_second_id("beer_order_sold_out2")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .beerUnitOrders(
                                                new BeerUnitOrder[]{
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnitsold_out21.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnitsold_out21.get().getBeer())
                                                                .number_unit(10)
                                                                .build(),
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnitsold_out22.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnitsold_out22.get().getBeer())
                                                                .number_unit(5)
                                                                .build()
                                                }
                                        )
                                        .build()
                        }
                )
                .build();

        beerOrder.createOrder(packageOrderData)
                .as(StepVerifier::create)
                .consumeNextWith(packageOrder -> {
                    System.out.println(packageOrder.getReal_price());
                    assertThat(packageOrder.getReal_price()).isEqualTo(470.4f);// 10*110*0.7 + (20*10*0.8 + 2*10*0.9)*0.7
                    //assertThat(packageOrder.getShip_price()).isEqualTo(42000);// inside region and weight is 0!!
                })
                .verifyComplete();


        packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .region_id(294)
                                .district_id(484)
                                .phone_number("1234567890")
                                .reciver_fullname("Nguyen Phong")
                                .reciver_address("232 bau cat, tan binh")
                                .user_device_id("iphone")
                                .build()
                )
                .preOrder(false)
                .beerOrders(
                        new PackageOrderData.BeerOrderData[]{
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .beerOrder(
                                                com.example.heroku.model.BeerOrder
                                                        .builder()
                                                        .beer_second_id("beer_order_hide2")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .beerUnitOrders(
                                                new BeerUnitOrder[]{
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnithide1.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnithide1.get().getBeer())
                                                                .number_unit(10)
                                                                .build(),
                                                        BeerUnitOrder
                                                                .builder()
                                                                .beer_unit_second_id(beerUnithide2.get().getBeer_unit_second_id())
                                                                .beer_second_id(beerUnithide2.get().getBeer())
                                                                .number_unit(5)
                                                                .build()
                                                }
                                        )
                                        .build()
                        }
                )
                .build();

        beerOrder.createOrder(packageOrderData)
                .as(StepVerifier::create)
                .consumeNextWith(packageOrder -> {
                    System.out.println(packageOrder.getReal_price());
                    assertThat(packageOrder.getReal_price()).isEqualTo(0f);// 10*110*0.7 + (20*10*0.8 + 2*10*0.9)*0.7
                    //assertThat(packageOrder.getShip_price()).isEqualTo(42000);// inside region and weight is 0!!
                })
                .verifyComplete();

    }

    void createVoucher() {

        voucherAPI.createVoucher(
                        VoucherData.builder()
                                .for_all_beer(true)
                                .for_all_user(true)
                                .voucher_second_id("ORDER_GIAM_5K")
                                .detail("Giảm 5k trên toàn bộ sản phẩm")
                                .amount(5)
                                .reuse(25)
                                .build()
                )
                .block();

        voucherAPI.createVoucher(
                        VoucherData.builder()
                                .for_all_beer(true)
                                .voucher_second_id("ORDER_GIAM_30%")
                                .discount(30)
                                .reuse(200)
                                .detail("Giảm 30% trên toàn bộ sản phẩm, chỉ áp dụng cho ai nhận được.")
                                .listUser(new String[]{"iphone"})
                                .build())
                .block();

        voucherAPI.createVoucher(
                        VoucherData.builder()
                                .voucher_second_id("ORDER_GIAM_50k")
                                .detail("Giảm 50k trên 1 loại sản phẩm. Chỉ áp dụng cho ai nhận được.")
                                .amount(50)
                                .reuse(2)
                                .listBeer(new String[]{"beer_order1", "beer_order2"})
                                .listUser(new String[]{"android", "iphone"})
                                .build())
                .block();
    }

    void createBeer() {

        beerAPI.CreateBeer(
                BeerInfo
                        .builder()
                        .beerUnit(new BeerUnit[]{
                                BeerUnit
                                        .builder()
                                        .beer("beer_order1")
                                        .price(10)
                                        .weight(0.3f)
                                        .discount(10)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("thung")
                                        .build(),
                                BeerUnit
                                        .builder()
                                        .beer("beer_order1")
                                        .price(20)
                                        .weight(1)
                                        .discount(20)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("lon")
                                        .build()
                        })
                        .beer(Beer
                                .builder()
                                .category(Beer.Category.CRAB)
                                .name("beer tigerrrrr")
                                .beer_second_id("beer_order1")
                                .detail("bia for order 1")
                                .build()
                                .AutoFill()
                        )
                        .build()
        )
                .blockLast();

        beerAPI.CreateBeer(
                BeerInfo
                        .builder()
                        .beerUnit(new BeerUnit[]{
                                BeerUnit
                                        .builder()
                                        .beer("beer_order2")
                                        .price(50)
                                        .discount(50)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("thung")
                                        .build(),
                                BeerUnit
                                        .builder()
                                        .beer("beer_order2")
                                        .price(60)
                                        .discount(50)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("lon")
                                        .build()
                        })
                        .beer(Beer
                                .builder()
                                .category(Beer.Category.CRAB)
                                .name("beer tigerrrrr")
                                .beer_second_id("beer_order2")
                                .detail("bia for order 2")
                                .build()
                                .AutoFill()
                        )
                        .build()
        )
                .blockLast();

        beerAPI.CreateBeer(
                BeerInfo
                        .builder()
                        .beerUnit(new BeerUnit[]{
                                BeerUnit
                                        .builder()
                                        .beer("beer_order3")
                                        .price(100)
                                        .discount(10)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("thung")
                                        .build(),
                                BeerUnit
                                        .builder()
                                        .beer("beer_order3")
                                        .price(110)
                                        .name("lon")
                                        .build()
                        })
                        .beer(Beer
                                .builder()
                                .category(Beer.Category.CRAB)
                                .name("beer tigerrrrr")
                                .beer_second_id("beer_order3")
                                .detail("bia for order 3")
                                .build()
                                .AutoFill()
                        )
                        .build()
        )
                .blockLast();

        beerAPI.CreateBeer(
                        BeerInfo
                                .builder()
                                .beerUnit(new BeerUnit[]{
                                        BeerUnit
                                                .builder()
                                                .beer("beer_order_sold_out1")
                                                .price(100)
                                                .discount(10)
                                                .date_expire(new Timestamp(new Date().getTime()))
                                                .name("thung")
                                                .build(),
                                        BeerUnit
                                                .builder()
                                                .beer("beer_order_sold_out1")
                                                .price(110)
                                                .name("lon")
                                                .build()
                                })
                                .beer(Beer
                                        .builder()
                                        .category(Beer.Category.CRAB)
                                        .name("sold out 1")
                                        .beer_second_id("beer_order_sold_out1")
                                        .detail("sold out 1")
                                        .status(Beer.Status.SOLD_OUT)
                                        .build()
                                        .AutoFill()
                                )
                                .build()
                )
                .blockLast();

        beerAPI.CreateBeer(
                        BeerInfo
                                .builder()
                                .beerUnit(new BeerUnit[]{
                                        BeerUnit
                                                .builder()
                                                .beer("beer_order_sold_out2")
                                                .price(100)
                                                .discount(10)
                                                .date_expire(new Timestamp(new Date().getTime()))
                                                .name("thung")
                                                .status(BeerUnit.Status.SOLD_OUT)
                                                .build(),
                                        BeerUnit
                                                .builder()
                                                .beer("beer_order_sold_out2")
                                                .price(110)
                                                .name("lon")
                                                .build()
                                })
                                .beer(Beer
                                        .builder()
                                        .category(Beer.Category.CRAB)
                                        .name("sold out 2")
                                        .beer_second_id("beer_order_sold_out2")
                                        .detail("sold out 2")
                                        .build()
                                        .AutoFill()
                                )
                                .build()
                )
                .blockLast();

        beerAPI.CreateBeer(
                        BeerInfo
                                .builder()
                                .beerUnit(new BeerUnit[]{
                                        BeerUnit
                                                .builder()
                                                .beer("beer_order_hide2")
                                                .price(100)
                                                .discount(10)
                                                .date_expire(new Timestamp(new Date().getTime()))
                                                .name("thung")
                                                .build(),
                                        BeerUnit
                                                .builder()
                                                .beer("beer_order_hide2")
                                                .price(110)
                                                .name("lon")
                                                .build()
                                })
                                .beer(Beer
                                        .builder()
                                        .category(Beer.Category.CRAB)
                                        .name("sold out 2")
                                        .beer_second_id("beer_order_hide2")
                                        .status(Beer.Status.HIDE)
                                        .detail("sold out 2")
                                        .build()
                                        .AutoFill()
                                )
                                .build()
                )
                .blockLast();
    }

    void createDevice(){
        userDeviceAPI.CreateUserDevice(com.example.heroku.model.UserDevice.builder().device_id("android").user_first_name("Nguyen").user_last_name("phong").build())
                .block();
        userDeviceAPI.CreateUserDevice(com.example.heroku.model.UserDevice.builder().device_id("iphone").user_first_name("Ho DUong").user_last_name("Vuong").build())
                .block();

    }

    void createProvider() throws Exception {

        String json = "{\n" +
                "    \"weigitExchange\":0.0002,\n" +
                "    \"listPackagePriceDetail\":[\n" +
                "        {\n" +
                "            \"reciverLocation\": \"INSIDE_REGION\",\n" +
                "            \"maxWeight\": 3,\n" +
                "            \"priceMaxWeight\": 22000,\n" +
                "            \"nextWeight\":0.5,\n" +
                "            \"priceNextWeight\":5000\n" +
                "        },\n" +
                "        {\n" +
                "            \"reciverLocation\": \"OUTSIDE_REGION_TYPE1\",\n" +
                "            \"maxWeight\": 1,\n" +
                "            \"priceMaxWeight\": 37000,\n" +
                "            \"nextWeight\":0.5,\n" +
                "            \"priceNextWeight\":6600\n" +
                "        },\n" +
                "        {\n" +
                "            \"reciverLocation\": \"OUTSIDE_REGION_TYPE2\",\n" +
                "            \"maxWeight\": 1,\n" +
                "            \"priceMaxWeight\": 37000,\n" +
                "            \"nextWeight\":0.5,\n" +
                "            \"priceNextWeight\":6600\n" +
                "        },\n" +
                "        {\n" +
                "            \"reciverLocation\": \"INSIDE_GREGION\",\n" +
                "            \"maxWeight\": 1,\n" +
                "            \"priceMaxWeight\": 37000,\n" +
                "            \"nextWeight\":0.5,\n" +
                "            \"priceNextWeight\":7000\n" +
                "        },\n" +
                "        {\n" +
                "            \"reciverLocation\": \"DIFFIRENT_GPREGION\",\n" +
                "            \"maxWeight\": 1,\n" +
                "            \"priceMaxWeight\": 37000,\n" +
                "            \"nextWeight\":0.5,\n" +
                "            \"priceNextWeight\":7700\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        shippingProvider.CreateShipProvider(ShippingProvider.GHN.ID, json)
                .block();
    }
}
