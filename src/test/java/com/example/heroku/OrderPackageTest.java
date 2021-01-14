package com.example.heroku;

import com.example.heroku.model.Beer;
import com.example.heroku.model.BeerUnit;
import com.example.heroku.model.BeerUnitOrder;
import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.repository.BeerUnitRepository;
import com.example.heroku.request.beer.BeerInfo;
import com.example.heroku.request.beer.PackageOrderData;
import com.example.heroku.request.voucher.VoucherData;
import com.example.heroku.services.BeerOrder;
import com.example.heroku.services.ShippingProvider;
import com.example.heroku.services.UserDevice;
import com.example.heroku.services.Voucher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {
        "JDBC_DATABASE_URL=r2dbc:postgresql://ec2-52-203-165-126.compute-1.amazonaws.com:5432/dab63e0a6snl77",
        "POSTGRESQL_PORT=5432",
        "POSTGRESQL_DB=dab63e0a6snl77",
        "POSTGRESQL_HOST=ec2-52-203-165-126.compute-1.amazonaws.com",
        "USER_NAME=pnkrlyomnugskb",
        "PASSWORD=4b16a191f30c89f36908193809d12d3a8d4c3ffb73277357e1b0e0515169cea2",
        "POSTGRESQL_POOLSIZE=5",
        "POSTGRESQL_TIMEIDLE=10000",
        "HTTPONLY_SECURE=false"})
public class OrderPackageTest {

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

    @Test
    public void OrderTest() throws Exception {
        createDevice();
        createProvider();
        createBeer();
        createVoucher();

        Thread[] threads = new Thread[30];

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
        //orderTest();
    }

    void orderTest() {

        AtomicReference<BeerUnit> beerUnit1= new AtomicReference<BeerUnit>();
        AtomicReference<BeerUnit> beerUnit2 = new AtomicReference<BeerUnit>();

        AtomicReference<BeerUnit> beerUnit3= new AtomicReference<BeerUnit>();
        AtomicReference<BeerUnit> beerUnit4 = new AtomicReference<BeerUnit>();

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
                    assertThat(packageOrder.getTotal_price()).isEqualTo(106);// 10*10*0.9 + 20*1*0.8
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
                    assertThat(packageOrder.getTotal_price()).isEqualTo(122);// 10*10*0.9 + 20*1*0.8
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
    }

    void createVoucher(){

        voucherAPI.createVoucher(
                VoucherData.builder()
                        .voucher(
                                com.example.heroku.model.Voucher
                                        .builder()
                                        .for_all_beer(true)
                                        .for_all_user(true)
                                        .voucher_second_id("ORDER_GIAM_5K")
                                        .detail("Giảm 5k trên toàn bộ sản phẩm")
                                        .amount(5)
                                        .reuse(25)
                                        .build()
                        )
                        .build())
                .block();

        voucherAPI.createVoucher(
                VoucherData.builder()
                        .voucher(
                                com.example.heroku.model.Voucher
                                        .builder()
                                        .for_all_beer(true)
                                        .voucher_second_id("ORDER_GIAM_30%")
                                        .discount(30)
                                        .reuse(200)
                                        .detail("Giảm 30% trên toàn bộ sản phẩm, chỉ áp dụng cho ai nhận được.")
                                        .build()
                        )
                        .listUser(new String[]{"iphone"})
                        .build())
                .block();

        voucherAPI.createVoucher(
                VoucherData.builder()
                        .voucher(
                                com.example.heroku.model.Voucher
                                        .builder()
                                        .voucher_second_id("ORDER_GIAM_50k")
                                        .detail("Giảm 50k trên 1 loại sản phẩm. Chỉ áp dụng cho ai nhận được.")
                                        .amount(50)
                                        .reuse(2)
                                        .build()
                        )
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
                                .category(Beer.Category.ALCOHOL)
                                .name("beer tiger")
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
                                .category(Beer.Category.ALCOHOL)
                                .name("beer tiger")
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
                                .category(Beer.Category.ALCOHOL)
                                .name("beer tiger")
                                .beer_second_id("beer_order3")
                                .detail("bia for order 3")
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
