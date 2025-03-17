package com.example.heroku;

import com.example.heroku.model.UserFCM;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.client.FCMToken;
import com.example.heroku.services.UserFCMS;
import lombok.Builder;
import reactor.test.StepVerifier;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class UserFCMTest {
    UserFCMS userFCMAPI;

    String group;

    public void Test() {
        userFCMAPI.createFCMToken(FCMToken.builder()
                        .device_id("fcm_device_1")
                        .fcm_id("fcm_1")
                        .group_id(group)
                        .build().covertModel())
                .block();
        userFCMAPI.createFCMToken(FCMToken.builder()
                        .device_id("fcm_device_1")
                        .fcm_id("fcm_11")
                        .group_id(group)
                        .build().covertModel())
                .block();
        userFCMAPI.createFCMToken(FCMToken.builder()
                        .device_id("fcm_device_2")
                        .fcm_id("fcm_2")
                        .group_id(group)
                        .build().covertModel())
                .block();
        userFCMAPI.createFCMToken(FCMToken.builder()
                        .device_id("fcm_device_2")
                        .fcm_id("fcm_22")
                        .group_id(group)
                        .build().covertModel())
                .block();

        userFCMAPI.findByGroupID(group)
                .sort(Comparator.comparing(UserFCM::getFcm_id))
                .as(StepVerifier::create)
                .consumeNextWith(fcm -> {
                    assertThat(fcm.getFcm_id()).isEqualTo("fcm_11");
                })
                .consumeNextWith(fcm -> {
                    assertThat(fcm.getFcm_id()).isEqualTo("fcm_22");
                })
                .verifyComplete();

        userFCMAPI.findByDeviceID(group, "fcm_device_2")
                .as(StepVerifier::create)
                .consumeNextWith(fcm -> {
                    assertThat(fcm.getFcm_id()).isEqualTo("fcm_22");
                })
                .verifyComplete();

        userFCMAPI.findByDeviceID(group, "fcm_device_1")
                .as(StepVerifier::create)
                .consumeNextWith(fcm -> {
                    assertThat(fcm.getFcm_id()).isEqualTo("fcm_11");
                })
                .verifyComplete();

        userFCMAPI.deleteByDeviceID(group, "fcm_device_1")
                .block();

        userFCMAPI.findByDeviceID(group, "fcm_device_1")
                .as(StepVerifier::create)
                .verifyComplete();

        userFCMAPI.findByDeviceID(group, "fcm_device_2")
                .as(StepVerifier::create)
                .consumeNextWith(fcm -> {
                    assertThat(fcm.getFcm_id()).isEqualTo("fcm_22");
                })
                .verifyComplete();

        userFCMAPI.findByGroupID(group)
                .sort(Comparator.comparing(UserFCM::getFcm_id))
                .as(StepVerifier::create)
                .consumeNextWith(fcm -> {
                    assertThat(fcm.getFcm_id()).isEqualTo("fcm_22");
                })
                .verifyComplete();
    }

}
