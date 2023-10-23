package com.example.heroku;

import com.example.heroku.request.beer.BeerSubmitData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class ClientDeviceTest {

    com.example.heroku.services.ClientDevice clientDeviceAPI;

    String group;

    public void BootStrapData() {
        clientDeviceAPI.bootStrapData(group)
                .as(StepVerifier::create)
                .consumeNextWith(bootStrapData -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(bootStrapData));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat((long) bootStrapData.getCarousel().size()).isEqualTo(4);
                    assertThat((long) bootStrapData.getProducts().size()).isEqualTo(3);
                    Flux.just(bootStrapData.getProducts().toArray(new BeerSubmitData[0]))
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerSubmitData.getImages().size()).isEqualTo(4);
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("sold_out");
                            })
                            .verifyComplete();
                })
                .verifyComplete();
    }

    public void BootStrapDataWithoutImage() {
        clientDeviceAPI.bootStrapData(group)
                .as(StepVerifier::create)
                .consumeNextWith(bootStrapData -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(bootStrapData));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat((long) bootStrapData.getProducts().size()).isEqualTo(3);
                    Flux.just(bootStrapData.getProducts().toArray(new BeerSubmitData[0]))
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
    }

    public void BootStrapDataNew() {
        clientDeviceAPI.adminBootStrapWithoutCarouselData(group)
                .as(StepVerifier::create)
                .consumeNextWith(bootStrapData -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(bootStrapData));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat((long) bootStrapData.getCarousel().size()).isEqualTo(0);
                    assertThat((long) bootStrapData.getProducts().size()).isEqualTo(3);
                    Flux.just(bootStrapData.getProducts().toArray(new BeerSubmitData[0]))
                            .sort(Comparator.comparing(BeerSubmitData::getBeerSecondID))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("123");
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("456");
                                assertThat(beerSubmitData.getImages().size()).isEqualTo(4);
                            })
                            .consumeNextWith(beerSubmitData -> {
                                assertThat(beerSubmitData.getBeerSecondID()).isEqualTo("sold_out");
                            })
                            .verifyComplete();
                })
                .verifyComplete();
    }

    public void BootStrapDataWithoutImageNew() {
        clientDeviceAPI.adminBootStrapWithoutCarouselData(group)
                .as(StepVerifier::create)
                .consumeNextWith(bootStrapData -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(bootStrapData));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat((long) bootStrapData.getProducts().size()).isEqualTo(3);
                    Flux.just(bootStrapData.getProducts().toArray(new BeerSubmitData[0]))
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
    }

    public void BootStrapDataLarge() {
        clientDeviceAPI.bootStrapData(group)
                .as(StepVerifier::create)
                .consumeNextWith(bootStrapData -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(bootStrapData));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat((long) bootStrapData.getProducts().size()).isEqualTo(24);
                })
                .verifyComplete();
    }
}
