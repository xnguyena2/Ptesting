package com.example.heroku;

import com.example.heroku.services.UserAddress;
import lombok.Builder;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class UserAddressTest {

    UserAddress userAddressAPI;

    public void UserAddressTest() {
        AtomicReference<String> addressID1 = new AtomicReference<String>();
        AtomicReference<String> addressID2 = new AtomicReference<String>();
        userAddressAPI.CreateAddress(
                com.example.heroku.model.UserAddress.builder()
                        .address_id("121212")
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
                        .address_id("232323")
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
                            addressID1.set(userAddress.getAddress_id());
                        }
                )
                .consumeNextWith(userAddress -> {
                            assertThat(userAddress.getHouse_number()).isEqualTo("123 Hai Bà Trưng");
                            assertThat(userAddress.getRegion()).isEqualTo(294);
                            assertThat(userAddress.getDistrict()).isEqualTo(484);
                            assertThat(userAddress.getPhone_number()).isEqualTo("1234555555");
                    addressID2.set(userAddress.getAddress_id());
                        }
                )
                .verifyComplete();

        userAddressAPI.GetUserAddress("3333")
                .as(StepVerifier::create)
                .verifyComplete();

        userAddressAPI.DeleteAddress(addressID1.get())
                .block();


        userAddressAPI.UpdateAddress(
                com.example.heroku.model.UserAddress.builder()
                        .address_id(addressID2.get())
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
