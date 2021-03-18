package com.example.heroku;

import com.example.heroku.request.beer.BeerPackage;
import com.example.heroku.request.beer.BeerUnitDelete;
import com.example.heroku.request.client.UserID;
import com.example.heroku.services.UserPackage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.Comparator;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class UserPackageTest {
    @Autowired
    UserPackage userPackageAPI;

    public void TestUserPackage() {
        BeerPackage beerPackage = BeerPackage.builder().build();
        userPackageAPI.AddBeerToPackage(beerPackage)
                .block();


        beerPackage = BeerPackage.builder()
                .beerID("123")
                .deviceID("222222")
                .beerUnits(new BeerPackage.BeerUnit[]{
                        BeerPackage.BeerUnit.builder()
                                .beerUnitID("1")
                                .numberUnit(100)
                                .build(),
                        BeerPackage.BeerUnit.builder()
                                .beerUnitID("2")
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
                    assertThat(userPackage.getBeer_id()).isEqualTo("456");
                    assertThat(userPackage.getBeer_unit()).isEqualTo("3");
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
                    assertThat(userPackage.getBeer_unit()).isEqualTo("1");
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
                    assertThat(userPackage.getBeer_unit()).isEqualTo("2");
                    assertThat(userPackage.getNumber_unit()).isEqualTo(9);
                    assertThat(userPackage.getBeerSubmitData().getBeerSecondID()).isEqualTo("123");
                    assertThat(userPackage.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .verifyComplete();

        userPackageAPI.DeleteByBeerUnit(BeerUnitDelete.builder().id("1").build()).blockLast();

        userPackageAPI.GetMyPackage(UserID.builder().id("222222").page( 0).size( 1000).build())
                .sort(Comparator.comparingInt(com.example.heroku.model.UserPackage::getNumber_unit).reversed())
                .as(StepVerifier::create)
                .consumeNextWith(userPackage -> {
                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    assertThat(userPackage.getBeer_id()).isEqualTo("456");
                    assertThat(userPackage.getBeer_unit()).isEqualTo("3");
                    assertThat(userPackage.getNumber_unit()).isEqualTo(109);
                    assertThat(userPackage.getBeerSubmitData().getBeerSecondID()).isEqualTo("456");
                    assertThat(userPackage.getBeerSubmitData().getListUnit().length).isEqualTo(1);
                })
                .consumeNextWith(userPackage -> {
                    assertThat(userPackage.getDevice_id()).isEqualTo("222222");
                    assertThat(userPackage.getBeer_id()).isEqualTo("123");
                    assertThat(userPackage.getBeer_unit()).isEqualTo("2");
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
