package com.example.heroku;

import com.example.heroku.services.UserAddress;
import lombok.Builder;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class UserAddressTest {

    UserAddress userAddressAPI;

    public void UserAddressTest() {
        userAddressAPI.CreateAddress(
                com.example.heroku.model.UserAddress.builder()
                        .device_id("222222")
                        .phone_number("1234555555")
                        .reciver_fullname("Nguyen Phong")
                        .house_number("123 Nguyễn Huệ")
                        .region(294)
                        .district(484)
                        .ward(10379)
                        .build()
        )
                .block();

        userAddressAPI.CreateAddress(
                com.example.heroku.model.UserAddress.builder()
                        .device_id("222222")
                        .phone_number("1234555555")
                        .reciver_fullname("Nguyen Phong")
                        .house_number("123 Nguyễn Huệ")
                        .region(294)
                        .district(484)
                        .ward(10379)
                        .build()
        )
                .block();

        userAddressAPI.CreateAddress(
                com.example.heroku.model.UserAddress.builder()
                        .device_id("222222")
                        .phone_number("1234555555")
                        .reciver_fullname("Nguyen Phong")
                        .house_number("123 Nguyễn Huệ")
                        .region(294)
                        .district(484)
                        .ward(10379)
                        .build()
        )
                .block();

        userAddressAPI.CreateAddress(
                com.example.heroku.model.UserAddress.builder()
                        .device_id("222222")
                        .phone_number("1234555555")
                        .reciver_fullname("Nguyen Phong")
                        .house_number("123 Hai Bà Trưng")
                        .region(294)
                        .district(484)
                        .ward(10379)
                        .build()
        )
                .block();

        userAddressAPI.GetUserAddress("222222")
                .as(StepVerifier::create)
                .consumeNextWith(userAddress -> {
                    assertThat(userAddress.getHouse_number()).isEqualTo("123 Nguyễn Huệ");
                    assertThat(userAddress.getRegion()).isEqualTo(294);
                    assertThat(userAddress.getDistrict()).isEqualTo(484);
                    assertThat(userAddress.getPhone_number()).isEqualTo("1234555555");
                }
                )
                .consumeNextWith(userAddress -> {
                    assertThat(userAddress.getHouse_number()).isEqualTo("123 Hai Bà Trưng");
                    assertThat(userAddress.getRegion()).isEqualTo(294);
                    assertThat(userAddress.getDistrict()).isEqualTo(484);
                    assertThat(userAddress.getPhone_number()).isEqualTo("1234555555");
                }
                )
                .verifyComplete();

        userAddressAPI.GetUserAddress("3333")
                .as(StepVerifier::create)
                .verifyComplete();

        userAddressAPI.DeleteAddress(1)
                .block();


        userAddressAPI.UpdateAddress(
                com.example.heroku.model.UserAddress.builder()
                        .id("2")
                        .device_id("222222")
                        .phone_number("33333333333")
                        .reciver_fullname("Nguyen Phong")
                        .house_number("125 Nguyễn Huệ")
                        .region(294)
                        .district(484)
                        .ward(10379)
                        .build()
        )
                .block();

        userAddressAPI.GetUserAddress("222222")
                .as(StepVerifier::create)
                .consumeNextWith(userAddress -> {
                            assertThat(userAddress.getHouse_number()).isEqualTo("125 Nguyễn Huệ");
                            assertThat(userAddress.getRegion()).isEqualTo(294);
                            assertThat(userAddress.getDistrict()).isEqualTo(484);
                            assertThat(userAddress.getPhone_number()).isEqualTo("33333333333");
                        }
                )
                .verifyComplete();
    }
}
