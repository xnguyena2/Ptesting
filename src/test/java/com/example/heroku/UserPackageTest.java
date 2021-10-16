package com.example.heroku;

import com.example.heroku.model.Beer;
import com.example.heroku.model.BeerUnit;
import com.example.heroku.request.beer.BeerPackage;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.BeerUnitDelete;
import com.example.heroku.request.client.UserID;
import com.example.heroku.services.UserPackage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class UserPackageTest {
    @Autowired
    UserPackage userPackageAPI;

    com.example.heroku.services.Beer beerAPI;

    public void TestUserPackage() {
        BeerPackage beerPackage = BeerPackage.builder().build();
        userPackageAPI.AddBeerToPackage(beerPackage)
                .block();


        AtomicReference<String> beerUnitsold_out1ID = new AtomicReference<String>();
        AtomicReference<String> beerUnitsold_out2ID = new AtomicReference<String>();
        this.beerAPI.GetBeerByID("sold_out")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getBeer().getBeer_second_id()).isEqualTo("sold_out");
                    assertThat(beerInfo.getBeer().getCategory()).isEqualTo(Beer.Category.CRAB);
                    assertThat(beerInfo.getBeerUnit().length).isEqualTo(2);
                    Flux.just(beerInfo.getBeerUnit())
                            .sort(Comparator.comparing(BeerUnit::getName))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    beerUnitsold_out1ID.set(beerUnit.getBeer_unit_second_id());
                                } else {
                                    beerUnitsold_out2ID.set(beerUnit.getBeer_unit_second_id());
                                }
                            })
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    beerUnitsold_out1ID.set(beerUnit.getBeer_unit_second_id());
                                } else {
                                    beerUnitsold_out2ID.set(beerUnit.getBeer_unit_second_id());
                                }
                            })
                            .verifyComplete();
                })
                .verifyComplete();


        AtomicReference<String> beerUnit1ID = new AtomicReference<String>();
        AtomicReference<String> beerUnit2ID = new AtomicReference<String>();
        this.beerAPI.GetBeerByID("123")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getBeer().getBeer_second_id()).isEqualTo("123");
                    assertThat(beerInfo.getBeer().getCategory()).isEqualTo(Beer.Category.CRAB);
                    assertThat(beerInfo.getBeerUnit().length).isEqualTo(2);
                    Flux.just(beerInfo.getBeerUnit())
                            .sort(Comparator.comparing(BeerUnit::getName))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    beerUnit1ID.set(beerUnit.getBeer_unit_second_id());
                                } else {
                                    beerUnit2ID.set(beerUnit.getBeer_unit_second_id());
                                }
                            })
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    beerUnit1ID.set(beerUnit.getBeer_unit_second_id());
                                } else {
                                    beerUnit2ID.set(beerUnit.getBeer_unit_second_id());
                                }
                            })
                            .verifyComplete();
                })
                .verifyComplete();


        AtomicReference<String> beerUnit4561ID = new AtomicReference<String>();
        AtomicReference<String> beerUnit4562ID = new AtomicReference<String>();
        this.beerAPI.GetBeerByID("456")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getBeer().getBeer_second_id()).isEqualTo("456");
                    assertThat(beerInfo.getBeer().getCategory()).isEqualTo(Beer.Category.CRAB);
                    assertThat(beerInfo.getBeerUnit().length).isEqualTo(2);
                    Flux.just(beerInfo.getBeerUnit())
                            .sort(Comparator.comparing(BeerUnit::getName))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    beerUnit4561ID.set(beerUnit.getBeer_unit_second_id());
                                } else {
                                    beerUnit4562ID.set(beerUnit.getBeer_unit_second_id());
                                }
                            })
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    beerUnit4561ID.set(beerUnit.getBeer_unit_second_id());
                                } else {
                                    beerUnit4562ID.set(beerUnit.getBeer_unit_second_id());
                                }
                            })
                            .verifyComplete();
                })
                .verifyComplete();


        beerPackage = BeerPackage.builder()
                .beerID("sold_out")
                .deviceID("soldoutttt")
                .beerUnits(new BeerPackage.BeerUnit[]{
                        BeerPackage.BeerUnit.builder()
                                .beerUnitID(beerUnitsold_out1ID.get())
                                .numberUnit(100)
                                .build(),
                        BeerPackage.BeerUnit.builder()
                                .beerUnitID(beerUnitsold_out2ID.get())
                                .numberUnit(9)
                                .build()
                })
                .build();
        userPackageAPI.AddBeerToPackage(beerPackage)
                .block();


        beerPackage = BeerPackage.builder()
                .beerID("123")
                .deviceID("soldoutttt")
                .beerUnits(new BeerPackage.BeerUnit[]{
                        BeerPackage.BeerUnit.builder()
                                .beerUnitID(beerUnit1ID.get())
                                .numberUnit(100)
                                .build(),
                        BeerPackage.BeerUnit.builder()
                                .beerUnitID(beerUnit2ID.get())
                                .numberUnit(9)
                                .build()
                })
                .build();
        userPackageAPI.AddBeerToPackage(beerPackage)
                .block();


        beerPackage = BeerPackage.builder()
                .beerID("123")
                .deviceID("222222")
                .beerUnits(new BeerPackage.BeerUnit[]{
                        BeerPackage.BeerUnit.builder()
                                .beerUnitID(beerUnit1ID.get())
                                .numberUnit(100)
                                .build(),
                        BeerPackage.BeerUnit.builder()
                                .beerUnitID(beerUnit2ID.get())
                                .numberUnit(9)
                                .build()
                })
                .build();
        userPackageAPI.AddBeerToPackage(beerPackage)
                .block();

        beerPackage = BeerPackage.builder()
                .beerID("456")
                .deviceID("222222")
                .beerUnits(new BeerPackage.BeerUnit[]{
                        BeerPackage.BeerUnit.builder()
                                .beerUnitID("3")
                                .numberUnit(100)
                                .build(),
                        BeerPackage.BeerUnit.builder()
                                .beerUnitID("3")
                                .numberUnit(9)
                                .build()
                })
                .build();
        userPackageAPI.AddBeerToPackage(beerPackage)
                .block();

        userPackageAPI.GetMyPackage(UserID.builder().id("222222").page( 0).size( 1000).build())
                .sort(Comparator.comparingInt(com.example.heroku.model.UserPackage::getNumber_unit).reversed())
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    assertThat(userPackage.getBeer_id()).isEqualTo("123");
                    assertThat(userPackage.getBeer_unit()).isEqualTo(beerUnit1ID.get());
                    assertThat(userPackage.getNumber_unit()).isEqualTo(100);
                    assertThat(userPackage.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(userPackage.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .consumeNextWith(userPackage -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    assertThat(userPackage.getBeer_id()).isEqualTo("123");
                    assertThat(userPackage.getBeer_unit()).isEqualTo(beerUnit2ID.get());
                    assertThat(userPackage.getNumber_unit()).isEqualTo(9);
                    assertThat(userPackage.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(userPackage.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();

        beerPackage = BeerPackage.builder()
                .beerID("456")
                .deviceID("222222")
                .beerUnits(new BeerPackage.BeerUnit[]{
                        BeerPackage.BeerUnit.builder()
                                .beerUnitID(beerUnit4561ID.get())
                                .numberUnit(100)
                                .build(),
                        BeerPackage.BeerUnit.builder()
                                .beerUnitID(beerUnit4561ID.get())
                                .numberUnit(9)
                                .build()
                })
                .build();
        userPackageAPI.AddBeerToPackage(beerPackage)
                .block();

        userPackageAPI.GetMyPackage(UserID.builder().id("222222").page( 0).size( 1000).build())
                .sort(Comparator.comparingInt(com.example.heroku.model.UserPackage::getNumber_unit).reversed())
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    assertThat(userPackage.getBeer_id()).isEqualTo("456");
                    assertThat(userPackage.getBeer_unit()).isEqualTo(beerUnit4561ID.get());
                    assertThat(userPackage.getNumber_unit()).isEqualTo(109);
                    assertThat(userPackage.getBeerSubmitData().getBeerSecondID()).isEqualTo("456");
                    assertThat(userPackage.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .consumeNextWith(userPackage -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    assertThat(userPackage.getBeer_id()).isEqualTo("123");
                    assertThat(userPackage.getBeer_unit()).isEqualTo(beerUnit1ID.get());
                    assertThat(userPackage.getNumber_unit()).isEqualTo(100);
                    assertThat(userPackage.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(userPackage.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .consumeNextWith(userPackage -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(userPackage));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    assertThat(userPackage.getBeer_id()).isEqualTo("123");
                    assertThat(userPackage.getBeer_unit()).isEqualTo(beerUnit2ID.get());
                    assertThat(userPackage.getNumber_unit()).isEqualTo(9);
                    assertThat(userPackage.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(userPackage.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();

        userPackageAPI.DeleteByBeerUnit(BeerUnitDelete.builder().id(beerUnit1ID.get()).build()).blockLast();

        userPackageAPI.GetMyPackage(UserID.builder().id("222222").page( 0).size( 1000).build())
                .sort(Comparator.comparingInt(com.example.heroku.model.UserPackage::getNumber_unit).reversed())
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {
                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    assertThat(userPackage.getBeer_id()).isEqualTo("456");
                    assertThat(userPackage.getBeer_unit()).isEqualTo(beerUnit4561ID.get());
                    assertThat(userPackage.getNumber_unit()).isEqualTo(109);
                    assertThat(userPackage.getBeerSubmitData().getBeerSecondID()).isEqualTo("456");
                    assertThat(userPackage.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .consumeNextWith(userPackage -> {
                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    assertThat(userPackage.getBeer_id()).isEqualTo("123");
                    assertThat(userPackage.getBeer_unit()).isEqualTo(beerUnit2ID.get());
                    assertThat(userPackage.getNumber_unit()).isEqualTo(9);
                    assertThat(userPackage.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(userPackage.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();

        userPackageAPI.DeleteByUserID(UserID.builder().id("222222").page( 0).size( 1000).build()).blockLast();

        userPackageAPI.GetMyPackage(UserID.builder().id("222222").page( 0).size( 1000).build())
                .sort(Comparator.comparingInt(com.example.heroku.model.UserPackage::getNumber_unit).reversed())
                .as(StepVerifier::create)
                .verifyComplete();
    }
}
