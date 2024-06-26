package com.example.heroku;

import com.example.heroku.model.Buyer;
import com.example.heroku.model.PaymentTransation;
import com.example.heroku.model.ProductUnit;
import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.statistics.*;
import com.example.heroku.request.beer.*;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.request.client.UserID;
import com.example.heroku.request.client.UserPackageID;
import com.example.heroku.response.BuyerData;
import com.example.heroku.response.PackageDataResponse;
import com.example.heroku.response.ProductInPackageResponse;
import com.example.heroku.services.StatisticServices;
import com.example.heroku.services.UserPackage;
import com.example.heroku.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.assertj.core.api.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class UserPackageTest {
    @Autowired
    UserPackage userPackageAPI;

    @Autowired
    StatisticServices statisticServices;

    @Autowired
    com.example.heroku.services.Buyer buyer;

    com.example.heroku.services.Beer beerAPI;

    String group;

    public void TestUserPackage() {
        ProductPackage productPackage = ProductPackage.builder().build();
        userPackageAPI.AddProductToPackage(productPackage)
                .block();


        AtomicReference<String> beerUnitsold_out1ID = new AtomicReference<String>();
        AtomicReference<String> beerUnitsold_out2ID = new AtomicReference<String>();
        this.beerAPI.GetBeerByID(group, "sold_out")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getProduct().getProduct_second_id()).isEqualTo("sold_out");
                    assertThat(beerInfo.getProduct().getCategory()).isEqualTo(Category.CRAB.getName());
                    assertThat(beerInfo.getProductUnit().length).isEqualTo(2);
                    Flux.just(beerInfo.getProductUnit())
                            .sort(Comparator.comparing(com.example.heroku.model.ProductUnit::getName))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    beerUnitsold_out1ID.set(beerUnit.getProduct_unit_second_id());
                                } else {
                                    beerUnitsold_out2ID.set(beerUnit.getProduct_unit_second_id());
                                }
                            })
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    beerUnitsold_out1ID.set(beerUnit.getProduct_unit_second_id());
                                } else {
                                    beerUnitsold_out2ID.set(beerUnit.getProduct_unit_second_id());
                                }
                            })
                            .verifyComplete();
                })
                .verifyComplete();


        AtomicReference<String> beerUnit1ID = new AtomicReference<String>();
        AtomicReference<String> beerUnit2ID = new AtomicReference<String>();
        this.beerAPI.GetBeerByID(group, "123")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getProduct().getProduct_second_id()).isEqualTo("123");
                    assertThat(beerInfo.getProduct().getCategory()).isEqualTo(Category.CRAB.getName());
                    assertThat(beerInfo.getProductUnit().length).isEqualTo(2);
                    Flux.just(beerInfo.getProductUnit())
                            .sort(Comparator.comparing(com.example.heroku.model.ProductUnit::getName))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    beerUnit1ID.set(beerUnit.getProduct_unit_second_id());
                                } else {
                                    beerUnit2ID.set(beerUnit.getProduct_unit_second_id());
                                }
                            })
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    beerUnit1ID.set(beerUnit.getProduct_unit_second_id());
                                } else {
                                    beerUnit2ID.set(beerUnit.getProduct_unit_second_id());
                                }
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        AtomicReference<String> beerUnit4561ID = new AtomicReference<String>();
        AtomicReference<String> beerUnit4562ID = new AtomicReference<String>();
        this.beerAPI.GetBeerByID(group, "456")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getProduct().getProduct_second_id()).isEqualTo("456");
                    assertThat(beerInfo.getProduct().getCategory()).isEqualTo(Category.CRAB.getName());
                    assertThat(beerInfo.getProductUnit().length).isEqualTo(2);
                    Flux.just(beerInfo.getProductUnit())
                            .sort(Comparator.comparing(com.example.heroku.model.ProductUnit::getName))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    beerUnit4561ID.set(beerUnit.getProduct_unit_second_id());
                                } else {
                                    beerUnit4562ID.set(beerUnit.getProduct_unit_second_id());
                                }
                            })
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    beerUnit4561ID.set(beerUnit.getProduct_unit_second_id());
                                } else {
                                    beerUnit4562ID.set(beerUnit.getProduct_unit_second_id());
                                }
                            })
                            .verifyComplete();
                })
                .verifyComplete();


        productPackage = ProductPackage.builder()
                .group_id(group)
                .device_id("soldoutttt")
                .package_second_id("package_iddddd")
                .note("note here!!")
                .image("img.jpeg")
                .price(1)
                .discount_amount(10000)
                .discount_percent(10)
                .ship_price(20000)
                .voucher("voucher_1d")
                .package_type("deliver")
                .progress("{}")
                .meta_search("hello meta search")
                .status(UserPackageDetail.Status.CREATE)
                .createat(Util.getInstance().Now())
                .product_units(new com.example.heroku.model.UserPackage[]{
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("sold_out")
                                .product_unit_second_id(beerUnitsold_out1ID.get())
                                .number_unit(100)
                                .build(),
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("sold_out")
                                .product_unit_second_id(beerUnitsold_out2ID.get())
                                .number_unit(9)
                                .build()
                })
                .build();
        userPackageAPI.AddProductToPackage(productPackage)
                .block();


        productPackage = ProductPackage.builder()
                .group_id(group)
                .device_id("soldoutttt")
                .package_second_id("package_idddddtttt")
                .note("note here!!")
                .image("img.jpeg")
                .price(2)
                .discount_amount(10000)
                .discount_percent(10)
                .ship_price(20000)
                .voucher("voucher_1d")
                .package_type("deliver")
                .progress("{}")
                .status(UserPackageDetail.Status.CREATE)
                .createat(Util.getInstance().Now())
                .product_units(new com.example.heroku.model.UserPackage[]{
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit1ID.get())
                                .number_unit(100)
                                .build(),
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit2ID.get())
                                .number_unit(9)
                                .build()
                })
                .build();
        userPackageAPI.AddProductToPackage(productPackage)
                .block();


        productPackage = ProductPackage.builder()
                .group_id(group)
                .device_id("soldoutttt")
                .package_second_id("package_idddddtttt")
                .note("note here!!")
                .image("img.jpeg")
                .price(3)
                .discount_amount(10000)
                .discount_percent(10)
                .ship_price(20000)
                .voucher("voucher_1d")
                .package_type("deliver")
                .progress("{}")
                .meta_search("hello meta search")
                .status(UserPackageDetail.Status.CREATE)
                .product_units(new com.example.heroku.model.UserPackage[]{
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit1ID.get())
                                .number_unit(11)
                                .build(),
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit1ID.get())
                                .number_unit(-5)
                                .build(),
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit2ID.get())
                                .number_unit(3)
                                .build()
                })
                .build();
        userPackageAPI.AddProductToPackage(productPackage)
                .block();


        productPackage = ProductPackage.builder()
                .group_id(group)
                .price(1)
                .device_id("222222")
                .status(UserPackageDetail.Status.CREATE)
                .product_units(new com.example.heroku.model.UserPackage[]{
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit1ID.get())
                                .number_unit(100)
                                .build(),
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit2ID.get())
                                .number_unit(9)
                                .build()
                })
                .build();
        userPackageAPI.AddProductToPackage(productPackage)
                .block();

        productPackage = ProductPackage.builder()
                .group_id(group)
                .price(2)
                .device_id("222222")
                .status(UserPackageDetail.Status.CREATE)
                .product_units(new com.example.heroku.model.UserPackage[]{
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("456")
                                .product_unit_second_id("3")
                                .number_unit(100)
                                .build(),
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("456")
                                .product_unit_second_id("3")
                                .number_unit(9)
                                .build()
                })
                .build();
        userPackageAPI.AddProductToPackage(productPackage)
                .block();

        userPackageAPI.GetMyPackageOfStatus(UserID.builder().id("222222").page(0).size(1000).group_id(group).build(), UserPackageDetail.Status.CREATE)
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice))
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();

                    assertThat(listItem.size()).isEqualTo(2);
                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(100);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);

                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(9);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    assertThat(listItem.size()).isEqualTo(0);
                })
                .verifyComplete();


        productPackage = ProductPackage.builder()
                .group_id(group)
                .price(3)
                .device_id("222222")
                .status(UserPackageDetail.Status.CREATE)
                .product_units(new com.example.heroku.model.UserPackage[]{
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("456")
                                .product_unit_second_id(beerUnit4561ID.get())
                                .number_unit(100)
                                .build(),
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("456")
                                .product_unit_second_id(beerUnit4561ID.get())
                                .number_unit(9)
                                .build()
                })
                .build();
        userPackageAPI.AddProductToPackage(productPackage)
                .block();

        userPackageAPI.GetMyPackage(UserID.builder().id("222222").page(0).size(1000).group_id(group).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice).reversed())
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(1);

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("456");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit4561ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(109);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("456");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    assertThat(listItem.size()).isEqualTo(0);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();

                    assertThat(listItem.size()).isEqualTo(2);
                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(100);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);

                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(9);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();

        userPackageAPI.GetByStatusBetween(UserPackageID.builder().group_id(group).product_second_id("123")
                        .product_unit_second_id(beerUnit1ID.get())
                        .from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00"))
                        .status(UserPackageDetail.Status.CREATE)
                        .page(0)
                        .size(100)
                        .build())
                .sort(Comparator.comparing(com.example.heroku.model.UserPackage::getDevice_id))
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    assertThat(userPackage.getProduct_second_id()).isEqualTo("123");
                    assertThat(userPackage.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(userPackage.getNumber_unit()).isEqualTo(100);
                })
                .consumeNextWith(userPackage -> {

                    assertThat(userPackage.getProduct_second_id()).isEqualTo("123");
                    assertThat(userPackage.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(userPackage.getNumber_unit()).isEqualTo(106);
                })
                .verifyComplete();

        userPackageAPI.DeleteByBeerUnit(PackageItemRemove.builder().device_id("222222").unit_id(beerUnit1ID.get()).group_id(group).build(), UserPackageDetail.Status.CREATE).blockLast();

        userPackageAPI.GetMyPackage(UserID.builder().id("222222").page(0).size(1000).group_id(group).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice).reversed())
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {
                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(1);

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("456");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit4561ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(109);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("456");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    assertThat(listItem.size()).isEqualTo(0);
                })
                .consumeNextWith(userPackage -> {
                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(1);

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(9);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();

        userPackageAPI.DeleteByUserID(UserID.builder().id("222222").page(0).size(1000).group_id(group).build(), UserPackageDetail.Status.CREATE).blockLast();

        userPackageAPI.GetMyPackage(UserID.builder().id("222222").page(0).size(1000).group_id(group).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice).reversed())
                .as(StepVerifier::create)
                .verifyComplete();

        userPackageAPI.GetMyPackage(UserID.builder().id("soldoutttt").page(0).size(1000).group_id(group).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice).reversed())
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_idddddtttt");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(106);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(12);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_iddddd");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    assertThat(listItem.size()).isEqualTo(0);
                })
                .verifyComplete();


        productPackage = ProductPackage.builder()
                .group_id(group)
                .device_id("save_package")
                .package_second_id("save_pack")
                .note("note here!!")
                .image("img.jpeg")
                .discount_amount(10000)
                .discount_percent(10)
                .discount_promotional(89)
                .discount_by_point(98)
                .additional_fee(87)
                .additional_config("additional_config")
                .ship_price(20000)
                .voucher("voucher_1d")
                .package_type("deliver")
                .progress("{}")
                .meta_search("hello meta search")
                .status(UserPackageDetail.Status.CREATE)
                .product_units(new com.example.heroku.model.UserPackage[]{
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit1ID.get())
                                .number_unit(100)
                                .discount_promotional(54)
                                .build(),
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit2ID.get())
                                .discount_promotional(45)
                                .number_unit(9)
                                .build()
                })
                .build();
        userPackageAPI.SavePackageWithoutCheck(productPackage)
                .block();

        userPackageAPI.GetPackage(PackageID.builder().group_id(group).device_id("save_package").package_id("save_pack").build())
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("save_package");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("save_pack");
                    assertThat(userPackage.getNote()).isEqualTo("note here!!");
                    assertThat(userPackage.getDiscount_promotional()).isEqualTo(89);
                    assertThat(userPackage.getDiscount_by_point()).isEqualTo(98);
                    assertThat(userPackage.getAdditional_fee()).isEqualTo(87);
                    assertThat(userPackage.getAdditional_config()).isEqualTo("additional_config");
                    assertThat(userPackage.getMeta_search()).isEqualTo("hello meta search");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(100);
                    assertThat(item.getDiscount_promotional()).isEqualTo(54);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(9);
                    assertThat(item.getDiscount_promotional()).isEqualTo(45);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();


        productPackage = ProductPackage.builder()
                .group_id(group)
                .device_id("save_package")
                .package_second_id("save_pack")
                .note("notettt here!!")
                .image("img.jpeg")
                .discount_amount(10000)
                .discount_percent(10)
                .ship_price(20000)
                .voucher("voucher_1d")
                .package_type("deliver")
                .progress("{}")
                .meta_search("hello meta search")
                .status(UserPackageDetail.Status.CREATE)
                .product_units(new com.example.heroku.model.UserPackage[]{
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit1ID.get())
                                .number_unit(3)
                                .build(),
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit2ID.get())
                                .number_unit(4)
                                .build()
                })
                .build();
        userPackageAPI.SavePackageWithoutCheckWithTransaction(ProductPackgeWithTransaction.builder()
                        .productPackage(productPackage)
                        .transation(PaymentTransation.builder()
                                .group_id(productPackage.getGroup_id())
                                .package_second_id(productPackage.getPackage_second_id())
                                .amount(productPackage.getPayment())
                                .money_source("tien mat")
                                .transaction_type(PaymentTransation.TType.INCOME)
                                .build())
                        .build())
                .block();

        userPackageAPI.GetPackage(PackageID.builder().group_id(group).device_id("save_package").package_id("save_pack").build())
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("save_package");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("save_pack");
                    assertThat(userPackage.getNote()).isEqualTo("notettt here!!");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit));

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(3);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(4);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();

        userPackageAPI.DeleteByUserID(UserID.builder().id("save_package").page(0).size(1000).group_id(group).build(), UserPackageDetail.Status.CREATE).blockLast();

        userPackageAPI.GetMyPackage(UserID.builder().id("save_package").page(0).size(1000).group_id(group).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice).reversed())
                .as(StepVerifier::create)
                .verifyComplete();

        userPackageAPI.GetPackage(PackageID.builder().group_id(group).device_id("save_package").package_id("save_pack").build())
                .as(StepVerifier::create)
                .verifyComplete();





        productPackage = ProductPackage.builder()
                .group_id(group)
                .device_id("save_package")
                .package_second_id("save_pack")
                .note("notettt here!!")
                .image("img.jpeg")
                .discount_amount(10000)
                .discount_percent(10)
                .ship_price(20000)
                .voucher("voucher_1d")
                .package_type("deliver")
                .progress("{}")
                .meta_search("hello meta search")
                .status(UserPackageDetail.Status.CREATE)
                .product_units(new com.example.heroku.model.UserPackage[]{
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit1ID.get())
                                .number_unit(3)
                                .build(),
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit2ID.get())
                                .number_unit(4)
                                .build()
                })
                .build();
        userPackageAPI.SavePackage(productPackage)
                .block();

        userPackageAPI.GetPackage(PackageID.builder().group_id(group).device_id("save_package").package_id("save_pack").build())
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("save_package");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("save_pack");
                    assertThat(userPackage.getNote()).isEqualTo("notettt here!!");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit));

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(3);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(4);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();

        userPackageAPI.GetPackage(PackageID.builder().group_id(group).device_id("save_package").package_id("save_pack").build())
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("save_package");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("save_pack");
                    assertThat(userPackage.getNote()).isEqualTo("notettt here!!");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit));

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(3);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(4);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();

        userPackageAPI.DeletePackage(PackageID.builder().group_id(group).device_id("save_package").package_id("save_pack").build()).block();

        userPackageAPI.GetPackage(PackageID.builder().group_id(group).device_id("save_package").package_id("save_pack").build())
                .as(StepVerifier::create)
                .verifyComplete();

        userPackageAPI.GetJustByPackageId(PackageID.builder().group_id(group).device_id("save_package").package_id("save_pack").build())
                .as(StepVerifier::create)
                .verifyComplete();


        productPackage = ProductPackage.builder()
                .group_id(group)
                .package_second_id("save_pack")
                .note("notettt here!!")
                .image("img.jpeg")
                .discount_amount(10000)
                .discount_percent(10)
                .discount_promotional(89)
                .discount_by_point(98)
                .additional_fee(87)
                .additional_config("additional_config")
                .ship_price(20000)
                .voucher("voucher_1d")
                .package_type("deliver")
                .progress("{}")
                .status(UserPackageDetail.Status.CREATE)
                .area_id("area_1")
                .area_name("tang 1")
                .table_id("table_1")
                .table_name("ban 1")
                .payment(7868)
                .cost(444)
                .profit(7878)
                .staff_id("nguyen_phong")
                .meta_search("hello meta search")
                .buyer(Buyer.builder()
                        .phone_number(" +84 0 229292 22")
                        .reciver_fullname("test create package")
                        .region_id(294)
                        .district_id(484)
                        .ward_id(10379)
                        .build())
                .product_units(new com.example.heroku.model.UserPackage[]{
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit1ID.get())
                                .number_unit(3)
                                .discount_promotional(54)
                                .buy_price(3)
                                .price(7)
                                .discount_amount(19)
                                .discount_percent(10)
                                .note("note hhh")
                                .build(),
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit2ID.get())
                                .buy_price(3)
                                .price(5)
                                .number_unit(4)
                                .build()
                })
                .build();
        userPackageAPI.SavePackage(productPackage)
                .block();

        userPackageAPI.GetPackage(PackageID.builder().group_id(group).device_id("0022929222").package_id("save_pack").build())
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("0022929222");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("save_pack");
                    assertThat(userPackage.getDiscount_promotional()).isEqualTo(89);
                    assertThat(userPackage.getDiscount_by_point()).isEqualTo(98);
                    assertThat(userPackage.getAdditional_fee()).isEqualTo(87);
                    assertThat(userPackage.getAdditional_config()).isEqualTo("additional_config");
                    assertThat(userPackage.getNote()).isEqualTo("notettt here!!");
                    assertThat(userPackage.getTable_id()).isEqualTo("table_1");
                    assertThat(userPackage.getTable_name()).isEqualTo("ban 1");
                    assertThat(userPackage.getArea_id()).isEqualTo("area_1");
                    assertThat(userPackage.getArea_name()).isEqualTo("tang 1");
                    assertThat(userPackage.getPayment()).isEqualTo(7868);
                    assertThat(userPackage.getCost()).isEqualTo(444);
                    assertThat(userPackage.getProfit()).isEqualTo(7878);
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    BuyerData buyer1 = userPackage.getBuyer();
                    assertThat(buyer1.getDevice_id()).isEqualTo("0022929222");
                    assertThat(buyer1.getRegion_id()).isEqualTo(294);
                    assertThat(buyer1.getDistrict_id()).isEqualTo(484);
                    assertThat(buyer1.getWard_id()).isEqualTo(10379);
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getRegion())).isEqualTo(Util.getInstance().RemoveAccent("Hồ Chí Minh"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getDistrict())).isEqualTo(Util.getInstance().RemoveAccent("Quận 1"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getWard())).isEqualTo(Util.getInstance().RemoveAccent("Phường Bến Nghé"));

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit));

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(3);
                    assertThat(item.getDiscount_promotional()).isEqualTo(54);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getDiscount_amount()).isEqualTo(19);
                    assertThat(item.getDiscount_percent()).isEqualTo(10);
                    assertThat(item.getNote()).isEqualTo("note hhh");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(4);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();

        userPackageAPI.GetJustByPackageId(PackageID.builder().group_id(group).device_id("0022929222").package_id("save_pack").build())
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("0022929222");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("save_pack");
                    assertThat(userPackage.getNote()).isEqualTo("notettt here!!");
                    assertThat(userPackage.getTable_id()).isEqualTo("table_1");
                    assertThat(userPackage.getTable_name()).isEqualTo("ban 1");
                    assertThat(userPackage.getArea_id()).isEqualTo("area_1");
                    assertThat(userPackage.getArea_name()).isEqualTo("tang 1");
                    assertThat(userPackage.getPayment()).isEqualTo(7868);
                    assertThat(userPackage.getCost()).isEqualTo(444);
                    assertThat(userPackage.getProfit()).isEqualTo(7878);
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    BuyerData buyer1 = userPackage.getBuyer();
                    assertThat(buyer1.getDevice_id()).isEqualTo("0022929222");
                    assertThat(buyer1.getRegion_id()).isEqualTo(294);
                    assertThat(buyer1.getDistrict_id()).isEqualTo(484);
                    assertThat(buyer1.getWard_id()).isEqualTo(10379);
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getRegion())).isEqualTo(Util.getInstance().RemoveAccent("Hồ Chí Minh"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getDistrict())).isEqualTo(Util.getInstance().RemoveAccent("Quận 1"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getWard())).isEqualTo(Util.getInstance().RemoveAccent("Phường Bến Nghé"));

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit));

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(3);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getDiscount_amount()).isEqualTo(19);
                    assertThat(item.getDiscount_percent()).isEqualTo(10);
                    assertThat(item.getNote()).isEqualTo("note hhh");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(4);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();


        userPackageAPI.GetPackageByGroup(UserID.builder().group_id(group).page(0).size(10000).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice))
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("0022929222");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("save_pack");
                    assertThat(userPackage.getNote()).isEqualTo("notettt here!!");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    BuyerData buyer1 = userPackage.getBuyer();
                    assertThat(buyer1.getDevice_id()).isEqualTo("0022929222");
                    assertThat(buyer1.getRegion_id()).isEqualTo(294);
                    assertThat(buyer1.getDistrict_id()).isEqualTo(484);
                    assertThat(buyer1.getWard_id()).isEqualTo(10379);
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getRegion())).isEqualTo(Util.getInstance().RemoveAccent("Hồ Chí Minh"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getDistrict())).isEqualTo(Util.getInstance().RemoveAccent("Quận 1"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getWard())).isEqualTo(Util.getInstance().RemoveAccent("Phường Bến Nghé"));

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit));

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(3);
                    assertThat(item.getDiscount_amount()).isEqualTo(19);
                    assertThat(item.getDiscount_percent()).isEqualTo(10);
                    assertThat(item.getNote()).isEqualTo("note hhh");
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(4);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_iddddd");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    assertThat(listItem.size()).isEqualTo(0);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_idddddtttt");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(106);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(12);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();


        userPackageAPI.GetWorkingPackageByGroup(UserID.builder().group_id(group).page(0).size(10000).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice))
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("0022929222");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("save_pack");
                    assertThat(userPackage.getNote()).isEqualTo("notettt here!!");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    BuyerData buyer1 = userPackage.getBuyer();
                    assertThat(buyer1.getDevice_id()).isEqualTo("0022929222");
                    assertThat(buyer1.getRegion_id()).isEqualTo(294);
                    assertThat(buyer1.getDistrict_id()).isEqualTo(484);
                    assertThat(buyer1.getWard_id()).isEqualTo(10379);
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getRegion())).isEqualTo(Util.getInstance().RemoveAccent("Hồ Chí Minh"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getDistrict())).isEqualTo(Util.getInstance().RemoveAccent("Quận 1"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getWard())).isEqualTo(Util.getInstance().RemoveAccent("Phường Bến Nghé"));

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit));

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(3);
                    assertThat(item.getDiscount_amount()).isEqualTo(19);
                    assertThat(item.getDiscount_percent()).isEqualTo(10);
                    assertThat(item.getNote()).isEqualTo("note hhh");
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(4);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_iddddd");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    assertThat(listItem.size()).isEqualTo(0);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_idddddtttt");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(106);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(12);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();


        userPackageAPI.GetWorkingPackageByGroupByJoinWith(UserID.builder().group_id(group).page(0).size(10000).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice))
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("0022929222");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("save_pack");
                    assertThat(userPackage.getNote()).isEqualTo("notettt here!!");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    BuyerData buyer1 = userPackage.getBuyer();
                    assertThat(buyer1.getDevice_id()).isEqualTo("0022929222");
                    assertThat(buyer1.getRegion_id()).isEqualTo(294);
                    assertThat(buyer1.getDistrict_id()).isEqualTo(484);
                    assertThat(buyer1.getWard_id()).isEqualTo(10379);
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getRegion())).isEqualTo(Util.getInstance().RemoveAccent("Hồ Chí Minh"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getDistrict())).isEqualTo(Util.getInstance().RemoveAccent("Quận 1"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getWard())).isEqualTo(Util.getInstance().RemoveAccent("Phường Bến Nghé"));

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit));

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(3);
                    assertThat(item.getDiscount_amount()).isEqualTo(19);
                    assertThat(item.getDiscount_percent()).isEqualTo(10);
                    assertThat(item.getNote()).isEqualTo("note hhh");
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(4);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_iddddd");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    assertThat(listItem.size()).isEqualTo(0);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_idddddtttt");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(106);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(12);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();


        userPackageAPI.GetPackageByGrouAndStatus(UserID.builder().group_id(group).page(0).size(10000).build(), UserPackageDetail.Status.CREATE)
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice))
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("0022929222");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("save_pack");
                    assertThat(userPackage.getNote()).isEqualTo("notettt here!!");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    BuyerData buyer1 = userPackage.getBuyer();
                    assertThat(buyer1.getDevice_id()).isEqualTo("0022929222");
                    assertThat(buyer1.getRegion_id()).isEqualTo(294);
                    assertThat(buyer1.getDistrict_id()).isEqualTo(484);
                    assertThat(buyer1.getWard_id()).isEqualTo(10379);
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getRegion())).isEqualTo(Util.getInstance().RemoveAccent("Hồ Chí Minh"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getDistrict())).isEqualTo(Util.getInstance().RemoveAccent("Quận 1"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getWard())).isEqualTo(Util.getInstance().RemoveAccent("Phường Bến Nghé"));

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit));

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(3);
                    assertThat(item.getDiscount_amount()).isEqualTo(19);
                    assertThat(item.getDiscount_percent()).isEqualTo(10);
                    assertThat(item.getNote()).isEqualTo("note hhh");
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(4);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_iddddd");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    assertThat(listItem.size()).isEqualTo(0);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_idddddtttt");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(106);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(12);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();

        buyer.FindByDeviceID(group, "0022929222")
                .as(StepVerifier::create)
                .consumeNextWith(buyerData -> {
                    assertThat(buyerData.getDiscount()).isEqualTo(0);
                    assertThat(buyerData.getReal_price()).isEqualTo(0);
                    assertThat(buyerData.getTotal_price()).isEqualTo(0);
                    assertThat(buyerData.getShip_price()).isEqualTo(0);
                    assertThat(buyerData.getPoint()).isEqualTo(0);
                    assertThat(buyerData.getDevice_id()).isEqualTo("0022929222");
                    assertThat(buyerData.getRegion_id()).isEqualTo(294);
                    assertThat(buyerData.getDistrict_id()).isEqualTo(484);
                    assertThat(buyerData.getWard_id()).isEqualTo(10379);
                    assertThat(Util.getInstance().RemoveAccent(buyerData.getRegion())).isEqualTo(Util.getInstance().RemoveAccent("Hồ Chí Minh"));
                    assertThat(Util.getInstance().RemoveAccent(buyerData.getDistrict())).isEqualTo(Util.getInstance().RemoveAccent("Quận 1"));
                    assertThat(Util.getInstance().RemoveAccent(buyerData.getWard())).isEqualTo(Util.getInstance().RemoveAccent("Phường Bến Nghé"));
                    assertThat(buyerData.getReciver_fullname()).isEqualTo("test create package");
                })
                .verifyComplete();

        buyer.getBuyerStatictis(PackageID.builder().group_id(group).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).device_id("0022929222").page(0).size(3).page(0).status(UserPackageDetail.Status.CREATE).build())
                .as(StepVerifier::create)
                .consumeNextWith(buyerStatictisData -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(buyerStatictisData));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    BenifitByMonth benifitByMonth = buyerStatictisData.getBenifitByMonth();
                    assertThat(benifitByMonth.getRevenue()).isEqualTo(7868);
                    assertThat(benifitByMonth.getDiscount()).isEqualTo(10000);
                    assertThat(benifitByMonth.getCount()).isEqualTo(1);

                    List<PackageDataResponse> packageDataResponses = buyerStatictisData.getPackageDataResponses();
                    assertThat(packageDataResponses.size()).isEqualTo(1);
                    PackageDataResponse userPackage = packageDataResponses.get(0);
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("save_pack");
                    assertThat(userPackage.getNote()).isEqualTo("notettt here!!");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    List<BenifitByProduct> benifitByProducts = buyerStatictisData.getBenifitByProducts();
                    assertThat(benifitByProducts.size()).isEqualTo(2);
                    BenifitByProduct thung = benifitByProducts.get(0);
                    if (thung.getProduct_unit_name().equals("lon")) {
                        thung = benifitByProducts.get(1);
                    }
                    assertThat(thung.getProduct_name()).isEqualTo("beer tiger");
                    assertThat(thung.getRevenue()).isEqualTo(20);
                    assertThat(thung.getProfit()).isEqualTo(8);
                    assertThat(thung.getNumber_unit()).isEqualTo(4);
                })
                .verifyComplete();


        statisticServices.getPackageStatictis(PackageID.builder().group_id(group).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).status(UserPackageDetail.Status.CREATE).build())
                .as(StepVerifier::create)
                .consumeNextWith(data -> {
                    assertThat(data.getCost()).isEqualTo(444);
                    assertThat(data.getProfit()).isEqualTo(7878);
                    assertThat(data.getRevenue()).isEqualTo(7868);
                    assertThat(data.getPrice()).isEqualTo(4);
                    assertThat(data.getCount()).isEqualTo(3);
                    assertThat(data.getBuyer()).isEqualTo(2);
                    assertThat(data.getShip_price()).isEqualTo(60000);
                    assertThat(data.getDiscount_by_point()).isEqualTo(98);
                    assertThat(data.getDiscount_promotional()).isEqualTo(89);
                    assertThat(data.getAdditional_fee()).isEqualTo(87);
                    assertThat(data.getReturn_price()).isEqualTo(0);
                    assertThat(new DecimalFormat("0.0").format(data.getDiscount())).isEqualTo("30000.4");
                })
                .verifyComplete();


        statisticServices.getBenifitOfPaymentByDateStatictis(PackageID.builder().group_id(group).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).status(UserPackageDetail.Status.CREATE).build())
                .as(StepVerifier::create)
                .consumeNextWith(data -> {
                    assertThat(data.getReturn_price()).isEqualTo(0);
                    assertThat(data.getBenifit_by_date().size()).isEqualTo(1);
                    assertThat(data.getBenifit_by_date_transaction().size()).isEqualTo(0);
                    assertThat(data.getBenifit_by_category_transaction().size()).isEqualTo(0);


                    BenifitByDate benifitByDate = data.getBenifit_by_date().get(0);


                    assertThat(benifitByDate.getCost()).isEqualTo(444);
                    assertThat(benifitByDate.getProfit()).isEqualTo(7878);
                    assertThat(benifitByDate.getRevenue()).isEqualTo(7868);
                    assertThat(benifitByDate.getPrice()).isEqualTo(4);
                    assertThat(benifitByDate.getCount()).isEqualTo(3);
                    assertThat(benifitByDate.getBuyer()).isEqualTo(2);
                    assertThat(benifitByDate.getShip_price()).isEqualTo(60000);
                    assertThat(benifitByDate.getDiscount_by_point()).isEqualTo(98);
                    assertThat(benifitByDate.getDiscount_promotional()).isEqualTo(89);
                    assertThat(benifitByDate.getAdditional_fee()).isEqualTo(87);
                    assertThat(benifitByDate.getReturn_price()).isEqualTo(0);
                    assertThat(new DecimalFormat("0.0").format(benifitByDate.getDiscount())).isEqualTo("30000.4");

                })
                .verifyComplete();

        statisticServices.getPackageStatictisByHour(PackageID.builder().group_id(group).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).status(UserPackageDetail.Status.CREATE).build())
                .sort(Comparator.comparing(BenifitByDateHour::getRevenue))
                .as(StepVerifier::create)
                .consumeNextWith(data -> {
                    assertThat(data.getCost()).isEqualTo(444);
                    assertThat(data.getProfit()).isEqualTo(7878);
                    assertThat(data.getRevenue()).isEqualTo(7868);
                    assertThat(data.getCount()).isEqualTo(3);
                    assertThat(data.getBuyer()).isEqualTo(2);
                    assertThat(data.getShip_price()).isEqualTo(60000);
                    assertThat(data.getPrice()).isEqualTo(4);
                    assertThat(data.getDiscount_by_point()).isEqualTo(98);
                    assertThat(data.getDiscount_promotional()).isEqualTo(89);
                    assertThat(data.getAdditional_fee()).isEqualTo(87);
                    assertThat(data.getReturn_price()).isEqualTo(0);
                    assertThat(new DecimalFormat("0.0").format(data.getDiscount())).isEqualTo("30000.4");
                })
                .verifyComplete();

        statisticServices.getBenifitOfPaymentByHourStatictis(PackageID.builder().group_id(group).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).status(UserPackageDetail.Status.CREATE).build())
                .as(StepVerifier::create)
                .consumeNextWith(data -> {
                    assertThat(data.getReturn_price()).isEqualTo(0);
                    assertThat(data.getBenifit_by_hour().size()).isEqualTo(1);
                    assertThat(data.getBenifit_by_hour_transaction().size()).isEqualTo(0);
                    assertThat(data.getBenifit_by_category_transaction().size()).isEqualTo(0);


                    BenifitByDateHour benifitByHour = data.getBenifit_by_hour().get(0);


                    assertThat(benifitByHour.getCost()).isEqualTo(444);
                    assertThat(benifitByHour.getProfit()).isEqualTo(7878);
                    assertThat(benifitByHour.getRevenue()).isEqualTo(7868);
                    assertThat(benifitByHour.getCount()).isEqualTo(3);
                    assertThat(benifitByHour.getBuyer()).isEqualTo(2);
                    assertThat(benifitByHour.getShip_price()).isEqualTo(60000);
                    assertThat(benifitByHour.getPrice()).isEqualTo(4);
                    assertThat(benifitByHour.getDiscount_by_point()).isEqualTo(98);
                    assertThat(benifitByHour.getDiscount_promotional()).isEqualTo(89);
                    assertThat(benifitByHour.getAdditional_fee()).isEqualTo(87);
                    assertThat(data.getReturn_price()).isEqualTo(0);
                    assertThat(new DecimalFormat("0.0").format(benifitByHour.getDiscount())).isEqualTo("30000.4");
                })
                .verifyComplete();

        statisticServices.getPackageTotalStatictis(PackageID.builder().group_id(group).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).status(UserPackageDetail.Status.CREATE).build())
                .as(StepVerifier::create)
                .consumeNextWith(data -> {
                    assertThat(data.getCost()).isEqualTo(444);
                    assertThat(data.getProfit()).isEqualTo(7878);
                    assertThat(data.getRevenue()).isEqualTo(7868);
                    assertThat(data.getCount()).isEqualTo(3);
                    assertThat(data.getBuyer()).isEqualTo(2);
                    assertThat(data.getPrice()).isEqualTo(4);
                    assertThat(data.getShip_price()).isEqualTo(60000);
                    assertThat(new DecimalFormat("0.0").format(data.getDiscount())).isEqualTo("30000.4");
                })
                .verifyComplete();

        statisticServices.getProductBenifitStatictis(PackageID.builder().group_id(group).page(0).size(1000).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).status(UserPackageDetail.Status.CREATE).build())
                .sort(Comparator.comparing(BenifitByProduct::getRevenue))
                .as(StepVerifier::create)
                .consumeNextWith(data -> {
                    assertThat(data.getProduct_second_id()).isEqualTo("123");
                    assertThat(data.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(data.getNumber_unit()).isEqualTo(109);
                    assertThat(data.getProfit()).isEqualTo(-47.1f);// (7*(1 - 0.1) - 19 - 3)*3
                    assertThat(data.getRevenue()).isEqualTo(-38.1f);// (7*(1 - 0.1) - 19)*3
                    assertThat(data.getProduct_name()).isEqualTo("beer tiger");
                    assertThat(data.getProduct_unit_name()).isEqualTo("lon");
                })
                .consumeNextWith(data -> {
                    assertThat(data.getProduct_second_id()).isEqualTo("123");
                    assertThat(data.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(data.getNumber_unit()).isEqualTo(16);
                    assertThat(data.getRevenue()).isEqualTo(20);
                    assertThat(data.getProfit()).isEqualTo(8);
                    assertThat(data.getProduct_name()).isEqualTo("beer tiger");
                    assertThat(data.getProduct_unit_name()).isEqualTo("thung");
                })
                .verifyComplete();

        statisticServices.getBuyerBenifitStatictis(PackageID.builder().group_id(group).page(0).size(1000).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).status(UserPackageDetail.Status.CREATE).build())
                .sort(Comparator.comparing(BenifitByBuyer::getRevenue))
                .as(StepVerifier::create)
                .consumeNextWith(data -> {
                    assertThat(data.getName()).isEqualTo(null);
                    assertThat(data.getId()).isEqualTo(null);//"soldoutttt"
                    assertThat(data.getCount()).isEqualTo(2);
                    assertThat(data.getRevenue()).isEqualTo(0.0f);
                    assertThat(data.getProfit()).isEqualTo(0.0f);
                })
                .consumeNextWith(data -> {
                    assertThat(data.getName()).isEqualTo("test create package");
                    assertThat(data.getId()).isEqualTo(" +84 0 229292 22");
                    assertThat(data.getCount()).isEqualTo(1);
                    assertThat(data.getRevenue()).isEqualTo(7868.0f);
                    assertThat(data.getProfit()).isEqualTo(7878.0f);
                })
                .verifyComplete();

        statisticServices.getStaffBenifitStatictis(PackageID.builder().group_id(group).page(0).size(1000).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).status(UserPackageDetail.Status.CREATE).build())
                .sort(Comparator.comparing(BenifitByBuyer::getRevenue))
                .as(StepVerifier::create)
                .consumeNextWith(data -> {
                    assertThat(data.getName()).isEqualTo(null);
                    assertThat(data.getId()).isEqualTo(null);
                    assertThat(data.getPhone()).isEqualTo(null);
                    assertThat(data.getCount()).isEqualTo(2);
                    assertThat(data.getRevenue()).isEqualTo(0.0f);// 7*3*(1-0.1) - 19
                    assertThat(data.getProfit()).isEqualTo(0.0f);// 7*3*(1-0.1) - 19
                })
                .consumeNextWith(data -> {
                    assertThat(data.getName()).isEqualTo(null);
                    assertThat(data.getId()).isEqualTo("nguyen_phong");
                    assertThat(data.getPhone()).isEqualTo(null);
                    assertThat(data.getCount()).isEqualTo(1);
                    assertThat(data.getRevenue()).isEqualTo(7868.0f);// 7*3*(1-0.1) - 19
                    assertThat(data.getProfit()).isEqualTo(7878.0f);// 7*3*(1-0.1) - 19
                })
                .verifyComplete();

        statisticServices.getOrderBenifitStatictis(PackageID.builder().group_id(group).page(0).size(1000).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).status(UserPackageDetail.Status.CREATE).build())
                .sort(Comparator.comparing(BenifitByOrder::getRevenue))
                .as(StepVerifier::create)
                .consumeNextWith(data -> {
                    assertThat(data.getPackage_second_id()).isEqualTo("package_iddddd");
                    assertThat(data.getPrice()).isEqualTo(1);
                    assertThat(data.getShip_price()).isEqualTo(20000);
                    assertThat(data.getRevenue()).isEqualTo(0.0f);// 7*3*(1-0.1) - 19
                    assertThat(data.getProfit()).isEqualTo(0.0f);// 7*3*(1-0.1) - 19
                })
                .consumeNextWith(data -> {
                    assertThat(data.getPackage_second_id()).isEqualTo("package_idddddtttt");
                    assertThat(data.getPrice()).isEqualTo(3);
                    assertThat(data.getShip_price()).isEqualTo(20000);
                    assertThat(data.getRevenue()).isEqualTo(0.0f);// 7*3*(1-0.1) - 19
                    assertThat(data.getProfit()).isEqualTo(0.0f);// 7*3*(1-0.1) - 19
                })
                .consumeNextWith(data -> {
                    assertThat(data.getPackage_second_id()).isEqualTo("save_pack");
                    assertThat(data.getPrice()).isEqualTo(0);
                    assertThat(data.getShip_price()).isEqualTo(20000);
                    assertThat(data.getRevenue()).isEqualTo(7868.0f);// 7*3*(1-0.1) - 19
                    assertThat(data.getProfit()).isEqualTo(7878.0f);// 7*3*(1-0.1) - 19
                })
                .verifyComplete();


        productPackage = ProductPackage.builder()
                .group_id(group)
                .package_second_id("save_pack")
                .note("notettt here!!")
                .image("img.jpeg")
                .discount_amount(10000)
                .discount_percent(10)
                .ship_price(20000)
                .voucher("voucher_1d")
                .package_type("deliver")
                .progress("{}")
                .status(UserPackageDetail.Status.DONE)
                .area_id("area_1")
                .area_name("tang 1")
                .table_id("table_1")
                .table_name("ban 1")
                .payment(7868)
                .cost(444)
                .profit(7878)
                .staff_id("nguyen_phong")
                .point(-77)
                .meta_search("hello meta search")
                .buyer(Buyer.builder()
                        .phone_number(" +84 0 229292 22")
                        .reciver_fullname("test create package")
                        .region_id(294)
                        .district_id(484)
                        .ward_id(10379)
                        .build())
                .product_units(new com.example.heroku.model.UserPackage[]{
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit1ID.get())
                                .number_unit(3)
                                .buy_price(3)
                                .price(7)
                                .discount_amount(19)
                                .discount_percent(10)
                                .note("note hhh")
                                .build(),
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit2ID.get())
                                .buy_price(3)
                                .price(5)
                                .number_unit(4)
                                .build()
                })
                .build();
        userPackageAPI.SavePackage(productPackage)
                .block();


        userPackageAPI.GetWorkingPackageByGroup(UserID.builder().group_id(group).page(0).size(10000).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice))
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("0022929222");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("save_pack");
                    assertThat(userPackage.getNote()).isEqualTo("notettt here!!");
                    assertThat(userPackage.getPoint()).isEqualTo(-77);
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    BuyerData buyer1 = userPackage.getBuyer();
                    assertThat(buyer1.getDevice_id()).isEqualTo("0022929222");
                    assertThat(buyer1.getRegion_id()).isEqualTo(294);
                    assertThat(buyer1.getDistrict_id()).isEqualTo(484);
                    assertThat(buyer1.getWard_id()).isEqualTo(10379);
                    assertThat(buyer1.getPoint()).isEqualTo(-77);
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getRegion())).isEqualTo(Util.getInstance().RemoveAccent("Hồ Chí Minh"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getDistrict())).isEqualTo(Util.getInstance().RemoveAccent("Quận 1"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getWard())).isEqualTo(Util.getInstance().RemoveAccent("Phường Bến Nghé"));

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit));

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(3);
                    assertThat(item.getDiscount_amount()).isEqualTo(19);
                    assertThat(item.getDiscount_percent()).isEqualTo(10);
                    assertThat(item.getNote()).isEqualTo("note hhh");
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(4);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_iddddd");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    assertThat(listItem.size()).isEqualTo(0);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_idddddtttt");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(106);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(12);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();


        userPackageAPI.GetWorkingPackageByGroupByJoinWith(UserID.builder().group_id(group).page(0).size(10000).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice))
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("0022929222");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("save_pack");
                    assertThat(userPackage.getNote()).isEqualTo("notettt here!!");
                    assertThat(userPackage.getPoint()).isEqualTo(-77);
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    BuyerData buyer1 = userPackage.getBuyer();
                    assertThat(buyer1.getDevice_id()).isEqualTo("0022929222");
                    assertThat(buyer1.getRegion_id()).isEqualTo(294);
                    assertThat(buyer1.getDistrict_id()).isEqualTo(484);
                    assertThat(buyer1.getWard_id()).isEqualTo(10379);
                    assertThat(buyer1.getPoint()).isEqualTo(-77);
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getRegion())).isEqualTo(Util.getInstance().RemoveAccent("Hồ Chí Minh"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getDistrict())).isEqualTo(Util.getInstance().RemoveAccent("Quận 1"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getWard())).isEqualTo(Util.getInstance().RemoveAccent("Phường Bến Nghé"));

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit));

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(3);
                    assertThat(item.getDiscount_amount()).isEqualTo(19);
                    assertThat(item.getDiscount_percent()).isEqualTo(10);
                    assertThat(item.getNote()).isEqualTo("note hhh");
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(4);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_iddddd");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    assertThat(listItem.size()).isEqualTo(0);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_idddddtttt");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(106);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(12);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();


        userPackageAPI.GetPackageByGrouAndStatus(UserID.builder().group_id(group).page(0).size(10000).build(), UserPackageDetail.Status.DONE)
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice))
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("0022929222");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("save_pack");
                    assertThat(userPackage.getNote()).isEqualTo("notettt here!!");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    BuyerData buyer1 = userPackage.getBuyer();
                    assertThat(buyer1.getDevice_id()).isEqualTo("0022929222");
                    assertThat(buyer1.getRegion_id()).isEqualTo(294);
                    assertThat(buyer1.getDistrict_id()).isEqualTo(484);
                    assertThat(buyer1.getWard_id()).isEqualTo(10379);
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getRegion())).isEqualTo(Util.getInstance().RemoveAccent("Hồ Chí Minh"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getDistrict())).isEqualTo(Util.getInstance().RemoveAccent("Quận 1"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getWard())).isEqualTo(Util.getInstance().RemoveAccent("Phường Bến Nghé"));

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit));

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(3);
                    assertThat(item.getDiscount_amount()).isEqualTo(19);
                    assertThat(item.getDiscount_percent()).isEqualTo(10);
                    assertThat(item.getNote()).isEqualTo("note hhh");
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(4);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();

        buyer.FindByDeviceID(group, "0022929222")
                .as(StepVerifier::create)
                .consumeNextWith(buyerData -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(buyerData));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(buyerData.getDiscount()).isEqualTo(10000);
                    assertThat(buyerData.getReal_price()).isEqualTo(7868);
                    assertThat(buyerData.getTotal_price()).isEqualTo(0);
                    assertThat(buyerData.getShip_price()).isEqualTo(20000);
                    assertThat(buyerData.getPoint()).isEqualTo(-77);
                    assertThat(buyerData.getDevice_id()).isEqualTo("0022929222");
                    assertThat(buyerData.getRegion_id()).isEqualTo(294);
                    assertThat(buyerData.getDistrict_id()).isEqualTo(484);
                    assertThat(buyerData.getWard_id()).isEqualTo(10379);
                    assertThat(Util.getInstance().RemoveAccent(buyerData.getRegion())).isEqualTo(Util.getInstance().RemoveAccent("Hồ Chí Minh"));
                    assertThat(Util.getInstance().RemoveAccent(buyerData.getDistrict())).isEqualTo(Util.getInstance().RemoveAccent("Quận 1"));
                    assertThat(Util.getInstance().RemoveAccent(buyerData.getWard())).isEqualTo(Util.getInstance().RemoveAccent("Phường Bến Nghé"));
                    assertThat(buyerData.getReciver_fullname()).isEqualTo("test create package");
                })
                .verifyComplete();


        productPackage = ProductPackage.builder()
                .group_id(group)
                .package_second_id("save_pack")
                .note("notettt here!!")
                .image("img.jpeg")
                .discount_amount(10000)
                .discount_percent(10)
                .ship_price(20000)
                .voucher("voucher_1d")
                .package_type("deliver")
                .progress("{}")
                .status(UserPackageDetail.Status.DONE)
                .area_id("area_1")
                .area_name("tang 1")
                .table_id("table_1")
                .table_name("ban 1")
                .payment(7868)
                .cost(444)
                .profit(7878)
                .staff_id("nguyen_phong")
                .meta_search("hello meta search")
                .buyer(Buyer.builder()
                        .phone_number(" +84 0 229292 22")
                        .reciver_fullname("test create package")
                        .region_id(294)
                        .district_id(484)
                        .ward_id(10379)
                        .build())
                .product_units(new com.example.heroku.model.UserPackage[]{
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit1ID.get())
                                .number_unit(3)
                                .buy_price(3)
                                .price(7)
                                .discount_amount(19)
                                .discount_percent(10)
                                .note("note hhh")
                                .build(),
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit2ID.get())
                                .buy_price(3)
                                .price(5)
                                .number_unit(4)
                                .build()
                })
                .build();
        userPackageAPI.SavePackage(productPackage)
                .block();

        userPackageAPI.CancelPackage(PackageID.builder().group_id(group).package_id("save_pack").build()).block();



        productPackage = ProductPackage.builder()
                .group_id(group)
                .package_second_id("save_pack_temp")
                .note("notettt here!!")
                .image("img.jpeg")
                .discount_amount(10000)
                .discount_percent(10)
                .ship_price(20000)
                .voucher("voucher_1d")
                .package_type("deliver")
                .progress("{}")
                .status(UserPackageDetail.Status.WEB_TEMP)
                .area_id("area_1")
                .area_name("tang 1")
                .table_id("table_1")
                .table_name("ban 1")
                .payment(7868)
                .cost(444)
                .profit(7878)
                .staff_id("nguyen_phong")
                .buyer(Buyer.builder()
                        .phone_number(" +84 0 229292 223")
                        .reciver_fullname("test create package")
                        .region_id(294)
                        .district_id(484)
                        .ward_id(10379)
                        .build())
                .product_units(new com.example.heroku.model.UserPackage[]{
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit1ID.get())
                                .number_unit(3)
                                .buy_price(3)
                                .price(7)
                                .discount_amount(19)
                                .discount_percent(10)
                                .note("note hhh")
                                .build(),
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit2ID.get())
                                .buy_price(3)
                                .price(5)
                                .number_unit(4)
                                .build()
                })
                .build();
        userPackageAPI.SavePackage(productPackage)
                .block();

        productPackage.getBuyer().setDevice_id("device_wrong");
        userPackageAPI.BuyerFromWebSubmitPackage(productPackage).block();

        userPackageAPI.GetPackageByGrouAndStatus(UserID.builder().group_id(group).page(0).size(10000).build(), UserPackageDetail.Status.WEB_SUBMIT)
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice))
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("00229292223");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("save_pack_temp");
                    assertThat(userPackage.getNote()).isEqualTo("notettt here!!");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    BuyerData buyer1 = userPackage.getBuyer();
                    assertThat(buyer1.getDevice_id()).isEqualTo("00229292223");
                    assertThat(buyer1.getRegion_id()).isEqualTo(294);
                    assertThat(buyer1.getDistrict_id()).isEqualTo(484);
                    assertThat(buyer1.getWard_id()).isEqualTo(10379);
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getRegion())).isEqualTo(Util.getInstance().RemoveAccent("Hồ Chí Minh"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getDistrict())).isEqualTo(Util.getInstance().RemoveAccent("Quận 1"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getWard())).isEqualTo(Util.getInstance().RemoveAccent("Phường Bến Nghé"));

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit));

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(3);
                    assertThat(item.getDiscount_amount()).isEqualTo(19);
                    assertThat(item.getDiscount_percent()).isEqualTo(10);
                    assertThat(item.getNote()).isEqualTo("note hhh");
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(4);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();

        userPackageAPI.GetPackageByGrouAndStatus(UserID.builder().group_id(group).page(0).size(10000).build(), UserPackageDetail.Status.DONE)
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice))
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("0022929222");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("save_pack");
                    assertThat(userPackage.getNote()).isEqualTo("notettt here!!");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    BuyerData buyer1 = userPackage.getBuyer();
                    assertThat(buyer1.getDevice_id()).isEqualTo("0022929222");
                    assertThat(buyer1.getRegion_id()).isEqualTo(294);
                    assertThat(buyer1.getDistrict_id()).isEqualTo(484);
                    assertThat(buyer1.getWard_id()).isEqualTo(10379);
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getRegion())).isEqualTo(Util.getInstance().RemoveAccent("Hồ Chí Minh"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getDistrict())).isEqualTo(Util.getInstance().RemoveAccent("Quận 1"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getWard())).isEqualTo(Util.getInstance().RemoveAccent("Phường Bến Nghé"));

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit));

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(3);
                    assertThat(item.getDiscount_amount()).isEqualTo(19);
                    assertThat(item.getDiscount_percent()).isEqualTo(10);
                    assertThat(item.getNote()).isEqualTo("note hhh");
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(4);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();

        userPackageAPI.BuyerFromWebSubmitPackage(ProductPackage.builder()
                .group_id(group)
                .package_second_id("save_pack").build()).block();

        // check product inventory
        this.beerAPI.GetBeerByID(group, "123")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    Flux.just(beerInfo.getProductUnit())
                            .sort(Comparator.comparing(com.example.heroku.model.ProductUnit::getName))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    assertThat(beerUnit.getInventory_number()).isEqualTo(543 - 106 - 3);
                                } else {
                                    assertThat(beerUnit.getInventory_number()).isEqualTo(345 - 12 - 4);
                                }
                            })
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    assertThat(beerUnit.getInventory_number()).isEqualTo(543 - 106 - 3);
                                } else {
                                    assertThat(beerUnit.getInventory_number()).isEqualTo(345 - 12 - 4);
                                }
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        userPackageAPI.ReturnPackage(PackageID.builder().group_id(group).package_id("save_pack").build()).block();


        userPackageAPI.GetPackageByGrouAndStatus(UserID.builder().group_id(group).page(0).size(10000).build(), UserPackageDetail.Status.DONE)
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice))
                .as(StepVerifier::create)
                .verifyComplete();


        userPackageAPI.GetPackageByGrouAndStatusAfterID(UserID.builder().group_id(group).id("1000").page(0).size(10000).build(), UserPackageDetail.Status.RETURN)
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice))
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("0022929222");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("save_pack");
                    assertThat(userPackage.getNote()).isEqualTo("notettt here!!");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    BuyerData buyer1 = userPackage.getBuyer();
                    assertThat(buyer1.getDevice_id()).isEqualTo("0022929222");
                    assertThat(buyer1.getRegion_id()).isEqualTo(294);
                    assertThat(buyer1.getDistrict_id()).isEqualTo(484);
                    assertThat(buyer1.getWard_id()).isEqualTo(10379);
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getRegion())).isEqualTo(Util.getInstance().RemoveAccent("Hồ Chí Minh"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getDistrict())).isEqualTo(Util.getInstance().RemoveAccent("Quận 1"));
                    assertThat(Util.getInstance().RemoveAccent(buyer1.getWard())).isEqualTo(Util.getInstance().RemoveAccent("Phường Bến Nghé"));

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit));

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(3);
                    assertThat(item.getDiscount_amount()).isEqualTo(19);
                    assertThat(item.getDiscount_percent()).isEqualTo(10);
                    assertThat(item.getNote()).isEqualTo("note hhh");
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(4);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();


        userPackageAPI.GetWorkingPackageByGroup(UserID.builder().group_id(group).page(0).size(10000).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice))
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_iddddd");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    assertThat(listItem.size()).isEqualTo(0);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_idddddtttt");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(106);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(12);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();


        userPackageAPI.GetWorkingPackageByGroupByJoinWith(UserID.builder().group_id(group).page(0).size(10000).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice))
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_iddddd");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    assertThat(listItem.size()).isEqualTo(0);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_idddddtttt");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(106);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(12);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();


        userPackageAPI.SearchWorkingPackageByGroupByJoinWith(SearchQuery.builder().query("hello met").group_id(group).page(0).size(10000).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice))
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_iddddd");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    assertThat(listItem.size()).isEqualTo(0);
                })
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_idddddtttt");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(106);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);


                    item = listItem.get(1);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(12);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();

        buyer.FindByDeviceID(group, "0022929222")
                .as(StepVerifier::create)
                .consumeNextWith(buyerData -> {
                    assertThat(buyerData.getDiscount()).isEqualTo(0);
                    assertThat(buyerData.getReal_price()).isEqualTo(0);
                    assertThat(buyerData.getTotal_price()).isEqualTo(0);
                    assertThat(buyerData.getShip_price()).isEqualTo(0);
                    assertThat(buyerData.getPoint()).isEqualTo(0);
                    assertThat(buyerData.getDevice_id()).isEqualTo("0022929222");
                    assertThat(buyerData.getRegion_id()).isEqualTo(294);
                    assertThat(buyerData.getDistrict_id()).isEqualTo(484);
                    assertThat(buyerData.getWard_id()).isEqualTo(10379);
                    assertThat(Util.getInstance().RemoveAccent(buyerData.getRegion())).isEqualTo(Util.getInstance().RemoveAccent("Hồ Chí Minh"));
                    assertThat(Util.getInstance().RemoveAccent(buyerData.getDistrict())).isEqualTo(Util.getInstance().RemoveAccent("Quận 1"));
                    assertThat(Util.getInstance().RemoveAccent(buyerData.getWard())).isEqualTo(Util.getInstance().RemoveAccent("Phường Bến Nghé"));
                    assertThat(buyerData.getReciver_fullname()).isEqualTo("test create package");
                })
                .verifyComplete();



        productPackage = ProductPackage.builder()
                .group_id(group)
                .device_id("soldoutttt")
                .package_second_id("package_idddddtttt")
                .note("note here!!")
                .image("img.jpeg")
                .price(3)
                .discount_amount(10000)
                .discount_percent(10)
                .ship_price(20000)
                .voucher("voucher_1d")
                .package_type("deliver")
                .progress("{}")
                .status(UserPackageDetail.Status.CANCEL)
                .product_units(new com.example.heroku.model.UserPackage[]{
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit1ID.get())
                                .number_unit(11)
                                .build(),
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit1ID.get())
                                .number_unit(-5)
                                .build(),
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("123")
                                .product_unit_second_id(beerUnit2ID.get())
                                .number_unit(3)
                                .build()
                })
                .build();
        userPackageAPI.SavePackage(productPackage)
                .block();


        userPackageAPI.GetWorkingPackageByGroupAfterID(UserID.builder().group_id(group).id("1000").page(0).size(10000).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice))
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_iddddd");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    assertThat(listItem.size()).isEqualTo(0);
                })
                .verifyComplete();


        userPackageAPI.GetWorkingPackageByGroupByJoinWith(UserID.builder().group_id(group).id("1000").page(0).size(10000).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice))
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_iddddd");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    assertThat(listItem.size()).isEqualTo(0);
                })
                .verifyComplete();


        userPackageAPI.SearchWorkingPackageByGroupByJoinWith(SearchQuery.builder().group_id(group).query("hello met").filter("1000").page(0).size(10000).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice))
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {

                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    assertThat(userPackage.getDevice_id()).isEqualTo("soldoutttt");
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("package_iddddd");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    listItem.sort(Comparator.comparingDouble(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    assertThat(listItem.size()).isEqualTo(0);
                })
                .verifyComplete();

        buyer.getBuyerStatictis(PackageID.builder().group_id(group).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).device_id("0022929222").page(0).size(3).page(0).status(UserPackageDetail.Status.CREATE).build())
                .as(StepVerifier::create)
                .consumeNextWith(buyerStatictisData -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(buyerStatictisData));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    BenifitByMonth benifitByMonth = buyerStatictisData.getBenifitByMonth();
                    assertThat(benifitByMonth.getRevenue()).isEqualTo(0);
                    assertThat(benifitByMonth.getDiscount()).isEqualTo(0);
                    assertThat(benifitByMonth.getCount()).isEqualTo(0);

                    List<PackageDataResponse> packageDataResponses = buyerStatictisData.getPackageDataResponses();
                    assertThat(packageDataResponses.size()).isEqualTo(1);
                    PackageDataResponse userPackage = packageDataResponses.get(0);
                    assertThat(userPackage.getPackage_second_id()).isEqualTo("save_pack");
                    assertThat(userPackage.getNote()).isEqualTo("notettt here!!");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                })
                .verifyComplete();

        buyer.deleteBuyer(group, "0022929222").block();

        statisticServices.getPackageStatictis(PackageID.builder().group_id(group).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).status(UserPackageDetail.Status.CREATE).build())
                .as(StepVerifier::create)
                .consumeNextWith(data -> {
                    assertThat(data.getCost()).isEqualTo(0);
                    assertThat(data.getProfit()).isEqualTo(0);
                    assertThat(data.getRevenue()).isEqualTo(0);
                    assertThat(data.getPrice()).isEqualTo(1);
                    assertThat(data.getCount()).isEqualTo(1);
                    assertThat(data.getBuyer()).isEqualTo(1);
                    assertThat(data.getShip_price()).isEqualTo(20000);
                    assertThat(data.getDiscount_by_point()).isEqualTo(0);
                    assertThat(data.getDiscount_promotional()).isEqualTo(0);
                    assertThat(data.getAdditional_fee()).isEqualTo(0);
                    assertThat(data.getReturn_price()).isEqualTo(0);
                    assertThat(new DecimalFormat("0.0").format(data.getDiscount())).isEqualTo("10000.1");
                })
                .verifyComplete();

        statisticServices.getBenifitOfPaymentByDateStatictis(PackageID.builder().group_id(group).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).status(UserPackageDetail.Status.CREATE).build())
                .as(StepVerifier::create)
                .consumeNextWith(data -> {
                    assertThat(data.getReturn_price()).isEqualTo(0);
                    assertThat(data.getBenifit_by_date().size()).isEqualTo(1);
                    assertThat(data.getBenifit_by_date_transaction().size()).isEqualTo(0);
                    assertThat(data.getBenifit_by_category_transaction().size()).isEqualTo(0);


                    BenifitByDate benifitByDate = data.getBenifit_by_date().get(0);


                    assertThat(benifitByDate.getCost()).isEqualTo(0);
                    assertThat(benifitByDate.getProfit()).isEqualTo(0);
                    assertThat(benifitByDate.getRevenue()).isEqualTo(0);
                    assertThat(benifitByDate.getPrice()).isEqualTo(1);
                    assertThat(benifitByDate.getCount()).isEqualTo(1);
                    assertThat(benifitByDate.getBuyer()).isEqualTo(1);
                    assertThat(benifitByDate.getShip_price()).isEqualTo(20000);
                    assertThat(benifitByDate.getDiscount_by_point()).isEqualTo(0);
                    assertThat(benifitByDate.getDiscount_promotional()).isEqualTo(0);
                    assertThat(benifitByDate.getAdditional_fee()).isEqualTo(0);
                    assertThat(data.getReturn_price()).isEqualTo(0);
                    assertThat(new DecimalFormat("0.0").format(benifitByDate.getDiscount())).isEqualTo("10000.1");
                })
                .verifyComplete();

        statisticServices.getPackageStatictisByHour(PackageID.builder().group_id(group).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).status(UserPackageDetail.Status.CREATE).build())
                .sort(Comparator.comparing(BenifitByDateHour::getRevenue))
                .as(StepVerifier::create)
                .consumeNextWith(data -> {
                    assertThat(data.getCost()).isEqualTo(0);
                    assertThat(data.getProfit()).isEqualTo(0);
                    assertThat(data.getRevenue()).isEqualTo(0);
                    assertThat(data.getPrice()).isEqualTo(1);
                    assertThat(data.getCount()).isEqualTo(1);
                    assertThat(data.getBuyer()).isEqualTo(1);
                    assertThat(data.getShip_price()).isEqualTo(20000);
                    assertThat(data.getDiscount_promotional()).isEqualTo(0);
                    assertThat(data.getDiscount_by_point()).isEqualTo(0);
                    assertThat(data.getAdditional_fee()).isEqualTo(0);
                    assertThat(data.getReturn_price()).isEqualTo(0);
                    assertThat(new DecimalFormat("0.0").format(data.getDiscount())).isEqualTo("10000.1");
                })
                .verifyComplete();

        statisticServices.getBenifitOfPaymentByHourStatictis(PackageID.builder().group_id(group).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).status(UserPackageDetail.Status.CREATE).build())
                .as(StepVerifier::create)
                .consumeNextWith(data -> {
                    assertThat(data.getReturn_price()).isEqualTo(0);
                    assertThat(data.getBenifit_by_hour().size()).isEqualTo(1);
                    assertThat(data.getBenifit_by_hour_transaction().size()).isEqualTo(0);
                    assertThat(data.getBenifit_by_category_transaction().size()).isEqualTo(0);


                    BenifitByDateHour benifitByHour = data.getBenifit_by_hour().get(0);


                    assertThat(benifitByHour.getCost()).isEqualTo(0);
                    assertThat(benifitByHour.getProfit()).isEqualTo(0);
                    assertThat(benifitByHour.getRevenue()).isEqualTo(0);
                    assertThat(benifitByHour.getPrice()).isEqualTo(1);
                    assertThat(benifitByHour.getCount()).isEqualTo(1);
                    assertThat(benifitByHour.getBuyer()).isEqualTo(1);
                    assertThat(benifitByHour.getShip_price()).isEqualTo(20000);
                    assertThat(benifitByHour.getDiscount_promotional()).isEqualTo(0);
                    assertThat(benifitByHour.getDiscount_by_point()).isEqualTo(0);
                    assertThat(benifitByHour.getAdditional_fee()).isEqualTo(0);
                    assertThat(benifitByHour.getReturn_price()).isEqualTo(0);
                    assertThat(new DecimalFormat("0.0").format(benifitByHour.getDiscount())).isEqualTo("10000.1");
                })
                .verifyComplete();

        statisticServices.getPackageTotalStatictis(PackageID.builder().group_id(group).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).status(UserPackageDetail.Status.CREATE).build())
                .as(StepVerifier::create)
                .consumeNextWith(data -> {
                    assertThat(data.getCost()).isEqualTo(0);
                    assertThat(data.getProfit()).isEqualTo(0);
                    assertThat(data.getRevenue()).isEqualTo(0);
                    assertThat(data.getPrice()).isEqualTo(1);
                    assertThat(data.getCount()).isEqualTo(1);
                    assertThat(data.getBuyer()).isEqualTo(1);
                    assertThat(data.getShip_price()).isEqualTo(20000);
                    assertThat(new DecimalFormat("0.0").format(data.getDiscount())).isEqualTo("10000.1");
                })
                .verifyComplete();

        statisticServices.getProductBenifitStatictis(PackageID.builder().group_id(group).page(0).size(1000).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).status(UserPackageDetail.Status.CREATE).build())
                .sort(Comparator.comparing(BenifitByProduct::getRevenue))
                .as(StepVerifier::create)
                .verifyComplete();

        statisticServices.getCountCancelReturnStatictis(PackageID.builder().group_id(group).page(0).size(1000).from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).status(UserPackageDetail.Status.CREATE).build())
                .as(StepVerifier::create)
                .consumeNextWith(data -> {
                    assertThat(data.getCount_cancel()).isEqualTo(1);
                    assertThat(data.getCount_return()).isEqualTo(1);
                    assertThat(data.getRevenue_cancel()).isEqualTo(3);
                    assertThat(data.getRevenue_return()).isEqualTo(0);
                })
                .verifyComplete();

        beerAPI.getAllProductUnitOfIDs(group, Arrays.asList("sold_out", "123", "hide", "456"), Arrays.asList("", beerUnitsold_out2ID.get(), beerUnit2ID.get(), beerUnit4561ID.get(), "5555"))
                .sort(Comparator.comparing(ProductUnit::getProduct_second_id))
                .as(StepVerifier::create)
                .consumeNextWith(data -> {
                    assertThat(data.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                })
                .consumeNextWith(data -> {
                    assertThat(data.getProduct_unit_second_id()).isEqualTo(beerUnit4561ID.get());
                })
                .verifyComplete();

    }
}
