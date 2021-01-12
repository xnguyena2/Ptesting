package com.example.heroku;

import com.example.heroku.model.Beer;
import com.example.heroku.model.BeerUnit;
import com.example.heroku.request.beer.BeerInfo;
import lombok.Builder;
import reactor.test.StepVerifier;

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
                                .category(Beer.Category.ALCOHOL)
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
                                .category(Beer.Category.ALCOHOL)
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
                    beerUnit1ID.set(beerUnit.getBeer_unit_second_id());
                    assertThat(beerUnit.getBeer_unit_second_id()).isNotNull();

                })
                .consumeNextWith(beerUnit -> {
                    beerUnit2ID.set(beerUnit.getBeer_unit_second_id());
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
                                        .name("thung")
                                        .build(),
                                BeerUnit
                                        .builder()
                                        .beer_unit_second_id(beerUnit2ID.get())
                                        .beer("456")
                                        .name("lon")
                                        .build()
                        })
                        .beer(Beer
                                .builder()
                                .category(Beer.Category.ALCOHOL)
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
                    assertThat(beerUnit.getBeer_unit_second_id()).isEqualTo(beerUnit1ID.get());

                })
                .consumeNextWith(beerUnit -> {
                    assertThat(beerUnit.getBeer_unit_second_id()).isEqualTo(beerUnit2ID.get());
                })
                .verifyComplete();

        this.beerAPI.GetBeerByID("123")
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                            assertThat(beerInfo.getBeer().getBeer_second_id()).isEqualTo("123");
                            assertThat(beerInfo.getBeer().getCategory()).isEqualTo(Beer.Category.ALCOHOL);
                            assertThat(beerInfo.getBeerUnit().length).isEqualTo(2);
                            assertThat(beerInfo.getBeerUnit()[0].getName()).isEqualTo("thung");
                            assertThat(beerInfo.getBeerUnit()[1].getName()).isEqualTo("lon");
                        }
                )
                .verifyComplete();

        this.beerAPI.SearchBeer("hà")
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                            assertThat(beerInfo.getBeer_second_id()).isEqualTo("456");
                        }
                )
                .verifyComplete();

        this.beerAPI.SearchBeer("ha%oai")
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                            assertThat(beerInfo.getBeer_second_id()).isEqualTo("456");
                        }
                )
                .verifyComplete();

        this.beerAPI.SearchBeer("bia&ha&lan")
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                            assertThat(beerInfo.getBeer_second_id()).isEqualTo("456");
                        }
                )
                .verifyComplete();

        this.beerAPI.SearchBeer("bi:*&ngo:*&nhap:*")
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                            assertThat(beerInfo.getBeer_second_id()).isEqualTo("456");
                        }
                )
                .verifyComplete();

        this.beerAPI.GetAllBeer(0, 100)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                            assertThat(beerInfo.getBeer_second_id()).isEqualTo("456");
                        }
                )
                .consumeNextWith(beerInfo -> {
                            assertThat(beerInfo.getBeer_second_id()).isEqualTo("123");
                        }
                )
                .verifyComplete();

        this.beerAPI.GetAllBeerByCategory(Beer.Category.ALCOHOL, 0, 100)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                            assertThat(beerInfo.getBeer_second_id()).isEqualTo("456");
                        }
                )
                .consumeNextWith(beerInfo -> {
                            assertThat(beerInfo.getBeer_second_id()).isEqualTo("123");
                        }
                )
                .verifyComplete();
    }
}
