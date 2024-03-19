package com.example.heroku;

import com.example.heroku.model.Store;
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

    boolean testWithMainGroup;

    public void BootStrapData() {
        clientDeviceAPI.bootStrapDataForWeb(group)
                .as(StepVerifier::create)
                .consumeNextWith(bootStrapData -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(bootStrapData));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat((long) bootStrapData.getCarousel().size()).isEqualTo(4);
                    assertThat((long) bootStrapData.getProducts().size()).isEqualTo(3);
                    if(testWithMainGroup) {
                        Store store = bootStrapData.getStore();
                        assertThat(store.getName()).isEqualTo("shop ban chuoi");
                        assertThat(store.getPhone()).isEqualTo("121212");
                        assertThat(store.getAddress()).isEqualTo("123 abc");
                        assertThat(store.getTime_open()).isEqualTo("all time");
                    }
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
        clientDeviceAPI.bootStrapDataForWeb(group)
                .as(StepVerifier::create)
                .consumeNextWith(bootStrapData -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(bootStrapData));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    assertThat((long) bootStrapData.getProducts().size()).isEqualTo(3);
                    if(testWithMainGroup) {
                        Store store = bootStrapData.getStore();
                        assertThat(store.getName()).isEqualTo("shop ban chuoi");
                        assertThat(store.getPhone()).isEqualTo("121212");
                        assertThat(store.getAddress()).isEqualTo("123 abc");
                        assertThat(store.getTime_open()).isEqualTo("all time");
                    }
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
                    if(testWithMainGroup) {
                        Store store = bootStrapData.getStore();
                        assertThat(store.getName()).isEqualTo("shop ban chuoi");
                        assertThat(store.getPhone()).isEqualTo("121212");
                        assertThat(store.getAddress()).isEqualTo("123 abc");
                        assertThat(store.getTime_open()).isEqualTo("all time");
                    }
                    assertThat((long) bootStrapData.getCarousel().size()).isEqualTo(0);
                    assertThat((long) bootStrapData.getProducts().size()).isEqualTo(3);
                    assertThat(bootStrapData.getBenifit().getRevenue()).isEqualTo(0);
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
        clientDeviceAPI.adminBootStrapWithoutCarouselDataBenifitOfCurrentDate(group)
                .as(StepVerifier::create)
                .consumeNextWith(bootStrapData -> {
                    try {
                        System.out.println(new ObjectMapper().writeValueAsString(bootStrapData));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    if(testWithMainGroup) {
                        Store store = bootStrapData.getStore();
                        assertThat(store.getName()).isEqualTo("shop ban chuoi");
                        assertThat(store.getPhone()).isEqualTo("121212");
                        assertThat(store.getAddress()).isEqualTo("123 abc");
                        assertThat(store.getTime_open()).isEqualTo("all time");
                    }
                    assertThat((long) bootStrapData.getProducts().size()).isEqualTo(3);
                    assertThat(bootStrapData.getBenifit().getRevenue()).isEqualTo(0);
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
        clientDeviceAPI.bootStrapDataForWeb(group)
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
