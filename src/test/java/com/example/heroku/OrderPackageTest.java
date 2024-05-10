package com.example.heroku;

import com.example.heroku.model.*;
import com.example.heroku.model.Buyer;
import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.repository.BeerUnitRepository;
import com.example.heroku.request.beer.*;
import com.example.heroku.request.client.UserID;
import com.example.heroku.request.ship.ShippingProviderData;
import com.example.heroku.request.voucher.VoucherData;
import com.example.heroku.response.BuyerData;
import com.example.heroku.response.PackageDataResponse;
import com.example.heroku.response.ProductInPackageResponse;
import com.example.heroku.services.*;
import com.example.heroku.services.ShippingProvider;
import com.example.heroku.services.UserDevice;
import com.example.heroku.services.UserPackage;
import com.example.heroku.services.Voucher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
    UserPackage userPackageAPI;

    @Autowired
    StatisticServices statisticServices;

    @Autowired
    DeleteAllData deleteAllData;


    @Value("${account.admin.username}")
    private String adminName;

    String mainGroup = Config.mainGroup;

    String otherGroup = Config.otherGroup;

    @Test
    public void testVoucher() {
        VoucherTest.builder().voucherAPI(voucherAPI).group(mainGroup).build().VoucherTest();
    }

    @Test
    public void OrderTest() throws Exception {

        AtomicReference<Float> voucher5K = new AtomicReference<>((float) 0);
        AtomicReference<Float> voucher30Percent = new AtomicReference<>((float) 0);
        AtomicReference<Float> packageVoucher5K = new AtomicReference<>((float) 0);

        makeOrder(mainGroup, voucher5K, voucher30Percent, packageVoucher5K);

        System.out.println("Delete all data of user: " + adminName);
        deleteAllData.seftMarkDelete(adminName).block();
    }

    @Test
    public void testVoucher2() {
//        VoucherTest.builder().voucherAPI(voucherAPI).group(otherGroup).build().VoucherTest();
    }

    @Test
    public void OrderTest2() throws Exception {
        VoucherTest.builder().voucherAPI(voucherAPI).group(otherGroup).build().VoucherTest();

        AtomicReference<Float> voucher5K = new AtomicReference<>((float) 0);
        AtomicReference<Float> voucher30Percent = new AtomicReference<>((float) 0);
        AtomicReference<Float> packageVoucher5K = new AtomicReference<>((float) 0);

        makeOrder(otherGroup, voucher5K, voucher30Percent, packageVoucher5K);

//        deleteAllData.deleteByGroupID(otherGroup).block();
    }

    void makeOrder(String group, AtomicReference<Float> voucher5K, AtomicReference<Float> voucher30Percent, AtomicReference<Float> packageVoucher5K)  throws Exception {
        final String mainGroup = group;
        final int totalSaveOrder = 10;// if not save success will 8

        createDevice(mainGroup);
        createProvider(mainGroup);
        createBeer(mainGroup);
        createVoucher(mainGroup);

        Thread[] threads = new Thread[testcase];

        for (int i = 0; i < threads.length; i++) {
            final int i1 = i;
            threads[i] = new Thread(() -> {
                System.out.println("Thread Running: " + (i1 + 1));
                orderTest(mainGroup, voucher5K, voucher30Percent, packageVoucher5K);
            });
            //Thread.sleep(5000);
            threads[i].start();
        }
        for (Thread t :
                threads) {
            t.join();
        }


        assertThat(voucher5K.get()).isEqualTo(122 * 5 + (122 - 5) * 25);
        assertThat(voucher30Percent.get().intValue()).isEqualTo((int) (122 * 10 + (122 * 0.7) * 20));
        assertThat(packageVoucher5K.get().intValue()).isEqualTo((int) (672 * 10 + (672 - 5) * 20));


        voucherAPI.getVoucherByID(mainGroup, "PACKAGE_VOUCHER_30%")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    System.out.println("package voucher: " + voucher.getVoucher_second_id() + ", reuse: " + voucher.getReuse());
                    assertThat(voucher.isFor_all_user()).isEqualTo(true);
                    assertThat(voucher.getReuse()).isEqualTo(45);
                    assertThat(voucher.getListBeer()).isEqualTo(new String[]{});
                    assertThat(voucher.getListUser()).isEqualTo(new String[]{"iphone"});
                })
                .verifyComplete();


        voucherAPI.getPackageVoucher(mainGroup, "PACKAGE_VOUCHER_30%", "hello")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    System.out.println("package voucher of hello: " + voucher.getVoucher_second_id() + ", reuse: " + voucher.getReuse());
                    assertThat(voucher.getReuse()).isEqualTo(45);
                })
                .verifyComplete();


        voucherAPI.getPackageVoucher(mainGroup, "PACKAGE_VOUCHER_30%", "iphone")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    System.out.println("package voucher of iphone: " + voucher.getVoucher_second_id() + ", reuse: " + voucher.getReuse());
                    assertThat(voucher.getReuse()).isEqualTo(15);
                })
                .verifyComplete();

        voucherAPI.getPackageVoucher(mainGroup, "PACKAGE_VOUCHER_5K", "hello")
                .as(StepVerifier::create)
                .verifyComplete();

        voucherAPI.getPackageVoucher(mainGroup, "PACKAGE_VOUCHER_5K", "android")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    System.out.println("package voucher of android: " + voucher.getVoucher_second_id() + ", reuse: " + voucher.getReuse());
                    assertThat(voucher.getReuse()).isEqualTo(20);
                })
                .verifyComplete();

        voucherAPI.getPackageVoucher(mainGroup, "PACKAGE_VOUCHER_5K", "iphone")
                .as(StepVerifier::create)
                .verifyComplete();

        voucherAPI.getAllMyVoucher(mainGroup, "iphone")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    System.out.println("Group: " + voucher.getGroup_id() + ", voucher of iphone: " + voucher.getVoucher_second_id() + ", reuse: " + voucher.getReuse());
                })
                .consumeNextWith(voucher -> {
                    System.out.println("Group: " + voucher.getGroup_id() + ", voucher of iphone: " + voucher.getVoucher_second_id() + ", reuse: " + voucher.getReuse());
                })
                .consumeNextWith(voucher -> {
                    System.out.println("Group: " + voucher.getGroup_id() + ", voucher of iphone: " + voucher.getVoucher_second_id() + ", reuse: " + voucher.getReuse());
                })
                .consumeNextWith(voucher -> {
                    System.out.println("Group: " + voucher.getGroup_id() + ", voucher of iphone: " + voucher.getVoucher_second_id() + ", reuse: " + voucher.getReuse());
                })
//                .consumeNextWith(voucher -> {
//                    System.out.println("Group: " + voucher.getGroup_id() + ", voucher of iphone: " + voucher.getVoucher_second_id() + ", reuse: " + voucher.getReuse());
//                })
                .verifyComplete();

        beerAPI.CountSearchBeer(SearchQuery.builder().query("tigerrrrr").page(0).size(2).filter(SearchQuery.Filter.SOLD_NUM.getName()).group_id(mainGroup).build())
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

        beerAPI.CountSearchBeer(SearchQuery.builder().query("beer&tigerrrrr").page(0).size(2).filter(SearchQuery.Filter.SOLD_NUM.getName()).group_id(mainGroup).build())
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

        AtomicReference<Float> donePointDiscount = new AtomicReference<>((float) 0);
        AtomicReference<Float> cancelPointDiscount = new AtomicReference<>((float) 0);

        AtomicReference<Float> donePrice = new AtomicReference<>((float) 0);
        AtomicReference<Float> cancelPrice = new AtomicReference<>((float) 0);
        AtomicReference<String> orderDoneID = new AtomicReference<>();
        AtomicReference<String> orderCancelID = new AtomicReference<>();
        AtomicReference<String> orderDonePhone = new AtomicReference<>();
        AtomicReference<String> orderCancelPhone = new AtomicReference<>();
        packageOrder.CountGetAllOrder(SearchQuery.builder().query(PackageOrder.Status.ORDER.getName()).group_id(mainGroup).page(0).size(200).filter("30").build())
                .as(StepVerifier::create)
                .consumeNextWith(orderSearchResult -> {
                    System.out.println("total order: " + orderSearchResult.getCount());
                    System.out.println("total order size: " + orderSearchResult.getResult().size());
                    orderDonePhone.set(orderSearchResult.getResult().get(0).getPhone_number_clean());
                    orderCancelPhone.set(orderSearchResult.getResult().get(1).getPhone_number_clean());
                    orderDoneID.set(orderSearchResult.getResult().get(0).getPackage_order_second_id());
                    orderCancelID.set(orderSearchResult.getResult().get(1).getPackage_order_second_id());
                    donePrice.updateAndGet(v -> v + orderSearchResult.getResult().get(0).getReal_price());
                    cancelPrice.updateAndGet(v -> v + orderSearchResult.getResult().get(1).getReal_price());
                    donePointDiscount.updateAndGet(v -> v + orderSearchResult.getResult().get(0).getDiscount());
                    cancelPointDiscount.updateAndGet(v -> v + orderSearchResult.getResult().get(1).getDiscount());
                    System.out.println("Done phone clean value: " + orderDonePhone.get());
                    System.out.println("Cancel phone clean value: " + orderCancelPhone.get());
                    System.out.println("Done order value: " + donePrice.get());
                    System.out.println("Cancel order value: " + cancelPrice.get());
                    System.out.println("Done point_discount value: " + donePointDiscount.get());
                    System.out.println("Cancel point_discount value: " + cancelPointDiscount.get());
                    assertThat(orderSearchResult.getCount()).isEqualTo(testcase * totalSaveOrder);
                    assertThat(orderSearchResult.getResult().size()).isEqualTo(Math.min(200, testcase * totalSaveOrder));
                })
                .verifyComplete();

        packageOrder.UpdateStatus(mainGroup, orderDoneID.get(), PackageOrder.Status.DONE).block();

        packageOrder.UpdateStatus(mainGroup, orderCancelID.get(), PackageOrder.Status.CANCEL).block();

        packageOrder.CountGetAllOrder(SearchQuery.builder().query(PackageOrder.Status.ORDER.getName()).group_id(mainGroup).page(0).size(300).filter("30").build())
                .as(StepVerifier::create)
                .consumeNextWith(orderSearchResult -> {
                    assertThat(orderSearchResult.getCount()).isEqualTo(testcase * totalSaveOrder - 2);
                    assertThat(orderSearchResult.getResult().size()).isEqualTo(testcase * totalSaveOrder - 2);
                })
                .verifyComplete();

        packageOrder.getOrderDetail(mainGroup, orderDoneID.get())
                .as(StepVerifier::create)
                .consumeNextWith(packageOrderData -> {
                    assertThat(packageOrderData.getStatus()).isEqualTo(PackageOrder.Status.DONE);
                })
                .verifyComplete();

        packageOrder.getOrderDetail(mainGroup, orderCancelID.get())
                .as(StepVerifier::create)
                .consumeNextWith(packageOrderData -> {
                    assertThat(packageOrderData.getStatus()).isEqualTo(PackageOrder.Status.CANCEL);
                })
                .verifyComplete();

        buyer.GetAll(SearchQuery.builder().filter(PackageOrder.Status.ORDER.getName()).group_id(mainGroup).page(0).size(500).build())
                .sort(Comparator.comparing(BuyerData::getDiscount))
                .as(StepVerifier::create)
                .consumeNextWith(buyerData -> {
                    float sub = 0;
                    if(buyerData.getPhone_number_clean().equals(orderDonePhone.get())){
                        sub += donePrice.get();
                    }
                    if(buyerData.getPhone_number_clean().equals(orderCancelPhone.get())){
                        sub += cancelPrice.get();
                    }
                    System.out.println("sub: " + sub + ", total sub: " + (donePrice.get() + cancelPrice.get()));
                    assertThat(buyerData.getPhone_number_clean()).isEqualTo("12345678909");
                    assertThat(buyerData.getDiscount()).isEqualTo(0);
                    assertThat(buyerData.getReal_price()).isEqualTo(10375 - sub);// 10375 = 122*5 + 117*25 + 106*30 + 122*30
                })
                .consumeNextWith(buyerData -> {
                    float sub = 0;
                    if(buyerData.getPhone_number_clean().equals(orderDonePhone.get())){
                        sub += donePrice.get();
                    }
                    if(buyerData.getPhone_number_clean().equals(orderCancelPhone.get())){
                        sub += cancelPrice.get();
                    }
                    System.out.println("sub: " + sub + ", total sub: " + (donePrice.get() + cancelPrice.get()));
                    assertThat(buyerData.getPhone_number_clean()).isEqualTo("1234567890");
                    assertThat(buyerData.getDiscount()).isEqualTo(30 - cancelPointDiscount.get() - donePointDiscount.get());
                    assertThat(buyerData.getReal_price()).isEqualTo(80361f - 10375 - sub);// 80361 = (106 + 122 + 365.4*3 + 672 * 0.7) * 30 + (122*5 + (122-5)*25) + (122*10 + 122*20*0.7) + (672*10 + (672-5)*20)
                })
                .verifyComplete();

        buyer.GetAllDirect(SearchQuery.builder().group_id(mainGroup).page(0).size(500).build())
                .sort(Comparator.comparing(BuyerData::getDiscount))
                .as(StepVerifier::create)
                .consumeNextWith(buyerData -> {
                    assertThat(buyerData.getPhone_number_clean()).isEqualTo("12345678909");
                    assertThat(buyerData.getDiscount()).isEqualTo(0);
                    assertThat(buyerData.getReal_price()).isEqualTo(10375);// 10375 = 122*5 + 117*25 + 106*30 + 122*30
                })
                .consumeNextWith(buyerData -> {
                    assertThat(buyerData.getPhone_number_clean()).isEqualTo("1234567890");
                    assertThat(buyerData.getDiscount()).isEqualTo(30);
                    assertThat(buyerData.getReal_price()).isEqualTo(69986.0f);// 69986 = (365.4*3 + 672 * 0.7) * 30 + (122*10 + 122*20*0.7) + (672*10 + (672-5)*20)
                })
                .verifyComplete();

        buyer.FindByPhone(SearchQuery.builder().query("Pong P").group_id(mainGroup).page(0).size(500).build())
                .as(StepVerifier::create)
                .consumeNextWith(buyerData -> {
                    assertThat(buyerData.getPhone_number_clean()).isEqualTo("1234567890");
                    assertThat(buyerData.getDiscount()).isEqualTo(30);
                    assertThat(buyerData.getReal_price()).isEqualTo(69986.0f);// 69986 = (365.4*3 + 672 * 0.7) * 30 + (122*10 + 122*20*0.7) + (672*10 + (672-5)*20)
                })
                .verifyComplete();

        buyer.FindByPhoneClean(SearchQuery.builder().query("12345678909").group_id(mainGroup).page(0).size(500).build())
                .as(StepVerifier::create)
                .consumeNextWith(buyerData -> {
                    assertThat(buyerData.getPhone_number_clean()).isEqualTo("12345678909");
                    assertThat(buyerData.getDiscount()).isEqualTo(0);
                    assertThat(buyerData.getReal_price()).isEqualTo(10375);
                })
                .verifyComplete();

        buyer.FindByPhone(SearchQuery.builder().query("nophone").group_id(mainGroup).page(0).size(500).build())
                .as(StepVerifier::create)
                .verifyComplete();

        buyer.FindByPhoneClean(SearchQuery.builder().query("nophone").group_id(mainGroup).page(0).size(500).build())
                .as(StepVerifier::create)
                .verifyComplete();

        buyer.insertOrUpdate(Buyer.builder()
                        .group_id(mainGroup)
                        .phone_number("03030303030")
                        .reciver_fullname("phong nguyen")
                .build().AutoFill(), 0,0,0,0, 0).block();

        buyer.insertOrUpdate(Buyer.builder()
                .group_id(mainGroup)
                .phone_number("04040404040")
                .reciver_fullname("phong nguyen")
                .build().AutoFill(), 0,0,0,0, 0).block();

        buyer.GetAllDirect(SearchQuery.builder().group_id(mainGroup).page(0).size(500).build())
                .sort(Comparator.comparing(BuyerData::getDevice_id))
                .as(StepVerifier::create)
                .consumeNextWith(buyerData -> {
                    assertThat(buyerData.getReciver_fullname()).isEqualTo("phong nguyen");
                    assertThat(buyerData.getRegion()).isEqualTo(null);
                    assertThat(buyerData.getDistrict()).isEqualTo(null);
                    assertThat(buyerData.getWard()).isEqualTo(null);
                })
                .consumeNextWith(buyerData -> {
                    assertThat(buyerData.getReciver_fullname()).isEqualTo("phong nguyen");
                    assertThat(buyerData.getRegion()).isEqualTo(null);
                    assertThat(buyerData.getDistrict()).isEqualTo(null);
                    assertThat(buyerData.getWard()).isEqualTo(null);
                })
                .consumeNextWith(buyerData -> {
                    assertThat(buyerData.getPhone_number_clean()).isEqualTo("12345678909");
                    assertThat(buyerData.getDiscount()).isEqualTo(0);
                    assertThat(buyerData.getReal_price()).isEqualTo(10375);// 10375 = 122*5 + 117*25 + 106*30 + 122*30
                })
                .consumeNextWith(buyerData -> {
                    assertThat(buyerData.getPhone_number_clean()).isEqualTo("1234567890");
                    assertThat(buyerData.getDiscount()).isEqualTo(30);
                    assertThat(buyerData.getReal_price()).isEqualTo(69986.0f);// 69986 = (365.4*3 + 672 * 0.7) * 30 + (122*10 + 122*20*0.7) + (672*10 + (672-5)*20)
                })
                .verifyComplete();

        buyer.deleteBuyer(mainGroup, "03030303030").block();

        buyer.GetAllDirect(SearchQuery.builder().group_id(mainGroup).page(0).size(500).build())
                .sort(Comparator.comparing(BuyerData::getDevice_id))
                .as(StepVerifier::create)
                .consumeNextWith(buyerData -> {
                    assertThat(buyerData.getReciver_fullname()).isEqualTo("phong nguyen");
                })
                .consumeNextWith(buyerData -> {
                    assertThat(buyerData.getPhone_number_clean()).isEqualTo("12345678909");
                    assertThat(buyerData.getDiscount()).isEqualTo(0);
                    assertThat(buyerData.getReal_price()).isEqualTo(10375);// 10375 = 122*5 + 117*25 + 106*30 + 122*30
                })
                .consumeNextWith(buyerData -> {
                    assertThat(buyerData.getPhone_number_clean()).isEqualTo("1234567890");
                    assertThat(buyerData.getDiscount()).isEqualTo(30);
                    assertThat(buyerData.getReal_price()).isEqualTo(69986.0f);// 69986 = (365.4*3 + 672 * 0.7) * 30 + (122*10 + 122*20*0.7) + (672*10 + (672-5)*20)
                })
                .verifyComplete();


        buyer.GetAll(SearchQuery.builder().filter(PackageOrder.Status.DONE.getName()).group_id(mainGroup).page(0).size(300).build())
                .as(StepVerifier::create)
                .consumeNextWith(buyerData -> {
                    assertThat(buyerData.getPhone_number_clean()).isEqualTo(orderDonePhone.get());
                    assertThat(buyerData.getReal_price()).isEqualTo(donePrice.get());
                })
                .verifyComplete();

        buyer.GetAll(SearchQuery.builder().filter(PackageOrder.Status.CANCEL.getName()).group_id(mainGroup).page(0).size(300).build())
                .as(StepVerifier::create)
                .consumeNextWith(buyerData -> {
                    assertThat(buyerData.getPhone_number_clean()).isEqualTo(orderCancelPhone.get());
                    assertThat(buyerData.getReal_price()).isEqualTo(cancelPrice.get());
                })
                .verifyComplete();

        buyer.GetAll(SearchQuery.builder().filter(PackageOrder.Status.PRE_ORDER.getName()).group_id(mainGroup).page(0).size(300).build())
                .as(StepVerifier::create)
                .verifyComplete();

        buyer.GetAll(SearchQuery.builder().filter(PackageOrder.Status.SENDING.getName()).group_id(mainGroup).page(0).size(300).build())
                .as(StepVerifier::create)
                .verifyComplete();

        statisticServices.getTotal(SearchQuery.builder().filter("30").query(PackageOrder.Status.DONE.getName()).page(0).size(300).group_id(mainGroup).build())
                .as(StepVerifier::create)
                .consumeNextWith(totalOrder -> {
                    assertThat(totalOrder.getReal_price()).isEqualTo(donePrice.get());
                    assertThat(totalOrder.getTotal_price()).isEqualTo(donePrice.get());
                })
                .verifyComplete();

        statisticServices.getByProductID(SearchQuery.builder().filter("30").query("beer_order1").page(0).size(30000).group_id(mainGroup).build())
                .reduce(0f, (total, product) -> {
                    return total + product.getTotal_price();
                })
                .as(StepVerifier::create)
                .consumeNextWith(totalOrder -> {
                    assertThat(totalOrder.intValue()).isEqualTo(28309);// 28309 = (106 + 122*3 +122*0.7*3)*30 + 122*5 + (122-5)*25 + 122*10 + 122*0.7*20
                })
                .verifyComplete();

        statisticServices.getAll(SearchQuery.builder().filter("30").page(0).size(3000).group_id(mainGroup).build())
                .reduce(0f, (total, product) -> {
                    return total + product.getTotal_price();
                })
                .as(StepVerifier::create)
                .consumeNextWith(totalOrder -> {
                    assertThat(Math.round(totalOrder)).isEqualTo(86509); //(106 + 122 + 365.4*3 + 672*2)*30 + 122*5 + 117*25 + 122*10 + 122*0.7*20
                })
                .verifyComplete();

        addBeerToPackage(mainGroup);

    }

    void orderTest(String mainGroup, AtomicReference<Float> voucher5K, AtomicReference<Float> voucher30Percent, AtomicReference<Float> packageVoucher5K) {

        AtomicReference<ProductUnit> beer_order1_Thung_10_10 = new AtomicReference<ProductUnit>();
        AtomicReference<ProductUnit> beer_order1_Lon_20_20 = new AtomicReference<ProductUnit>();

        AtomicReference<ProductUnit> beer_order2_Thung_50_50 = new AtomicReference<ProductUnit>();
        AtomicReference<ProductUnit> beer_order2_Lon_60_50 = new AtomicReference<ProductUnit>();

        AtomicReference<ProductUnit> beer_order_sold_out1_Thung_100_10_soldout = new AtomicReference<ProductUnit>();
        AtomicReference<ProductUnit> beer_order_sold_out1_Lon_110_0_soldout = new AtomicReference<ProductUnit>();

        AtomicReference<ProductUnit> beer_order_sold_out2_Thung_100_10_soldout = new AtomicReference<ProductUnit>();
        AtomicReference<ProductUnit> beer_order_sold_out2_Lon_110_0 = new AtomicReference<ProductUnit>();

        AtomicReference<ProductUnit> beer_order_hide2_Thung_100_10_hide = new AtomicReference<ProductUnit>();
        AtomicReference<ProductUnit> beer_order_hide2_Lon_110_0_hide = new AtomicReference<ProductUnit>();

        beerUnitRepository.findByBeerID(mainGroup, "beer_order1")
                .as(StepVerifier::create)
                .consumeNextWith(beer_order1_Thung_10_10::set)
                .consumeNextWith(beer_order1_Lon_20_20::set)
                .verifyComplete();

        beerUnitRepository.findByBeerID(mainGroup, "beer_order2")
                .as(StepVerifier::create)
                .consumeNextWith(beer_order2_Thung_50_50::set)
                .consumeNextWith(beer_order2_Lon_60_50::set)
                .verifyComplete();

        beerUnitRepository.findByBeerID(mainGroup, "beer_order_sold_out1")
                .as(StepVerifier::create)
                .consumeNextWith(beer_order_sold_out1_Thung_100_10_soldout::set)
                .consumeNextWith(beer_order_sold_out1_Lon_110_0_soldout::set)
                .verifyComplete();

        beerUnitRepository.findByBeerID(mainGroup, "beer_order_sold_out2")
                .as(StepVerifier::create)
                .consumeNextWith(beer_order_sold_out2_Thung_100_10_soldout::set)
                .consumeNextWith(beer_order_sold_out2_Lon_110_0::set)
                .verifyComplete();

        beerUnitRepository.findByBeerID(mainGroup, "beer_order_hide2")
                .as(StepVerifier::create)
                .consumeNextWith(beer_order_hide2_Thung_100_10_hide::set)
                .consumeNextWith(beer_order_hide2_Lon_110_0_hide::set)
                .verifyComplete();

        if (beer_order1_Lon_20_20.get().getName().equals("thung")) {
            ProductUnit temp = beer_order1_Lon_20_20.get();
            beer_order1_Lon_20_20.set(beer_order1_Thung_10_10.get());
            beer_order1_Thung_10_10.set(temp);
        }

        if (beer_order2_Lon_60_50.get().getName().equals("thung")) {
            ProductUnit temp = beer_order2_Lon_60_50.get();
            beer_order2_Lon_60_50.set(beer_order2_Thung_50_50.get());
            beer_order2_Thung_50_50.set(temp);
        }

        if (beer_order_sold_out1_Lon_110_0_soldout.get().getName().equals("thung")) {
            ProductUnit temp = beer_order_sold_out1_Lon_110_0_soldout.get();
            beer_order_sold_out1_Lon_110_0_soldout.set(beer_order_sold_out1_Thung_100_10_soldout.get());
            beer_order_sold_out1_Thung_100_10_soldout.set(temp);
        }

        if (beer_order_sold_out2_Lon_110_0.get().getName().equals("thung")) {
            ProductUnit temp = beer_order_sold_out2_Lon_110_0.get();
            beer_order_sold_out2_Lon_110_0.set(beer_order_sold_out2_Thung_100_10_soldout.get());
            beer_order_sold_out2_Thung_100_10_soldout.set(temp);
        }

        if (beer_order_hide2_Lon_110_0_hide.get().getName().equals("thung")) {
            ProductUnit temp = beer_order_hide2_Lon_110_0_hide.get();
            beer_order_hide2_Lon_110_0_hide.set(beer_order_hide2_Thung_100_10_hide.get());
            beer_order_hide2_Thung_100_10_hide.set(temp);
        }

        PackageOrderData packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .group_id(mainGroup)
                                .region_id(294)
                                .district_id(484)
                                .phone_number("12345678909")
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
                                        .productOrder(
                                                ProductOrder
                                                        .builder()
                                                        .product_second_id("beer_order1")
                                                        .build()
                                        )
                                        .productUnitOrders(
                                                new ProductUnitOrder[]{
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order1_Thung_10_10.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order1_Thung_10_10.get().getProduct_second_id())
                                                                .number_unit(10)
                                                                .build(),
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order1_Lon_20_20.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order1_Lon_20_20.get().getProduct_second_id())
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
                                .group_id(mainGroup)
                                .region_id(294)
                                .district_id(484)
                                .phone_number("12345678909")
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
                                        .productOrder(
                                                ProductOrder
                                                        .builder()
                                                        .product_second_id("beer_order1")
                                                        .voucher_second_id("ORDER_GIAM_5K")
                                                        .build()
                                        )
                                        .productUnitOrders(
                                                new ProductUnitOrder[]{
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order1_Thung_10_10.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order1_Thung_10_10.get().getProduct_second_id())
                                                                .number_unit(10)
                                                                .build(),
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order1_Lon_20_20.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order1_Lon_20_20.get().getProduct_second_id())
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
                    System.out.println("Using order ORDER_GIAM_5K: " + packageOrder.getReal_price());
                    voucher5K.updateAndGet(v -> v += packageOrder.getReal_price());
//                    assertThat(packageOrder.getTotal_price()).isEqualTo(117);// 10*10*0.9 + 20*2*0.8 - 5
                    assertThat(packageOrder.getShip_price()).isEqualTo(42000);// inside region and weight is 0!!
                })
                .verifyComplete();

        packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .group_id(mainGroup)
                                .region_id(294)
                                .district_id(484)
                                .phone_number("12345678909")
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
                                        .productOrder(
                                                ProductOrder
                                                        .builder()
                                                        .product_second_id("beer_order1")
                                                        .voucher_second_id("ORDER_GIAM_5Kk")
                                                        .build()
                                        )
                                        .productUnitOrders(
                                                new ProductUnitOrder[]{
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order1_Thung_10_10.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order1_Thung_10_10.get().getProduct_second_id())
                                                                .number_unit(10)
                                                                .build(),
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order1_Lon_20_20.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order1_Lon_20_20.get().getProduct_second_id())
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
                    assertThat(packageOrder.getReal_price()).isEqualTo(122);// 10*10*0.9 + 20*1*0.8 - 0
                    assertThat(packageOrder.getShip_price()).isEqualTo(42000);// inside region and weight is 0!!
                })
                .verifyComplete();

        packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .group_id(mainGroup)
                                .region_id(294)
                                .district_id(484)
                                .phone_number("1234567890")
                                .reciver_fullname("Pong Pong")
                                .reciver_address("232 bau cat, tan binh")
                                .user_device_id("iphone")
                                .build()
                )
                .preOrder(false)
                .beerOrders(
                        new PackageOrderData.BeerOrderData[]{
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .productOrder(
                                                ProductOrder
                                                        .builder()
                                                        .product_second_id("beer_order1")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .productUnitOrders(
                                                new ProductUnitOrder[]{
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order1_Thung_10_10.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order1_Thung_10_10.get().getProduct_second_id())
                                                                .number_unit(10)
                                                                .build(),
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order1_Lon_20_20.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order1_Lon_20_20.get().getProduct_second_id())
                                                                .number_unit(2)
                                                                .build()
                                                }
                                        )
                                        .build()
                                ,
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .productOrder(
                                                ProductOrder
                                                        .builder()
                                                        .product_second_id("beer_order2")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .productUnitOrders(
                                                new ProductUnitOrder[]{
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order2_Thung_50_50.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order2_Thung_50_50.get().getProduct_second_id())
                                                                .number_unit(10)
                                                                .build(),
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order2_Lon_60_50.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order2_Lon_60_50.get().getProduct_second_id())
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
                    System.out.println("Using order ORDER_GIAM_30%: " + packageOrder.getReal_price());
                    assertThat(packageOrder.getTotal_price()).isEqualTo(365.4f);// 10*10*0.9 + 20*1*0.8
                    assertThat(packageOrder.getShip_price()).isEqualTo(42000);// inside region and weight is 0!!
                })
                .verifyComplete();

        packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .group_id(mainGroup)
                                .region_id(294)
                                .district_id(484)
                                .phone_number("1234567890")
                                .reciver_fullname("Pong Pong")
                                .reciver_address("232 bau cat, tan binh")
                                .user_device_id("iphone")
                                .build()
                )
                .preOrder(false)
                .beerOrders(
                        new PackageOrderData.BeerOrderData[]{
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .productOrder(
                                                ProductOrder
                                                        .builder()
                                                        .product_second_id("beer_order1")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .productUnitOrders(
                                                new ProductUnitOrder[]{
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order1_Thung_10_10.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order1_Thung_10_10.get().getProduct_second_id())
                                                                .number_unit(10)
                                                                .build(),
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order1_Lon_20_20.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order1_Lon_20_20.get().getProduct_second_id())
                                                                .number_unit(2)
                                                                .build()
                                                }
                                        )
                                        .build()
                                ,
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .productOrder(
                                                ProductOrder
                                                        .builder()
                                                        .product_second_id("beer_order2")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .productUnitOrders(
                                                new ProductUnitOrder[]{
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order2_Thung_50_50.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order2_Thung_50_50.get().getProduct_second_id())
                                                                .number_unit(10)
                                                                .build(),
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order2_Lon_60_50.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order2_Lon_60_50.get().getProduct_second_id())
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
                    System.out.println("Using again 2 order ORDER_GIAM_30%: " + packageOrder.getReal_price());
                    assertThat(packageOrder.getTotal_price()).isEqualTo(365.4f);// 10*10*0.9 + 20*1*0.8
                    assertThat(packageOrder.getShip_price()).isEqualTo(42000);// inside region and weight is 0!!
                })
                .verifyComplete();


        packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .group_id(mainGroup)
                                .region_id(294)
                                .district_id(484)
                                .phone_number("1234567890")
                                .reciver_fullname("Pong Pong")
                                .reciver_address("232 bau cat, tan binh")
                                .user_device_id("iphone")
                                .build()
                )
                .preOrder(false)
                .beerOrders(
                        new PackageOrderData.BeerOrderData[]{
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .productOrder(
                                                ProductOrder
                                                        .builder()
                                                        .product_second_id("beer_order1")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .productUnitOrders(
                                                new ProductUnitOrder[]{
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order1_Thung_10_10.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order1_Thung_10_10.get().getProduct_second_id())
                                                                .number_unit(10)
                                                                .build(),
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order1_Lon_20_20.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order1_Lon_20_20.get().getProduct_second_id())
                                                                .number_unit(2)
                                                                .build()
                                                }
                                        )
                                        .build()
                                ,
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .productOrder(
                                                ProductOrder
                                                        .builder()
                                                        .product_second_id("beer_order2")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .productUnitOrders(
                                                new ProductUnitOrder[]{
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order2_Thung_50_50.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order2_Thung_50_50.get().getProduct_second_id())
                                                                .number_unit(10)
                                                                .build(),
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order2_Lon_60_50.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order2_Lon_60_50.get().getProduct_second_id())
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
                    System.out.println("Using again 3 order ORDER_GIAM_30%: " + packageOrder.getReal_price());
                    assertThat(packageOrder.getTotal_price()).isEqualTo(365.4f);// 10*10*0.9 + 20*1*0.8
                    assertThat(packageOrder.getShip_price()).isEqualTo(42000);// inside region and weight is 0!!
                })
                .verifyComplete();


        packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .group_id(mainGroup)
                                .region_id(294)
                                .district_id(484)
                                .phone_number("1234567890")
                                .reciver_fullname("Pong Pong")
                                .reciver_address("232 bau cat, tan binh")
                                .user_device_id("iphone")
                                .voucher_second_id("ORDER_GIAM_30%_wrong")
                                .build()
                )
                .preOrder(false)
                .beerOrders(
                        new PackageOrderData.BeerOrderData[]{
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .productOrder(
                                                ProductOrder
                                                        .builder()
                                                        .product_second_id("beer_order1")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .productUnitOrders(
                                                new ProductUnitOrder[]{
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order1_Thung_10_10.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order1_Thung_10_10.get().getProduct_second_id())
                                                                .number_unit(10)
                                                                .build(),
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order1_Lon_20_20.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order1_Lon_20_20.get().getProduct_second_id())
                                                                .number_unit(2)
                                                                .build()
                                                }
                                        )
                                        .build()
                                ,
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .productOrder(
                                                ProductOrder
                                                        .builder()
                                                        .product_second_id("beer_order_sold_out1")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .productUnitOrders(
                                                new ProductUnitOrder[]{
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order_sold_out1_Thung_100_10_soldout.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order_sold_out1_Thung_100_10_soldout.get().getProduct_second_id())
                                                                .number_unit(10)
                                                                .build(),
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order_sold_out1_Lon_110_0_soldout.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order_sold_out1_Lon_110_0_soldout.get().getProduct_second_id())
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
                    System.out.println("Using again 4 order ORDER_GIAM_30%: " + packageOrder.getReal_price());
                    voucher30Percent.updateAndGet(v -> v += packageOrder.getReal_price());
                    //assertThat(packageOrder.getTotal_price()).isEqualTo(522f);// 10*10*0.9 + 20*1*0.8
                    assertThat(packageOrder.getShip_price()).isEqualTo(42000);// inside region and weight is 0!!
                })
                .verifyComplete();


        packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .group_id(mainGroup)
                                .region_id(294)
                                .district_id(484)
                                .phone_number("1234567890")
                                .reciver_fullname("Pong Pong")
                                .reciver_address("232 bau cat, tan binh")
                                .user_device_id("iphone")
                                .voucher_second_id("PACKAGE_VOUCHER_5K")
                                .build()
                )
                .preOrder(false)
                .beerOrders(
                        new PackageOrderData.BeerOrderData[]{
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .productOrder(
                                                ProductOrder
                                                        .builder()
                                                        .product_second_id("beer_order1")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .productUnitOrders(
                                                new ProductUnitOrder[]{
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order1_Thung_10_10.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order1_Thung_10_10.get().getProduct_second_id())
                                                                .number_unit(10)
                                                                .build(),
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order1_Lon_20_20.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order1_Lon_20_20.get().getProduct_second_id())
                                                                .number_unit(2)
                                                                .build()
                                                }
                                        )
                                        .build()
                                ,
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .productOrder(
                                                ProductOrder
                                                        .builder()
                                                        .product_second_id("beer_order_sold_out2")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .productUnitOrders(
                                                new ProductUnitOrder[]{
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order_sold_out2_Thung_100_10_soldout.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order_sold_out2_Thung_100_10_soldout.get().getProduct_second_id())
                                                                .number_unit(10)
                                                                .build(),
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order_sold_out2_Lon_110_0.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order_sold_out2_Lon_110_0.get().getProduct_second_id())
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
                    System.out.println("Using again 5 order ORDER_GIAM_30% and PACKAGE_VOUCHER_5K: " + packageOrder.getReal_price());
                    packageVoucher5K.updateAndGet(v -> v + packageOrder.getReal_price());
//                    assertThat(packageOrder.getReal_price()).isEqualTo(672f - 5);// 5*110 + (20*2*0.8 + 10*10*0.9) - 5
                    assertThat(packageOrder.getShip_price()).isEqualTo(42000);// inside region and weight is 0!!
                })
                .verifyComplete();


        packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .group_id(mainGroup)
                                .region_id(294)
                                .district_id(484)
                                .phone_number("1234567890")
                                .reciver_fullname("Pong Pong")
                                .reciver_address("232 bau cat, tan binh")
                                .user_device_id("iphone")
                                .voucher_second_id("PACKAGE_VOUCHER_30%")
                                .discount(0.5f)
                                .build()
                )
                .preOrder(false)
                .beerOrders(
                        new PackageOrderData.BeerOrderData[]{
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .productOrder(
                                                ProductOrder
                                                        .builder()
                                                        .product_second_id("beer_order1")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .productUnitOrders(
                                                new ProductUnitOrder[]{
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order1_Thung_10_10.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order1_Thung_10_10.get().getProduct_second_id())
                                                                .number_unit(10)
                                                                .build(),
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order1_Lon_20_20.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order1_Lon_20_20.get().getProduct_second_id())
                                                                .number_unit(2)
                                                                .build()
                                                }
                                        )
                                        .build()
                                ,
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .productOrder(
                                                ProductOrder
                                                        .builder()
                                                        .product_second_id("beer_order_sold_out2")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .productUnitOrders(
                                                new ProductUnitOrder[]{
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order_sold_out2_Thung_100_10_soldout.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order_sold_out2_Thung_100_10_soldout.get().getProduct_second_id())
                                                                .number_unit(10)
                                                                .build(),
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order_sold_out2_Lon_110_0.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order_sold_out2_Lon_110_0.get().getProduct_second_id())
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
                    System.out.println("Using again 5 order ORDER_GIAM_30% and PACKAGE_VOUCHER_30%: " + packageOrder.getReal_price());
                    assertThat(packageOrder.getReal_price()).isEqualTo(672f * 0.7f);// [5*110 + (20*2*0.8 + 10*10*0.9)] * 0.7
                    assertThat(packageOrder.getShip_price()).isEqualTo(42000);// inside region and weight is 0!!
                })
                .verifyComplete();


        packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .group_id(mainGroup)
                                .region_id(294)
                                .district_id(484)
                                .phone_number("1234567890")
                                .reciver_fullname("Pong Pong")
                                .reciver_address("232 bau cat, tan binh")
                                .user_device_id("iphone")
                                .voucher_second_id("ORDER_GIAM_30%")
                                .discount(0.5f)
                                .build()
                )
                .preOrder(false)
                .beerOrders(
                        new PackageOrderData.BeerOrderData[]{
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .productOrder(
                                                ProductOrder
                                                        .builder()
                                                        .product_second_id("beer_order_hide2")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .productUnitOrders(
                                                new ProductUnitOrder[]{
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order_hide2_Thung_100_10_hide.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order_hide2_Thung_100_10_hide.get().getProduct_second_id())
                                                                .number_unit(10)
                                                                .build(),
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beer_order_hide2_Lon_110_0_hide.get().getProduct_unit_second_id())
                                                                .product_second_id(beer_order_hide2_Lon_110_0_hide.get().getProduct_second_id())
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
                    System.out.println("Using again 6 order ORDER_GIAM_30%: " + packageOrder.getReal_price());
                    assertThat(packageOrder.getReal_price()).isEqualTo(0f);// 10*110*0.7 + (20*10*0.8 + 2*10*0.9)*0.7
                    //assertThat(packageOrder.getShip_price()).isEqualTo(42000);// inside region and weight is 0!!
                })
                .verifyComplete();

    }

    void createVoucher(String mainGroup) {

        voucherAPI.createVoucher(
                        VoucherData.builder()
                                .group_id(mainGroup)
                                .for_all_product(true)
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
                                .group_id(mainGroup)
                                .for_all_product(true)
                                .voucher_second_id("ORDER_GIAM_30%")
                                .discount(30)
                                .reuse(200)
                                .detail("Giảm 30% trên toàn bộ sản phẩm, chỉ áp dụng cho ai nhận được.")
                                .listUser(new String[]{"iphone"})
                                .build())
                .block();

        voucherAPI.createVoucher(
                        VoucherData.builder()
                                .group_id(mainGroup)
                                .voucher_second_id("ORDER_GIAM_50k")
                                .detail("Giảm 50k trên 1 loại sản phẩm. Chỉ áp dụng cho ai nhận được.")
                                .amount(50)
                                .reuse(2)
                                .listBeer(new String[]{"beer_order1", "beer_order2"})
                                .listUser(new String[]{"android", "iphone"})
                                .build())
                .block();

        voucherAPI.createVoucher(
                        VoucherData.builder()
                                .group_id(mainGroup)
                                .voucher_second_id("PACKAGE_VOUCHER_5K")
                                .detail("Giảm 5k trên toàn bộ bill")
                                .amount(5)
                                .reuse(20)
                                .package_voucher(true)
                                .listBeer(new String[]{"beer_order1", "beer_order2"})
                                .listUser(new String[]{"android", "iphone"})
                                .build())
                .block();

        voucherAPI.createVoucher(
                        VoucherData.builder()
                                .group_id(mainGroup)
                                .voucher_second_id("PACKAGE_VOUCHER_30%")
                                .detail("Giảm 30% trên toàn bộ bill")
                                .discount(30)
                                .reuse(45)
                                .package_voucher(true)
                                .listBeer(new String[]{"beer_order1", "beer_order2"})
                                .listUser(new String[]{"android", "iphone"})
                                .for_all_user(true)
                                .build())
                .block();

        voucherAPI.createVoucher(
                        VoucherData.builder()
                                .group_id(mainGroup)
                                .voucher_second_id("VOUCHER_DELETE")
                                .detail("Giảm 30% trên toàn bộ bill")
                                .discount(30)
                                .reuse(45)
                                .package_voucher(true)
                                .listBeer(new String[]{"beer_order1", "beer_order2"})
                                .listUser(new String[]{"android", "iphone"})
                                .for_all_user(true)
                                .build()
                )
                .block();

        voucherAPI.getVoucherByID(mainGroup, "VOUCHER_DELETE")
                .as(StepVerifier::create)
                .consumeNextWith(voucher -> {
                    assertThat(voucher.isFor_all_user()).isEqualTo(true);
                    assertThat(voucher.getReuse()).isEqualTo(45);
                })
                .verifyComplete();
        voucherAPI.deleteByID(
                        VoucherData.builder()
                                .group_id(mainGroup)
                                .voucher_second_id("VOUCHER_DELETE")
                                .detail("Giảm 30% trên toàn bộ bill")
                                .discount(30)
                                .reuse(45)
                                .package_voucher(true)
                                .listBeer(new String[]{"beer_order1", "beer_order2"})
                                .listUser(new String[]{"android", "iphone"})
                                .for_all_user(true)
                                .build()
                )
                .block();

        voucherAPI.getVoucherByID(mainGroup, "VOUCHER_DELETE")
                .as(StepVerifier::create)
                .verifyComplete();
    }

    void createBeer(String mainGroup) {

        beerAPI.CreateBeer(
                        BeerInfo
                                .builder()
                                .productUnit(new ProductUnit[]{
                                        com.example.heroku.model.ProductUnit
                                                .builder()
                                                .product_second_id("beer_order1")
                                                .buy_price(7)
                                                .price(10)
                                                .weight(0.3f)
                                                .discount(10)
                                                .date_expire(new Timestamp(new Date().getTime()))
                                                .name("thung")
                                                .build(),
                                        com.example.heroku.model.ProductUnit
                                                .builder()
                                                .product_second_id("beer_order1")
                                                .buy_price(12)
                                                .price(20)
                                                .weight(1)
                                                .discount(20)
                                                .date_expire(new Timestamp(new Date().getTime()))
                                                .name("lon")
                                                .build()
                                })
                                .product(Product
                                        .builder()
                                        .group_id(mainGroup)
                                        .category(Category.CRAB.getName())
                                        .name("beer tigerrrrr")
                                        .product_second_id("beer_order1")
                                        .detail("bia for order 1")
                                        .build()
                                        .AutoFill()
                                )
                                .build()
                )
                .block();

        beerAPI.CreateBeer(
                        BeerInfo
                                .builder()
                                .productUnit(new ProductUnit[]{
                                        com.example.heroku.model.ProductUnit
                                                .builder()
                                                .product_second_id("beer_order2")
                                                .price(50)
                                                .discount(50)
                                                .date_expire(new Timestamp(new Date().getTime()))
                                                .name("thung")
                                                .build(),
                                        com.example.heroku.model.ProductUnit
                                                .builder()
                                                .product_second_id("beer_order2")
                                                .price(60)
                                                .discount(50)
                                                .date_expire(new Timestamp(new Date().getTime()))
                                                .name("lon")
                                                .build()
                                })
                                .product(Product
                                        .builder()
                                        .group_id(mainGroup)
                                        .category(Category.CRAB.getName())
                                        .name("beer tigerrrrr")
                                        .product_second_id("beer_order2")
                                        .detail("bia for order 2")
                                        .build()
                                        .AutoFill()
                                )
                                .build()
                )
                .block();

        beerAPI.CreateBeer(
                        BeerInfo
                                .builder()
                                .productUnit(new ProductUnit[]{
                                        com.example.heroku.model.ProductUnit
                                                .builder()
                                                .product_second_id("beer_order3")
                                                .price(100)
                                                .discount(10)
                                                .date_expire(new Timestamp(new Date().getTime()))
                                                .name("thung")
                                                .build(),
                                        com.example.heroku.model.ProductUnit
                                                .builder()
                                                .product_second_id("beer_order3")
                                                .price(110)
                                                .name("lon")
                                                .build()
                                })
                                .product(Product
                                        .builder()
                                        .group_id(mainGroup)
                                        .category(Category.CRAB.getName())
                                        .name("beer tigerrrrr")
                                        .product_second_id("beer_order3")
                                        .detail("bia for order 3")
                                        .build()
                                        .AutoFill()
                                )
                                .build()
                )
                .block();

        beerAPI.CreateBeer(
                        BeerInfo
                                .builder()
                                .productUnit(new ProductUnit[]{
                                        com.example.heroku.model.ProductUnit
                                                .builder()
                                                .product_second_id("beer_order_sold_out1")
                                                .price(100)
                                                .discount(10)
                                                .date_expire(new Timestamp(new Date().getTime()))
                                                .name("thung")
                                                .build(),
                                        com.example.heroku.model.ProductUnit
                                                .builder()
                                                .product_second_id("beer_order_sold_out1")
                                                .price(110)
                                                .name("lon")
                                                .build()
                                })
                                .product(Product
                                        .builder()
                                        .group_id(mainGroup)
                                        .category(Category.CRAB.getName())
                                        .name("sold out 1")
                                        .product_second_id("beer_order_sold_out1")
                                        .detail("sold out 1")
                                        .status(Product.Status.SOLD_OUT)
                                        .build()
                                        .AutoFill()
                                )
                                .build()
                )
                .block();

        beerAPI.CreateBeer(
                        BeerInfo
                                .builder()
                                .productUnit(new ProductUnit[]{
                                        com.example.heroku.model.ProductUnit
                                                .builder()
                                                .product_second_id("beer_order_sold_out2")
                                                .price(100)
                                                .discount(10)
                                                .date_expire(new Timestamp(new Date().getTime()))
                                                .name("thung")
                                                .status(com.example.heroku.model.ProductUnit.Status.SOLD_OUT)
                                                .build(),
                                        com.example.heroku.model.ProductUnit
                                                .builder()
                                                .product_second_id("beer_order_sold_out2")
                                                .price(110)
                                                .name("lon")
                                                .build()
                                })
                                .product(Product
                                        .builder()
                                        .group_id(mainGroup)
                                        .category(Category.CRAB.getName())
                                        .name("sold out 2")
                                        .product_second_id("beer_order_sold_out2")
                                        .detail("sold out 2")
                                        .build()
                                        .AutoFill()
                                )
                                .build()
                )
                .block();

        beerAPI.CreateBeer(
                        BeerInfo
                                .builder()
                                .productUnit(new ProductUnit[]{
                                        com.example.heroku.model.ProductUnit
                                                .builder()
                                                .product_second_id("beer_order_hide2")
                                                .price(100)
                                                .discount(10)
                                                .date_expire(new Timestamp(new Date().getTime()))
                                                .name("thung")
                                                .build(),
                                        com.example.heroku.model.ProductUnit
                                                .builder()
                                                .product_second_id("beer_order_hide2")
                                                .price(110)
                                                .name("lon")
                                                .build()
                                })
                                .product(Product
                                        .builder()
                                        .group_id(mainGroup)
                                        .category(Category.CRAB.getName())
                                        .name("sold out 2")
                                        .product_second_id("beer_order_hide2")
                                        .status(Product.Status.HIDE)
                                        .detail("sold out 2")
                                        .build()
                                        .AutoFill()
                                )
                                .build()
                )
                .block();
    }

    void createDevice(String mainGroup) {
        userDeviceAPI.CreateUserDevice(com.example.heroku.model.UserDevice.builder().device_id("android").user_first_name("Nguyen").user_last_name("phong").group_id(mainGroup).build())
                .block();
        userDeviceAPI.CreateUserDevice(com.example.heroku.model.UserDevice.builder().device_id("iphone").user_first_name("Ho DUong").user_last_name("Vuong").group_id(mainGroup).build())
                .block();
        userDeviceAPI.CreateUserDevice(com.example.heroku.model.UserDevice.builder().device_id("order_test").user_first_name("Ho DUong").user_last_name("Vuong").group_id(mainGroup).build())
                .block();

    }

    void addBeerToPackage(String mainGroup) {

        AtomicReference<String> beerUnit4561ID = new AtomicReference<String>();
        AtomicReference<String> beerUnit4562ID = new AtomicReference<String>();
        this.beerAPI.GetBeerByID(mainGroup, "beer_order1")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getProduct().getProduct_second_id()).isEqualTo("beer_order1");
                    assertThat(beerInfo.getProduct().getCategory()).isEqualTo(Category.CRAB.getName());
                    assertThat(beerInfo.getProductUnit().length).isEqualTo(2);
                    Flux.just(beerInfo.getProductUnit())
                            .sort(Comparator.comparing(com.example.heroku.model.ProductUnit::getName))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    beerUnit4561ID.set(beerUnit.getProduct_unit_second_id());
                                    assertThat(beerUnit.getBuy_price()).isEqualTo(12);
                                } else {
                                    beerUnit4562ID.set(beerUnit.getProduct_unit_second_id());
                                    assertThat(beerUnit.getBuy_price()).isEqualTo(7);
                                }
                            })
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    beerUnit4561ID.set(beerUnit.getProduct_unit_second_id());
                                    assertThat(beerUnit.getBuy_price()).isEqualTo(12);
                                } else {
                                    beerUnit4562ID.set(beerUnit.getProduct_unit_second_id());
                                    assertThat(beerUnit.getBuy_price()).isEqualTo(7);
                                }
                            })
                            .verifyComplete();
                })
                .verifyComplete();
        ProductPackage productPackage = ProductPackage.builder()
                .group_id(mainGroup)
                .device_id("order_test")
                .product_units(new com.example.heroku.model.UserPackage[]{
                        com.example.heroku.model.UserPackage.builder()
                                .product_unit_second_id(beerUnit4561ID.get())
                                .product_second_id("beer_order1")
                                .number_unit(100)
                                .build(),
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("beer_order1")
                                .product_unit_second_id(beerUnit4562ID.get())
                                .number_unit(9)
                                .build()
                })
                .build();
        userPackageAPI.AddProductToPackage(productPackage)
                .block();


        PackageOrderData packageOrderData = PackageOrderData
                .builder()
                .packageOrder(
                        PackageOrder
                                .builder()
                                .group_id(mainGroup)
                                .region_id(294)
                                .district_id(484)
                                .phone_number("1234567890")
                                .reciver_fullname("Pong Pong")
                                .reciver_address("232 bau cat, tan binh")
                                .user_device_id("order_test")
                                .build()
                )
                .preOrder(false)
                .beerOrders(
                        new PackageOrderData.BeerOrderData[]{
                                PackageOrderData.BeerOrderData
                                        .builder()
                                        .productOrder(
                                                ProductOrder
                                                        .builder()
                                                        .product_second_id("beer_order1")
                                                        .voucher_second_id("ORDER_GIAM_30%")
                                                        .build()
                                        )
                                        .productUnitOrders(
                                                new ProductUnitOrder[]{
                                                        ProductUnitOrder
                                                                .builder()
                                                                .product_unit_second_id(beerUnit4561ID.get())
                                                                .product_second_id("beer_order1")
                                                                .number_unit(10)
                                                                .build(),
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
                    assertThat(packageOrder.getReal_price()).isEqualTo(160.0f);// 10*110*0.7 + (20*10*0.8 + 2*10*0.9)*0.7
                    //assertThat(packageOrder.getShip_price()).isEqualTo(42000);// inside region and weight is 0!!
                })
                .verifyComplete();

        userPackageAPI.GetMyPackage(UserID.builder().id("order_test").page(0).size(1000).group_id(mainGroup).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice).reversed())
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(userPackage.getDevice_id()).isEqualTo("order_test");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(1);
                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("beer_order1");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit4562ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(9);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("beer_order1");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();
    }

    void createProvider(String mainGroup) throws Exception {

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
        shippingProvider.CreateShipProvider(ShippingProviderData.builder().id(ShippingProvider.GHN.ID).json(json).group_id(mainGroup).build())
                .block();
    }
}
