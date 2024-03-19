package com.example.heroku;

import com.example.heroku.model.Store;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.services.ClientDevice;
import com.example.heroku.services.DeviceConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class DeviceConfigTest {
    private DeviceConfig deviceConfig;

    private ClientDevice clientDevice;

    boolean testWithMainGroup;

    String group;

    public void DeviceConfigTestWithoutImage() {

        ObjectMapper objectMapper = new ObjectMapper();
        List<String> listCategorys = new ArrayList<>();
        String listCategorysString = "listCategorysString";

        for (Category ctg : Category.values()) {
            listCategorys.add(ctg.getName());
        }
        try {
            listCategorysString = objectMapper.writeValueAsString(listCategorys);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        this.deviceConfig
                .UpdateConfig(com.example.heroku.model.DeviceConfig.builder().color("#ffffff").categorys("#ffffff").config("config").group_id(group).build())
                .block();
        this.deviceConfig.GetConfig(group)
                .as(StepVerifier::create)
                .consumeNextWith(config -> {
                    assertThat(config.getColor()).isEqualTo("#ffffff");
                    assertThat(config.getCategorys()).isEqualTo("#ffffff");
                    assertThat(config.getConfig()).isEqualTo("config");
                        }
                )
                .verifyComplete();

        this.deviceConfig
                .UpdateConfig(com.example.heroku.model.DeviceConfig.builder().color("#333333").categorys(listCategorysString).config("config").group_id(group).build())
                .block();
        String finalListCategorysString = listCategorysString;
        this.deviceConfig.GetConfig(group)
                .as(StepVerifier::create)
                .consumeNextWith(config -> {
                    assertThat(config.getColor()).isEqualTo("#333333");
                    assertThat(config.getCategorys()).isEqualTo(finalListCategorysString);
                    assertThat(config.getConfig()).isEqualTo("config");
                        }
                )
                .verifyComplete();

        this.clientDevice.bootStrapDataForWeb(group)
                .as(StepVerifier::create)
                .consumeNextWith(config -> {
                            assertThat(config.getDeviceConfig().getColor()).isEqualTo("#333333");
                            assertThat(config.getDeviceConfig().getCategorys()).isEqualTo(finalListCategorysString);
                            assertThat(config.getDeviceConfig().getConfig()).isEqualTo("config");
                            if (testWithMainGroup) {
                                Store store = config.getStore();
                                assertThat(store.getName()).isEqualTo("shop ban chuoi");
                                assertThat(store.getPhone()).isEqualTo("121212");
                                assertThat(store.getAddress()).isEqualTo("123 abc");
                                assertThat(store.getTime_open()).isEqualTo("all time");
                            }
                            try {
                                List<String> listCategory = objectMapper.readValue(config.getDeviceConfig().getCategorys(), new TypeReference<List<String>>() {
                                });
                                assertThat(listCategory.size()).isEqualTo(Category.values().length);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }

                        }
                )
                .verifyComplete();

        this.clientDevice.adminBootStrapWithoutCarouselData(group)
                .as(StepVerifier::create)
                .consumeNextWith(config -> {
                            assertThat(config.getDeviceConfig().getColor()).isEqualTo("#333333");
                            assertThat(config.getDeviceConfig().getCategorys()).isEqualTo(finalListCategorysString);
                            assertThat(config.getDeviceConfig().getConfig()).isEqualTo("config");
                            assertThat(config.getBenifit().getRevenue()).isEqualTo(0);
                            if(testWithMainGroup) {
                                Store store = config.getStore();
                                assertThat(store.getName()).isEqualTo("shop ban chuoi");
                                assertThat(store.getPhone()).isEqualTo("121212");
                                assertThat(store.getAddress()).isEqualTo("123 abc");
                                assertThat(store.getTime_open()).isEqualTo("all time");
                            }
                            try {
                                List<String> listCategory = objectMapper.readValue(config.getDeviceConfig().getCategorys(), new TypeReference<List<String>>() {
                                });
                                assertThat(listCategory.size()).isEqualTo(Category.values().length);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }

                        }
                )
                .verifyComplete();
    }

    public void DeviceConfigTest() {

        ObjectMapper objectMapper = new ObjectMapper();
        List<String> listCategorys = new ArrayList<>();
        String listCategorysString = "listCategorysString";

        for (Category ctg : Category.values()) {
            listCategorys.add(ctg.getName());
        }
        try {
            listCategorysString = objectMapper.writeValueAsString(listCategorys);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        this.deviceConfig
                .UpdateConfig(com.example.heroku.model.DeviceConfig.builder().color("#ffffff").categorys("#ffffff").config("config").group_id(group).build())
                .block();
        this.deviceConfig.GetConfig(group)
                .as(StepVerifier::create)
                .consumeNextWith(config -> {
                            assertThat(config.getColor()).isEqualTo("#ffffff");
                            assertThat(config.getCategorys()).isEqualTo("#ffffff");
                            assertThat(config.getConfig()).isEqualTo("config");
                        }
                )
                .verifyComplete();

        this.deviceConfig
                .UpdateConfig(com.example.heroku.model.DeviceConfig.builder().color("#333333").categorys(listCategorysString).config("config").group_id(group).build())
                .block();
        String finalListCategorysString = listCategorysString;
        this.deviceConfig.GetConfig(group)
                .as(StepVerifier::create)
                .consumeNextWith(config -> {
                            assertThat(config.getColor()).isEqualTo("#333333");
                    assertThat(config.getCategorys()).isEqualTo(finalListCategorysString);
                    assertThat(config.getConfig()).isEqualTo("config");
                        }
                )
                .verifyComplete();

        this.clientDevice.bootStrapDataForWeb(group)
                .as(StepVerifier::create)
                .consumeNextWith(config -> {
                            assertThat(config.getDeviceConfig().getColor()).isEqualTo("#333333");
                            assertThat(config.getDeviceConfig().getCategorys()).isEqualTo(finalListCategorysString);
                            assertThat(config.getDeviceConfig().getConfig()).isEqualTo("config");
                            assertThat((long) config.getCarousel().size()).isEqualTo(4);
                            if(testWithMainGroup) {
                                Store store = config.getStore();
                                assertThat(store.getName()).isEqualTo("shop ban chuoi");
                                assertThat(store.getPhone()).isEqualTo("121212");
                                assertThat(store.getAddress()).isEqualTo("123 abc");
                                assertThat(store.getTime_open()).isEqualTo("all time");
                            }
                            try {
                                List<String> listCategory = objectMapper.readValue(config.getDeviceConfig().getCategorys(), new TypeReference<List<String>>() {
                                });
                                assertThat(listCategory.size()).isEqualTo(Category.values().length);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .verifyComplete();

        this.clientDevice.adminBootStrapWithoutCarouselDataBenifitOfCurrentDate(group)
                .as(StepVerifier::create)
                .consumeNextWith(config -> {
                            assertThat(config.getDeviceConfig().getColor()).isEqualTo("#333333");
                            assertThat(config.getDeviceConfig().getCategorys()).isEqualTo(finalListCategorysString);
                            assertThat(config.getDeviceConfig().getConfig()).isEqualTo("config");
                            assertThat((long) config.getCarousel().size()).isEqualTo(0);
                            assertThat(config.getBenifit().getRevenue()).isEqualTo(0);
                            if(testWithMainGroup) {
                                Store store = config.getStore();
                                assertThat(store.getName()).isEqualTo("shop ban chuoi");
                                assertThat(store.getPhone()).isEqualTo("121212");
                                assertThat(store.getAddress()).isEqualTo("123 abc");
                                assertThat(store.getTime_open()).isEqualTo("all time");
                            }
                            try {
                                List<String> listCategory = objectMapper.readValue(config.getDeviceConfig().getCategorys(), new TypeReference<List<String>>() {
                                });
                                assertThat(listCategory.size()).isEqualTo(Category.values().length);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .verifyComplete();
    }
}
