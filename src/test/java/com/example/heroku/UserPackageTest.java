package com.example.heroku;

import com.example.heroku.model.Buyer;
import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.request.beer.ProductPackage;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.PackageItemRemove;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.request.client.UserID;
import com.example.heroku.response.BuyerData;
import com.example.heroku.response.ProductInPackageResponse;
import com.example.heroku.services.UserPackage;
import com.example.heroku.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class UserPackageTest {
    @Autowired
    UserPackage userPackageAPI;

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

        userPackageAPI.GetMyPackage(UserID.builder().id("222222").page(0).size(1000).group_id(group).build())
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
                    listItem.sort(Comparator.comparingInt(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

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
                    listItem.sort(Comparator.comparingInt(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    assertThat(listItem.size()).isEqualTo(0);
                })
                .verifyComplete();


        productPackage = ProductPackage.builder()
                .group_id(group)
                .price(3)
                .device_id("222222")
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

                    listItem.sort(Comparator.comparingInt(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

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
                    listItem.sort(Comparator.comparingInt(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

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
                    listItem.sort(Comparator.comparingInt(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

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

        userPackageAPI.DeleteByBeerUnit(PackageItemRemove.builder().device_id("222222").unit_id(beerUnit1ID.get()).group_id(group).build()).blockLast();

        userPackageAPI.GetMyPackage(UserID.builder().id("222222").page(0).size(1000).group_id(group).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice).reversed())
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {
                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(1);

                    listItem.sort(Comparator.comparingInt(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

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
                    listItem.sort(Comparator.comparingInt(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    assertThat(listItem.size()).isEqualTo(0);
                })
                .consumeNextWith(userPackage -> {
                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(1);

                    listItem.sort(Comparator.comparingInt(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

                    ProductInPackageResponse item = listItem.get(0);
                    assertThat(item.getProduct_second_id()).isEqualTo("123");
                    assertThat(item.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(item.getNumber_unit()).isEqualTo(9);
                    assertThat(item.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(item.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();

        userPackageAPI.DeleteByUserID(UserID.builder().id("222222").page(0).size(1000).group_id(group).build()).blockLast();

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

                    listItem.sort(Comparator.comparingInt(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

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
                    listItem.sort(Comparator.comparingInt(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

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
                .ship_price(20000)
                .voucher("voucher_1d")
                .package_type("deliver")
                .progress("{}")
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
                    assertThat(userPackage.getNote()).isEqualTo("note here!!");
                    List<ProductInPackageResponse> listItem = userPackage.getItems();
                    assertThat(listItem.size()).isEqualTo(2);

                    listItem.sort(Comparator.comparingInt(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

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

                    listItem.sort(Comparator.comparingInt(com.example.heroku.response.ProductInPackageResponse::getNumber_unit));

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

        userPackageAPI.DeleteByUserID(UserID.builder().id("save_package").page(0).size(1000).group_id(group).build()).blockLast();

        userPackageAPI.GetMyPackage(UserID.builder().id("save_package").page(0).size(1000).group_id(group).build())
                .sort(Comparator.comparingDouble(com.example.heroku.response.PackageDataResponse::getPrice).reversed())
                .as(StepVerifier::create)
                .verifyComplete();

        userPackageAPI.GetPackage(PackageID.builder().group_id(group).device_id("save_package").package_id("save_pack").build())
                .as(StepVerifier::create)
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
                .status(UserPackageDetail.Status.CREATE)
                .area_id("area_1")
                .area_name("tang 1")
                .table_id("table_1")
                .table_name("ban 1")
                .payment(7868)
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
                                .discount_amount(19)
                                .discount_percent(10)
                                .note("note hhh")
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
                    assertThat(userPackage.getNote()).isEqualTo("notettt here!!");
                    assertThat(userPackage.getTable_id()).isEqualTo("table_1");
                    assertThat(userPackage.getTable_name()).isEqualTo("ban 1");
                    assertThat(userPackage.getArea_id()).isEqualTo("area_1");
                    assertThat(userPackage.getArea_name()).isEqualTo("tang 1");
                    assertThat(userPackage.getPayment()).isEqualTo(7868);
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

                    listItem.sort(Comparator.comparingInt(com.example.heroku.response.ProductInPackageResponse::getNumber_unit));

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

                    listItem.sort(Comparator.comparingInt(com.example.heroku.response.ProductInPackageResponse::getNumber_unit));

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
                    listItem.sort(Comparator.comparingInt(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

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

                    listItem.sort(Comparator.comparingInt(com.example.heroku.response.ProductInPackageResponse::getNumber_unit).reversed());

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

        buyer.deleteBuyer(group, "0022929222").block();


    }
}
