package com.example.heroku;

import com.example.heroku.request.client.FCMToken;
import com.example.heroku.services.UserFCMS;
import lombok.Builder;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class UserFCMTest {
    UserFCMS userFCMAPI;

    public void UserFCMTest() {
        userFCMAPI.createFCMToken(FCMToken.builder()
                        .device_id("fcm_device_1")
                        .fcm_id("fcm_1")
                        .group_id(Config.group)
                        .build().covertModel())
                .block();
        userFCMAPI.createFCMToken(FCMToken.builder()
                        .device_id("fcm_device_1")
                        .fcm_id("fcm_11")
                        .group_id(Config.group)
                        .build().covertModel())
                .block();
        userFCMAPI.createFCMToken(FCMToken.builder()
                        .device_id("fcm_device_2")
                        .fcm_id("fcm_2")
                        .group_id(Config.group)
                        .build().covertModel())
                .block();
        userFCMAPI.createFCMToken(FCMToken.builder()
                        .device_id("fcm_device_2")
                        .fcm_id("fcm_22")
                        .group_id(Config.group)
                        .build().covertModel())
                .block();

        userFCMAPI.findByDeviceID(Config.group, "fcm_device_2")
                .as(StepVerifier::create)
                .consumeNextWith(fcm -> {
                    assertThat(fcm.getFcm_id().equals("fcm_22"));
                })
                .verifyComplete();

        userFCMAPI.findByDeviceID(Config.group, "fcm_device_1")
                .as(StepVerifier::create)
                .consumeNextWith(fcm -> {
                    assertThat(fcm.getFcm_id().equals("fcm_11"));
                })
                .verifyComplete();

        userFCMAPI.deleteByDeviceID(Config.group, "fcm_device_1")
                .block();

        userFCMAPI.findByDeviceID(Config.group, "fcm_device_1")
                .as(StepVerifier::create)
                .verifyComplete();

        userFCMAPI.findByDeviceID(Config.group, "fcm_device_2")
                .as(StepVerifier::create)
                .consumeNextWith(fcm -> {
                    assertThat(fcm.getFcm_id().equals("fcm_22"));
                })
                .verifyComplete();
    }

}
