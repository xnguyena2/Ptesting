package com.example.heroku.controller;

import com.example.heroku.request.client.Register;
import com.example.heroku.response.BootStrapData;
import com.example.heroku.services.UserDevice;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/clientdevice")
public class ClientDeviceController {

    @Autowired
    com.example.heroku.services.ClientDevice clientDeviceAPI;

    @Autowired
    UserDevice userDeviceAPI;

    @GetMapping("/bootstrap")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<BootStrapData> bootStrapData(){
        System.out.println("Get bootstrap!");
        return clientDeviceAPI.bootStrapData();
    }

    @PostMapping("/admin/register")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Object> register(@RequestBody @Valid Register userInfo) {
        System.out.println("register new user: " + userInfo.getId());
        return userDeviceAPI.CreateUserDevice(
                com.example.heroku.model.UserDevice.builder()
                        .device_id(userInfo.getId())
                        .user_first_name(userInfo.getFirstName())
                        .user_last_name(userInfo.getLastName())
                        .build());
    }
}
