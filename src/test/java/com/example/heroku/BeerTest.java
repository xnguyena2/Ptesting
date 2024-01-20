package com.example.heroku;

import com.example.heroku.model.Product;
import com.example.heroku.model.ProductUnit;
import com.example.heroku.request.beer.BeerInfo;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.datetime.NgbDateStruct;
import com.example.heroku.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class BeerTest {

    com.example.heroku.services.Beer beerAPI;

    String group;

    public void saveBeerTest() {

        beerAPI.CreateBeer(
                BeerInfo
                        .builder()
                        .productUnit(new ProductUnit[]{
                                ProductUnit.builder().product_second_id("sold_out").name("thung").status(ProductUnit.Status.SOLD_OUT).group_id(group).build(),
                                ProductUnit.builder().product_second_id("sold_out").name("lon").group_id(group).build()
                        })
                        .product(Product
                                .builder()
                                .category(Category.CRAB.getName())
                                .name("beer tiger(sold out)")
                                .product_second_id("sold_out")
                                .status(Product.Status.SOLD_OUT)
                                .group_id(group)
                                .build()
                                .AutoFill()
                        )
                        .build()
        ).block();

        AtomicReference<String> beerUnit1231ID = new AtomicReference<String>();
        AtomicReference<String> beerUnit1232ID = new AtomicReference<String>();

        AtomicReference<String> beerUnit4561ID = new AtomicReference<String>();
        AtomicReference<String> beerUnit4562ID = new AtomicReference<String>();

        beerAPI.CreateBeer(
                BeerInfo
                        .builder()
                        .productUnit(new ProductUnit[]{
                                ProductUnit.builder().product_second_id("123").name("thung").group_id(group).wholesale_number(34).wholesale_price(1233).build(),
                                ProductUnit.builder().product_second_id("123").name("lon").group_id(group).wholesale_number(43).wholesale_price(3321).build()
                        })
                        .product(Product
                                .builder()
                                .category(Category.CRAB.getName())
                                .name("beer tiger")
                                .upc("343434")
                                .sku("76767676")
                                .product_second_id("123")
                                .group_id(group)
                                .build()
                                .AutoFill()
                        )
                        .build()
        )
                .as(StepVerifier::create)
                .consumeNextWith(beerSubmitData -> {
                    BeerSubmitData.BeerUnit[] productUnitList = beerSubmitData.getListUnit();
                    BeerSubmitData.BeerUnit f = productUnitList[0];
                    BeerSubmitData.BeerUnit s = productUnitList[1];

                    if (f.getName().equals("lon")) {
                        beerUnit1231ID.set(f.getBeer_unit_second_id());
                        beerUnit1232ID.set(s.getBeer_unit_second_id());
                    } else {
                        beerUnit1232ID.set(f.getBeer_unit_second_id());
                        beerUnit1231ID.set(s.getBeer_unit_second_id());
                    }
                    assertThat(f.getBeer_unit_second_id()).isNotNull();
                    assertThat(s.getBeer_unit_second_id()).isNotNull();

                })
                .verifyComplete();

        beerAPI.CreateBeer(
                BeerInfo
                        .builder()
                        .productUnit(new ProductUnit[]{
                                ProductUnit
                                        .builder()
                                        .product_second_id("456")
                                        .name("thung")
                                        .group_id(group)
                                        .build(),
                                ProductUnit
                                        .builder()
                                        .product_second_id("456")
                                        .name("lon")
                                        .group_id(group)
                                        .build()
                        })
                        .product(Product
                                .builder()
                                .category(Category.CRAB.getName())
                                .detail("Đây là beer tiger có nồn độ cồn cao nên chú ý khi sử dụng:\n" +
                                        "- bia thơm ngon\n" +
                                        "- bia nhập ngoại\n" +
                                        "- bia sản xuất từ hà lan")
                                .name("beer tiger")
                                .product_second_id("456")
                                .group_id(group)
                                .build()
                                .AutoFill()
                        )
                        .build()
        )
                .as(StepVerifier::create)
                .consumeNextWith(beerSubmitData -> {
                    BeerSubmitData.BeerUnit[] productUnitList = beerSubmitData.getListUnit();
                    BeerSubmitData.BeerUnit f = productUnitList[0];
                    BeerSubmitData.BeerUnit s = productUnitList[1];

                    if (f.getName().equals("lon")) {
                        beerUnit4561ID.set(f.getBeer_unit_second_id());
                        beerUnit4562ID.set(s.getBeer_unit_second_id());
                    } else {
                        beerUnit4562ID.set(f.getBeer_unit_second_id());
                        beerUnit4561ID.set(s.getBeer_unit_second_id());
                    }
                    assertThat(f.getBeer_unit_second_id()).isNotNull();
                    assertThat(s.getBeer_unit_second_id()).isNotNull();

                })
                .verifyComplete();


        beerAPI.CreateBeer(
                BeerInfo
                        .builder()
                        .productUnit(new ProductUnit[]{
                                ProductUnit
                                        .builder()
                                        .product_unit_second_id(beerUnit4561ID.get())
                                        .product_second_id("456")
                                        .price(10)
                                        .weight(0.3f)
                                        .discount(10)
                                        .date_expire(Timestamp.valueOf("2021-03-31 20:45:00"))
                                        .name("thung")
                                        .group_id(group)
                                        .build(),
                                ProductUnit
                                        .builder()
                                        .product_unit_second_id(beerUnit4562ID.get())
                                        .product_second_id("456")
                                        .price(10)
                                        .weight(0.3f)
                                        .discount(10)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("lon")
                                        .group_id(group)
                                        .build()
                        })
                        .product(Product
                                .builder()
                                .category(Category.CRAB.getName())
                                .detail("Đây là beer tiger có nồn độ cồn cao nên chú ý khi sử dụng:\n" +
                                        "- bia thơm ngon\n" +
                                        "- bia nhập ngoại\n" +
                                        "- bia sản xuất từ hà lan")
                                .name("beer tiger")
                                .product_second_id("456")
                                .group_id(group)
                                .build()
                                .AutoFill()
                        )
                        .build()
        )
                .as(StepVerifier::create)
                .consumeNextWith(beerSubmitData -> {
                    BeerSubmitData.BeerUnit[] productUnitList = beerSubmitData.getListUnit();
                    BeerSubmitData.BeerUnit f = productUnitList[0];
                    BeerSubmitData.BeerUnit s = productUnitList[1];
                    if(f.getName().equals("lon")){
                        assertThat(f.getBeer_unit_second_id()).isEqualTo(beerUnit4562ID.get());
                        assertThat(s.getBeer_unit_second_id()).isEqualTo(beerUnit4561ID.get());
                    } else {
                        assertThat(f.getBeer_unit_second_id()).isEqualTo(beerUnit4561ID.get());
                        assertThat(s.getBeer_unit_second_id()).isEqualTo(beerUnit4562ID.get());
                    }
                })
                .verifyComplete();


        beerAPI.CreateBeer(
                        BeerInfo
                                .builder()
                                .productUnit(new ProductUnit[]{
                                        ProductUnit
                                                .builder()
                                                .product_second_id("hide")
                                                .price(10)
                                                .weight(0.3f)
                                                .discount(10)
                                                .date_expire(Timestamp.valueOf("2021-03-31 20:45:00"))
                                                .name("thung")
                                                .group_id(group)
                                                .build(),
                                        ProductUnit
                                                .builder()
                                                .product_second_id("hide")
                                                .price(10)
                                                .weight(0.3f)
                                                .discount(10)
                                                .date_expire(new Timestamp(new Date().getTime()))
                                                .name("lon")
                                                .group_id(group)
                                                .build()
                                })
                                .product(Product
                                        .builder()
                                        .category(Category.CRAB.getName())
                                        .detail("Đây là beer tiger có nồn độ cồn cao nên chú ý khi sử dụng:\n" +
                                                "- bia thơm ngon\n" +
                                                "- bia nhập ngoại\n" +
                                                "- bia sản xuất từ hà lan")
                                        .name("beer tiger")
                                        .status(Product.Status.HIDE)
                                        .group_id(group)
                                        .product_second_id("hide")
                                        .build()
                                        .AutoFill()
                                )
                                .build()
                )
                .as(StepVerifier::create)
                .consumeNextWith(beerSubmitData -> {
                    assertThat(beerSubmitData.getListUnit().length).isEqualTo(2);
                })
                .verifyComplete();

        this.beerAPI.GetBeerByID(group, "123")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getProduct().getProduct_second_id()).isEqualTo("123");
                    assertThat(beerInfo.getProduct().getUpc()).isEqualTo("343434");
                    assertThat(beerInfo.getProduct().getSku()).isEqualTo("76767676");
                    assertThat(beerInfo.getProduct().getCategory()).isEqualTo(Category.CRAB.getName());
                    assertThat(beerInfo.getProduct().getMeta_search()).isEqualTo("beer tiger 76767676 343434");
                    assertThat(beerInfo.getProductUnit().length).isEqualTo(2);
                    Flux.just(beerInfo.getProductUnit())
                            .sort(Comparator.comparing(ProductUnit::getName))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerUnit -> {
                                assertThat(beerUnit.getName()).isEqualTo("lon");
                                assertThat(beerUnit.getWholesale_number()).isEqualTo(43);
                                assertThat(beerUnit.getWholesale_price()).isEqualTo(3321);
                                assertThat(beerUnit.getDate_expire()).isEqualTo(null);
                            })
                            .consumeNextWith(beerUnit -> {
                                assertThat(beerUnit.getName()).isEqualTo("thung");
                                assertThat(beerUnit.getWholesale_number()).isEqualTo(34);
                                assertThat(beerUnit.getWholesale_price()).isEqualTo(1233);
                                assertThat(beerUnit.getDate_expire()).isEqualTo(null);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.GetBeerByID(group, "456")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getProduct().getProduct_second_id()).isEqualTo("456");
                    assertThat(beerInfo.getProduct().getUpc()).isEqualTo(null);
                    assertThat(beerInfo.getProduct().getSku()).isEqualTo(null);
                    assertThat(beerInfo.getProduct().getCategory()).isEqualTo(Category.CRAB.getName());
                    assertThat(beerInfo.getProduct().getMeta_search()).isEqualTo("beer tiger day la beer tiger co non do con cao nen chu y khi su dung:\n" +
                            "- bia thom ngon\n" +
                            "- bia nhap ngoai\n" +
                            "- bia san xuat tu ha lan");
                    assertThat(beerInfo.getProductUnit().length).isEqualTo(2);
                    Flux.just(beerInfo.getProductUnit())
                            .sort(Comparator.comparing(ProductUnit::getName))
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

        for (SearchQuery.Filter filter :
                SearchQuery.Filter.values()) {

            this.beerAPI.CountSearchBeer(SearchQuery.builder().query("hà").page(0).size(100).filter(filter.getName()).group_id(group).build())
                    .as(StepVerifier::create)
                    .consumeNextWith(resultWithCount -> {
                        try {
                            System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        assertThat(resultWithCount.getCount()).isEqualTo(1);
                        Mono.just(resultWithCount.getResult())
                                .flatMapMany(Flux::fromIterable)
                                .as(StepVerifier::create)
                                .consumeNextWith(beerSubmitData -> {
                                    assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                                })
                                .verifyComplete();
                    })
                    .verifyComplete();
        }

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("hà").page(0).size(100).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("hà").page(0).size(100).filter(SearchQuery.Filter.NAME_ASC.getName()).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("tiger").page(0).size(100).filter(SearchQuery.Filter.PRICE_ASC.getName()).group_id(group).build())
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
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("sold_out");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("tiger").page(0).size(100).filter(SearchQuery.Filter.PRICE_DESC.getName()).group_id(group).build())
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
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("sold_out");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("tiger").page(0).size(100).filter(SearchQuery.Filter.CREATE_ASC.getName()).group_id(group).build())
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
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("sold_out");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("tiger").page(0).size(100).filter(SearchQuery.Filter.CREATE_DESC.getName()).group_id(group).build())
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
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("sold_out");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("tiger").page(0).size(100).filter(SearchQuery.Filter.SOLD_NUM.getName()).group_id(group).build())
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
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("sold_out");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("ha%oai").page(0).size(100).filter(SearchQuery.Filter.CREATE_ASC.getName()).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        for (SearchQuery.Filter filter :
                SearchQuery.Filter.values()) {
            this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bia&ha&lan").page(0).size(100).filter(filter.getName()).group_id(group).build())
                    .as(StepVerifier::create)
                    .consumeNextWith(resultWithCount -> {
                        try {
                            System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        assertThat(resultWithCount.getCount()).isEqualTo(1);
                        Mono.just(resultWithCount.getResult())
                                .flatMapMany(Flux::fromIterable)
                                .as(StepVerifier::create)
                                .consumeNextWith(beerSubmitData -> {
                                    assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                                })
                                .verifyComplete();
                    })
                    .verifyComplete();
        }

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bia&ha&lan").page(0).size(100).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bia&ha&lan").page(0).size(100).filter(SearchQuery.Filter.CREATE_DESC.getName()).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    
                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bi:*&ngo:*&nhap:*").page(0).size(100).filter(SearchQuery.Filter.NAME_ASC.getName()).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    
                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bi:*&ngo:*&nhap:*").page(0).size(100).filter(SearchQuery.Filter.NAME_DESC.getName()).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    
                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bi:*&ngo:*&nhap:*").page(0).size(100).filter(SearchQuery.Filter.PRICE_ASC.getName()).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    
                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bi:*&ngo:*&nhap:*").page(0).size(100).filter(SearchQuery.Filter.PRICE_DESC.getName()).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    
                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bi:*&ngo:*&nhap:*").page(0).size(100).filter(SearchQuery.Filter.CREATE_ASC.getName()).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    
                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bi:*&ngo:*&nhap:*").page(0).size(100).filter(SearchQuery.Filter.CREATE_DESC.getName()).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    
                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountSearchBeer(SearchQuery.builder().query("bi:*&ngo:*&nhap:*").page(0).size(100).filter(SearchQuery.Filter.SOLD_NUM.getName()).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(1);
                    
                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .verifyComplete();
                })
                .verifyComplete();


        for (SearchQuery.Filter filter : SearchQuery.Filter.values()) {
            this.beerAPI.AdminCountSearchBeer(SearchQuery.builder().query("bi:*&ngo:*&nhap:*").page(0).size(100).filter(filter.getName()).group_id(group).build())
                    .as(StepVerifier::create)
                    .consumeNextWith(resultWithCount -> {
                        try {
                            System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        assertThat(resultWithCount.getCount()).isEqualTo(2);

                        Mono.just(resultWithCount.getResult())
                                .flatMapMany(Flux::fromIterable)
                                .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                                .as(StepVerifier::create)
                                .consumeNextWith(beerSubmitData -> {
                                    assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                                })
                                .consumeNextWith(beerSubmitData -> {
                                    assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("hide");
                                })
                                .verifyComplete();
                    })
                    .verifyComplete();
        }


        for (SearchQuery.Filter filter : SearchQuery.Filter.values()) {
            System.out.println("filter: " + filter.getName());
            this.beerAPI.AdminCountSearchBeer(SearchQuery.builder().query("bia nhập").page(0).size(100).filter(filter.getName()).group_id(group).build())
                    .as(StepVerifier::create)
                    .consumeNextWith(resultWithCount -> {
                        try {
                            System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        assertThat(resultWithCount.getCount()).isEqualTo(2);

                        Mono.just(resultWithCount.getResult())
                                .flatMapMany(Flux::fromIterable)
                                .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                                .as(StepVerifier::create)
                                .consumeNextWith(beerSubmitData -> {
                                    assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                                })
                                .consumeNextWith(beerSubmitData -> {
                                    assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("hide");
                                })
                                .verifyComplete();
                    })
                    .verifyComplete();
        }

        this.beerAPI.AdminCountSearchBeer(SearchQuery.builder().query("bi:*&ngo:*&nhap:*").page(0).size(100).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(2);
                    
                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("hide");
                            })
                            .verifyComplete();
                })
                .verifyComplete();


        this.beerAPI.AdminCountSearchBeer(SearchQuery.builder().query("bia nhập").page(0).size(100).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(resultWithCount -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(resultWithCount));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(resultWithCount.getCount()).isEqualTo(2);

                    Mono.just(resultWithCount.getResult())
                            .flatMapMany(Flux::fromIterable)
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("hide");
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.GetAllBeer(SearchQuery.builder().page(0).size(100).group_id(group).build())
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
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getBeerSecondID()).isEqualTo("sold_out");
                    assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                })
                .verifyComplete();

        for (SearchQuery.Filter filter : SearchQuery.Filter.values()) {
            this.beerAPI.CountGetAllBeer(SearchQuery.builder().page(0).size(100).filter(filter.getName()).group_id(group).build())
                    .as(StepVerifier::create)
                    .consumeNextWith(searchResult -> {
                        try {
                            System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        assertThat(searchResult.getCount()).isEqualTo(3);

                        Mono.just(searchResult.getResult())
                                .flatMapMany(Flux::fromIterable)
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
                                .consumeNextWith(beerInfo -> {
                                    assertThat(beerInfo.getBeerSecondID()).isEqualTo("sold_out");
                                    assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                                })
                                .verifyComplete();
                    })
                    .verifyComplete();

        }

        this.beerAPI.CountGetAllBeer(SearchQuery.builder().page(0).size(100).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(3);
                    

                    Mono.just(searchResult.getResult())
                            .flatMapMany(Flux::fromIterable)
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
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("sold_out");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.AdminCountGetAllBeer(SearchQuery.builder().page(0).size(3).filter(SearchQuery.Filter.NAME_ASC.getName()).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(4);


                    Mono.just(searchResult.getResult())
                            .flatMapMany(Flux::fromIterable)
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
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("hide");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
//                            .consumeNextWith(beerInfo -> {
//                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("sold_out");
//                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
//                            })
                            .verifyComplete();
                })
                .verifyComplete();

        for (SearchQuery.Filter filter : SearchQuery.Filter.values()) {
            this.beerAPI.AdminCountGetAllBeer(SearchQuery.builder().page(0).size(100).filter(filter.getName()).group_id(group).build())
                    .as(StepVerifier::create)
                    .consumeNextWith(searchResult -> {
                        try {
                            System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        assertThat(searchResult.getCount()).isEqualTo(4);


                        Mono.just(searchResult.getResult())
                                .flatMapMany(Flux::fromIterable)
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
                                .consumeNextWith(beerInfo -> {
                                    assertThat(beerInfo.getBeerSecondID()).isEqualTo("hide");
                                    assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                                })
                                .consumeNextWith(beerInfo -> {
                                    assertThat(beerInfo.getBeerSecondID()).isEqualTo("sold_out");
                                    assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                                })
                                .verifyComplete();
                    })
                    .verifyComplete();
        }

        this.beerAPI.AdminCountGetAllBeer(SearchQuery.builder().page(0).size(100).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(4);
                    

                    Mono.just(searchResult.getResult())
                            .flatMapMany(Flux::fromIterable)
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
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("hide");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("sold_out");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeerByCategory(SearchQuery.builder().page(0).size(100).query(Category.CRAB.getName()).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(3);
                    

                    Mono.just(searchResult.getResult())
                            .flatMapMany(Flux::fromIterable)
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
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("sold_out");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeerByCategory(SearchQuery.builder().page(0).size(100).filter(SearchQuery.Filter.PRICE_DESC.getName()).query(Category.SQUID.getName()).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(0);

                    assertThat(searchResult.getResult() == null).isEqualTo(true);
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeerByCategory(SearchQuery.builder().page(0).size(100).query(Category.SQUID.getName()).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(0);

                    assertThat(searchResult.getResult() == null).isEqualTo(true);
                })
                .verifyComplete();

        this.beerAPI.CountGetAllBeerByCategory(SearchQuery.builder().page(0).size(100).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(0);

                    assertThat(searchResult.getResult() == null).isEqualTo(true);
                })
                .verifyComplete();


        for (SearchQuery.Filter filter :
                SearchQuery.Filter.values()) {
            this.beerAPI.CountGetAllBeerByCategory(SearchQuery.builder().page(0).size(100).filter(filter.getName()).query(Category.CRAB.getName()).group_id(group).build())
                    .as(StepVerifier::create)
                    .consumeNextWith(searchResult -> {
                        try {
                            System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        assertThat(searchResult.getCount()).isEqualTo(3);


                        Mono.just(searchResult.getResult())
                                .flatMapMany(Flux::fromIterable)
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
                                .consumeNextWith(beerInfo -> {
                                    assertThat(beerInfo.getBeerSecondID()).isEqualTo("sold_out");
                                    assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                                })
                                .verifyComplete();
                    })
                    .verifyComplete();
        }

        this.beerAPI.AdminCountGetAllBeerByCategory(SearchQuery.builder().page(0).size(2).filter(SearchQuery.Filter.SOLD_NUM.getName()).query(Category.CRAB.getName()).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(4);


                    Mono.just(searchResult.getResult())
                            .flatMapMany(Flux::fromIterable)
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
//                            .consumeNextWith(beerInfo -> {
//                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("hide");
//                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
//                            })
//                            .consumeNextWith(beerInfo -> {
//                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("sold_out");
//                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
//                            })
                            .verifyComplete();
                })
                .verifyComplete();

        for (SearchQuery.Filter filter :
                SearchQuery.Filter.values()) {
            this.beerAPI.AdminCountGetAllBeerByCategory(SearchQuery.builder().page(0).size(100).filter(filter.getName()).query(Category.CRAB.getName()).group_id(group).build())
                    .as(StepVerifier::create)
                    .consumeNextWith(searchResult -> {
                        try {
                            System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        assertThat(searchResult.getCount()).isEqualTo(4);


                        Mono.just(searchResult.getResult())
                                .flatMapMany(Flux::fromIterable)
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
                                .consumeNextWith(beerInfo -> {
                                    assertThat(beerInfo.getBeerSecondID()).isEqualTo("hide");
                                    assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                                })
                                .consumeNextWith(beerInfo -> {
                                    assertThat(beerInfo.getBeerSecondID()).isEqualTo("sold_out");
                                    assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                                })
                                .verifyComplete();
                    })
                    .verifyComplete();
        }

        this.beerAPI.AdminCountGetAllBeerByCategory(SearchQuery.builder().page(0).size(100).query(Category.CRAB.getName()).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(4);
                    

                    Mono.just(searchResult.getResult())
                            .flatMapMany(Flux::fromIterable)
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
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("hide");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .consumeNextWith(beerInfo -> {
                                assertThat(beerInfo.getBeerSecondID()).isEqualTo("sold_out");
                                assertThat(beerInfo.getListUnit().length).isEqualTo(2);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        this.beerAPI.AdminCountGetAllBeerByCategory(SearchQuery.builder().page(0).size(100).query(Category.HOLOTHURIAN.getName()).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(searchResult -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(searchResult));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat(searchResult.getCount()).isEqualTo(0);

                    assertThat(searchResult.getResult() == null).isEqualTo(true);
                })
                .verifyComplete();



        beerAPI.CreateBeer(
                BeerInfo
                        .builder()
                        .productUnit(new ProductUnit[]{
                                ProductUnit
                                        .builder()
                                        .product_unit_second_id(beerUnit4561ID.get())
                                        .product_second_id("444")
                                        .price(10)
                                        .weight(0.3f)
                                        .discount(10)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("thung")
                                        .group_id(group)
                                        .build(),
                                ProductUnit
                                        .builder()
                                        .product_unit_second_id(beerUnit4562ID.get())
                                        .product_second_id("444")
                                        .price(10)
                                        .weight(0.3f)
                                        .discount(10)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("lon")
                                        .group_id(group)
                                        .build()
                        })
                        .product(Product
                                .builder()
                                .category(Category.CRAB.getName())
                                .detail("Đây là beer tiger có nồn độ cồn cao nên chú ý khi sử dụng:\n" +
                                        "- bia thơm ngon\n" +
                                        "- bia nhập ngoại\n" +
                                        "- bia sản xuất từ hà lan")
                                .name("beer tiger")
                                .group_id(group)
                                .product_second_id("444").build()
                                .AutoFill()
                        )
                        .build()
        )
                .as(StepVerifier::create)
                .expectError();



        beerAPI.CreateBeer(
                        BeerInfo
                                .builder()
                                .productUnit(new ProductUnit[]{
                                        ProductUnit
                                                .builder()
                                                .product_second_id("444")
                                                .price(10)
                                                .weight(0.3f)
                                                .discount(10)
                                                .date_expire(new Timestamp(new Date().getTime()))
                                                .name("thung")
                                                .group_id(group)
                                                .build(),
                                        ProductUnit
                                                .builder()
                                                .product_second_id("444")
                                                .price(10)
                                                .weight(0.3f)
                                                .discount(10)
                                                .date_expire(new Timestamp(new Date().getTime()))
                                                .name("lon")
                                                .group_id(group)
                                                .build()
                                })
                                .product(Product
                                        .builder()
                                        .category(Category.CRAB.getName())
                                        .detail("Đây là beer tiger có nồn độ cồn cao nên chú ý khi sử dụng:\n" +
                                                "- bia thơm ngon\n" +
                                                "- bia nhập ngoại\n" +
                                                "- bia sản xuất từ hà lan")
                                        .name("beer tiger")
                                        .group_id(group)
                                        .product_second_id("444").build()
                                        .AutoFill()
                                )
                                .build()
                ).block();

        this.beerAPI.GetBeerByID(group, "444")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getProduct().getProduct_second_id()).isEqualTo("444");
                })
                .verifyComplete();

        this.beerAPI.DeleteBeerByID(group, "444").block();

        this.beerAPI.GetBeerByID(group, "444")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    private BeerInfo createTestBeer(int idi, int price) {
        String id = idi + "";
        Category category = Category.CRAB;
        if (idi < 24) {
            category = Category.SHRIMP;
        } else if (idi < 50) {
            category = Category.SQUID;
        } else if (idi < 60) {
            category = Category.OYSTER;
        } else if (idi < 75) {
            category = Category.HOLOTHURIAN;
        }
        return
                BeerInfo
                        .builder()
                        .productUnit(new ProductUnit[]{
                                ProductUnit
                                        .builder()
                                        .product_second_id(id)
                                        .price(price)
                                        .weight(0.3f)
                                        .discount(10)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("thung")
                                        .group_id(group)
                                        .build(),
                                ProductUnit
                                        .builder()
                                        .product_second_id(id)
                                        .price(10000)
                                        .weight(0.3f)
                                        .discount(10)
                                        .date_expire(new Timestamp(new Date().getTime()))
                                        .name("lon")
                                        .group_id(group)
                                        .build()
                        })
                        .product(Product
                                .builder()
                                .category(category.getName())
                                .detail("nhưng Milan khởi đầu ấn tượng. Với Mandzukic lần đầu đá chính, cùng sự hỗ trợ của bộ ba Castillejo, Krunic, Rebic, đội nhì bảng Serie A liên tục gây sóng gió về phía cầu môn Sao Đỏ. Chỉ trong 13 phút đầu, Milan")
                                .name(id + " beer tiger")
                                .group_id(group)
                                .product_second_id(id).build()
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
                beerAPI.CreateBeer(template).block();
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