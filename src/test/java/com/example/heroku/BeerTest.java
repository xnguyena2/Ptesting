package com.example.heroku;

import com.example.heroku.model.Beer;
import com.example.heroku.model.BeerUnit;
import com.example.heroku.request.beer.BeerInfo;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.datetime.NgbDateStruct;
import com.example.heroku.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class BeerTest {

    com.example.heroku.services.Beer beerAPI;

    public void saveBeerTest() {

        beerAPI.CreateBeer(
                BeerInfo
                        .builder()
                        .beerUnit(new BeerUnit[]{
                                BeerUnit.builder().beer("123").name("thung").build(),
                                BeerUnit.builder().beer("123").name("lon").build()
                        })
                        .beer(Beer
                                .builder()
                                .category(Beer.Category.CRAB)
                                .name("beer tiger")
                                .beer_second_id("123")
                                .build()
                                .AutoFill()
                        )
                        .build()
        ).blockLast();

        AtomicReference<String> beerUnit1ID = new AtomicReference<String>();
        AtomicReference<String> beerUnit2ID = new AtomicReference<String>();

        beerAPI.CreateBeer(
                BeerInfo
                        .builder()
                        .beerUnit(new BeerUnit[]{
                                BeerUnit
                                        .builder()
                                        .beer("456")
                                        .name("thung")
                                        .build(),
                                BeerUnit
                                        .builder()
                                        .beer("456")
                                        .name("lon")
                                        .build()
                        })
                        .beer(Beer
                                .builder()
                                .category(Beer.Category.CRAB)
                                .detail("Đây là beer tiger có nồn độ cồn cao nên chú ý khi sử dụng:\n" +
                                        "- bia thơm ngon\n" +
                                        "- bia nhập ngoại\n" +
                                        "- bia sản xuất từ hà lan")
                                .name("beer tiger")
                                .beer_second_id("456").build()
                                .AutoFill()
                        )
                        .build()
        )
                .as(StepVerifier::create)
                .consumeNextWith(beerUnit -> {
                    if (beerUnit.getName().equals("lon")) {
                        beerUnit1ID.set(beerUnit.getBeer_unit_second_id());
                    } else {
                        beerUnit2ID.set(beerUnit.getBeer_unit_second_id());
                    }
                    assertThat(beerUnit.getBeer_unit_second_id()).isNotNull();

                })
                .consumeNextWith(beerUnit -> {
                    if (beerUnit.getName().equals("lon")) {
                        beerUnit1ID.set(beerUnit.getBeer_unit_second_id());
                    } else {
                        beerUnit2ID.set(beerUnit.getBeer_unit_second_id());
                    }
                    assertThat(beerUnit.getBeer_unit_second_id()).isNotNull();
                })
                .verifyComplete();


        beerAPI.CreateBeer(
                BeerInfo
                        .builder()
                        .beerUnit(new BeerUnit[]{
                                BeerUnit
                                        .builder()
                                        .beer_unit_second_id(beerUnit1ID.get())
                                        .beer("456")
                                        .price(10)
                                        .weight(0.3f)
                                        .discount(10)
                                        .date_expire(Timestamp.valueOf("2021-03-31 20:45:00"))
                                        .name("thung")
                                        .build(),
                                BeerUnit
                                        .builder()
                                        .beer_unit_second_id(beerUnit2ID.get())
                                        .beer("456")
                                        .price(10)
                                        .weight(0.3f)
                                        .discount(10)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("lon")
                                        .build()
                        })
                        .beer(Beer
                                .builder()
                                .category(Beer.Category.CRAB)
                                .detail("Đây là beer tiger có nồn độ cồn cao nên chú ý khi sử dụng:\n" +
                                        "- bia thơm ngon\n" +
                                        "- bia nhập ngoại\n" +
                                        "- bia sản xuất từ hà lan")
                                .name("beer tiger")
                                .beer_second_id("456").build()
                                .AutoFill()
                        )
                        .build()
        )
                .sort(Comparator.comparing(BeerUnit::getName))
                .as(StepVerifier::create)
                .consumeNextWith(beerUnit -> {
                    assertThat(beerUnit.getBeer_unit_second_id()).isEqualTo(beerUnit2ID.get());
                })
                .consumeNextWith(beerUnit -> {
                    assertThat(beerUnit.getBeer_unit_second_id()).isEqualTo(beerUnit1ID.get());
                })
                .verifyComplete();

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
                                assertThat(beerUnit.getName()).isEqualTo("lon");
                                assertThat(beerUnit.getDate_expire()).isEqualTo(null);
                            })
                            .consumeNextWith(beerUnit -> {
                                assertThat(beerUnit.getName()).isEqualTo("thung");
                                assertThat(beerUnit.getDate_expire()).isEqualTo(null);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

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
                                assertThat(beerUnit.getName()).isEqualTo("lon");
                                assertThat(NgbDateStruct.FromTimestamp(beerUnit.getDate_expire())).isEqualTo(NgbDateStruct.FromTimestamp(new Timestamp(new Date().getTime())));
                            })
                            .consumeNextWith(beerUnit -> {
                                assertThat(beerUnit.getName()).isEqualTo("thung");
                                assertThat(NgbDateStruct.FromTimestamp(beerUnit.getDate_expire())).isEqualTo(NgbDateStruct.FromTimestamp(Timestamp.valueOf("2021-03-31 20:45:00")));
                                assertThat(Util.getInstance().DiffirentDays(beerUnit.getDate_expire(), new Timestamp(new Date().getTime())) >= 0).isEqualTo(false);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("hà").page(0).size(100).filter(SearchQuery.Filter.NAME_DESC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("hà").page(0).size(100).filter(SearchQuery.Filter.NAME_ASC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("tiger").page(0).size(100).filter(SearchQuery.Filter.PRICE_ASC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(2);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("tiger").page(0).size(100).filter(SearchQuery.Filter.PRICE_DESC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(2);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("tiger").page(0).size(100).filter(SearchQuery.Filter.CREATE_ASC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(2);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("tiger").page(0).size(100).filter(SearchQuery.Filter.CREATE_DESC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(2);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("tiger").page(0).size(100).filter(SearchQuery.Filter.SOLD_NUM.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(2);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("ha%oai").page(0).size(100).filter(SearchQuery.Filter.CREATE_ASC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bia&ha&lan").page(0).size(100).filter(SearchQuery.Filter.CREATE_ASC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bia&ha&lan").page(0).size(100).filter(SearchQuery.Filter.CREATE_DESC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bi:*&ngo:*&nhap:*").page(0).size(100).filter(SearchQuery.Filter.NAME_ASC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bi:*&ngo:*&nhap:*").page(0).size(100).filter(SearchQuery.Filter.NAME_DESC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bi:*&ngo:*&nhap:*").page(0).size(100).filter(SearchQuery.Filter.PRICE_ASC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bi:*&ngo:*&nhap:*").page(0).size(100).filter(SearchQuery.Filter.PRICE_DESC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bi:*&ngo:*&nhap:*").page(0).size(100).filter(SearchQuery.Filter.CREATE_ASC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bi:*&ngo:*&nhap:*").page(0).size(100).filter(SearchQuery.Filter.CREATE_DESC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bi:*&ngo:*&nhap:*").page(0).size(100).filter(SearchQuery.Filter.SOLD_NUM.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Flux.just(resultWithCount.GetResultAsArray())
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.GetAllBeer(SearchQuery.builder().page(0).size(100).build())
                .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getBeerSecondID()).isEqualTo("123");
                    assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                })
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                    assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeer(SearchQuery.builder().page(0).size(100).filter(SearchQuery.Filter.DEFAULT.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(2);
                    Flux.just(searchResult.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                                assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeer(SearchQuery.builder().page(0).size(100).filter(SearchQuery.Filter.PRICE_DESC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(2);
                    Flux.just(searchResult.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                                assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeer(SearchQuery.builder().page(0).size(100).filter(SearchQuery.Filter.PRICE_ASC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(2);
                    Flux.just(searchResult.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                                assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeer(SearchQuery.builder().page(0).size(100).filter(SearchQuery.Filter.NAME_DESC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(2);
                    Flux.just(searchResult.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                                assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeer(SearchQuery.builder().page(0).size(100).filter(SearchQuery.Filter.CREATE_ASC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(2);
                    Flux.just(searchResult.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                                assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeer(SearchQuery.builder().page(0).size(100).filter(SearchQuery.Filter.CREATE_DESC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(2);
                    Flux.just(searchResult.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                                assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeer(SearchQuery.builder().page(0).size(100).filter(SearchQuery.Filter.SOLD_NUM.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(2);
                    Flux.just(searchResult.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                                assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeer(SearchQuery.builder().page(0).size(100).filter(SearchQuery.Filter.NAME_ASC.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(2);
                    Flux.just(searchResult.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                                assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeerByCategory(SearchQuery.builder().page(0).size(100).filter(SearchQuery.Filter.PRICE_ASC.getName()).query(Beer.Category.CRAB.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(2);
                    Flux.just(searchResult.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                                assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeerByCategory(SearchQuery.builder().page(0).size(100).filter(SearchQuery.Filter.PRICE_DESC.getName()).query(Beer.Category.CRAB.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(2);
                    Flux.just(searchResult.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                                assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeerByCategory(SearchQuery.builder().page(0).size(100).filter(SearchQuery.Filter.NAME_ASC.getName()).query(Beer.Category.CRAB.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(2);
                    Flux.just(searchResult.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                                assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeerByCategory(SearchQuery.builder().page(0).size(100).filter(SearchQuery.Filter.NAME_DESC.getName()).query(Beer.Category.CRAB.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(2);
                    Flux.just(searchResult.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                                assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeerByCategory(SearchQuery.builder().page(0).size(100).filter(SearchQuery.Filter.CREATE_DESC.getName()).query(Beer.Category.CRAB.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(2);
                    Flux.just(searchResult.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                                assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeerByCategory(SearchQuery.builder().page(0).size(100).filter(SearchQuery.Filter.CREATE_ASC.getName()).query(Beer.Category.CRAB.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(2);
                    Flux.just(searchResult.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                                assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeerByCategory(SearchQuery.builder().page(0).size(100).filter(SearchQuery.Filter.SOLD_NUM.getName()).query(Beer.Category.CRAB.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(2);
                    Flux.just(searchResult.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                                assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeerByCategory(SearchQuery.builder().page(0).size(100).filter(SearchQuery.Filter.DEFAULT.getName()).query(Beer.Category.CRAB.getName()).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(2);
                    Flux.just(searchResult.GetResultAsArray())
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                                assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();



        beerAPI.CreateBeer(
                BeerInfo
                        .builder()
                        .beerUnit(new BeerUnit[]{
                                BeerUnit
                                        .builder()
                                        .beer_unit_second_id(beerUnit1ID.get())
                                        .beer("444")
                                        .price(10)
                                        .weight(0.3f)
                                        .discount(10)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("thung")
                                        .build(),
                                BeerUnit
                                        .builder()
                                        .beer_unit_second_id(beerUnit2ID.get())
                                        .beer("444")
                                        .price(10)
                                        .weight(0.3f)
                                        .discount(10)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("lon")
                                        .build()
                        })
                        .beer(Beer
                                .builder()
                                .category(Beer.Category.CRAB)
                                .detail("Đây là beer tiger có nồn độ cồn cao nên chú ý khi sử dụng:\n" +
                                        "- bia thơm ngon\n" +
                                        "- bia nhập ngoại\n" +
                                        "- bia sản xuất từ hà lan")
                                .name("beer tiger")
                                .beer_second_id("444").build()
                                .AutoFill()
                        )
                        .build()
        ).blockLast();

        this.beerAPI.GetBeerByID("444")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getBeer().getBeer_second_id()).isEqualTo("444");
                })
                .verifyComplete();

        this.beerAPI.DeleteBeerByID("444").block();

        this.beerAPI.GetBeerByID("444")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    private BeerInfo createTestBeer(int idi, int price) {
        String id = idi + "";
        Beer.Category category = Beer.Category.CRAB;
        if (idi < 24) {
            category = Beer.Category.SHRIMP;
        } else if (idi < 50) {
            category = Beer.Category.SQUID;
        } else if (idi < 60) {
            category = Beer.Category.OYSTER;
        } else if (idi < 75) {
            category = Beer.Category.HOLOTHURIAN;
        }
        return
                BeerInfo
                        .builder()
                        .beerUnit(new BeerUnit[]{
                                BeerUnit
                                        .builder()
                                        .beer(id)
                                        .price(price)
                                        .weight(0.3f)
                                        .discount(10)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("thung")
                                        .build(),
                                BeerUnit
                                        .builder()
                                        .beer(id)
                                        .price(10000)
                                        .weight(0.3f)
                                        .discount(10)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("lon")
                                        .build()
                        })
                        .beer(Beer
                                .builder()
                                .category(category)
                                .detail("nhưng Milan khởi đầu ấn tượng. Với Mandzukic lần đầu đá chính, cùng sự hỗ trợ của bộ ba Castillejo, Krunic, Rebic, đội nhì bảng Serie A liên tục gây sóng gió về phía cầu môn Sao Đỏ. Chỉ trong 13 phút đầu, Milan")
                                .name(id + " beer tiger")
                                .beer_second_id(id).build()
                                .AutoFill()
                        )
                        .build();
    }

    public void createPeerTest() throws InterruptedException {

        Thread[] threads = new Thread[100];

        for (int i = 0; i < threads.length; i++) {
            final int i1 = i;
            threads[i] = new Thread(() -> {
                System.out.println("Thread Running: " + (i1 + 1));
                BeerInfo template = createTestBeer(i1, i1 * 1000 + 5000);
                beerAPI.CreateBeer(template).blockLast();
            });
            //Thread.sleep(5000);
            threads[i].start();
        }
        for (Thread t :
                threads) {
            t.join();
        }
    }
}