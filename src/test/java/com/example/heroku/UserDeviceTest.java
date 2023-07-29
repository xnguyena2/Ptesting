package com.example.heroku;

import com.example.heroku.model.ProductUnit;
import com.example.heroku.services.UserDevice;
import lombok.Builder;
import reactor.test.StepVerifier;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class UserDeviceTest {

    UserDevice userDeviceAPI;

    String group;

    public void UserTest() {
        userDeviceAPI.CreateUserDevice(com.example.heroku.model.UserDevice.builder().device_id("222222").user_first_name("Nguyen").user_last_name("phong").group_id(group).build())
                .block();
        userDeviceAPI.CreateUserDevice(com.example.heroku.model.UserDevice.builder().device_id("222222").user_first_name("Nguyen Update").user_last_name("phong update").group_id(group).build())
                .block();
        userDeviceAPI.CreateUserDevice(com.example.heroku.model.UserDevice.builder().device_id("333333").user_first_name("Ho DUong").user_last_name("Vuong").group_id(group).build())
                .block();

        userDeviceAPI.getDevice(group, "222222")
                .as(StepVerifier::create)
                .consumeNextWith(device -> {
                    assertThat(device.getUser_first_name()).isEqualTo("Nguyen Update");
                })
                .verifyComplete();

        userDeviceAPI.getAllDevice(group, 0, 100)
                .sort(Comparator.comparing(com.example.heroku.model.UserDevice::getUser_first_name))
                .as(StepVerifier::create)
                .consumeNextWith(device -> {
                    assertThat(device.getUser_first_name()).isEqualTo("Ho DUong");
                })
                .consumeNextWith(device -> {
                    assertThat(device.getUser_first_name()).isEqualTo("Nguyen Update");
                })
                .verifyComplete();

        userDeviceAPI.deleteDevice(group, "222222").block();

        userDeviceAPI.getDevice(group, "222222")
                .as(StepVerifier::create)
                .verifyComplete();

        userDeviceAPI.getAllDevice(group, 0, 100)
                .sort(Comparator.comparing(com.example.heroku.model.UserDevice::getUser_first_name))
                .as(StepVerifier::create)
                .consumeNextWith(device -> {
                    assertThat(device.getUser_first_name()).isEqualTo("Ho DUong");
                })
                .verifyComplete();
    }

}
