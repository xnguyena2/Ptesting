package com.example.heroku;

import com.example.heroku.services.UserDevice;
import lombok.Builder;

@Builder
public class UserDeviceTest {

    UserDevice userDeviceAPI;

    public void UserTest(){
        userDeviceAPI.CreateUserDevice(com.example.heroku.model.UserDevice.builder().device_id("222222").user_first_name("Nguyen").user_last_name("phong").build())
                .block();
        userDeviceAPI.CreateUserDevice(com.example.heroku.model.UserDevice.builder().device_id("222222").user_first_name("Nguyen Update").user_last_name("phong update").build())
                .block();
        userDeviceAPI.CreateUserDevice(com.example.heroku.model.UserDevice.builder().device_id("333333").user_first_name("Ho DUong").user_last_name("Vuong").build())
                .block();
    }

}
