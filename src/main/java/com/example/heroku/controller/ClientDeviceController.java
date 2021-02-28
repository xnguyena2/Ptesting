package com.example.heroku.controller;

import com.example.heroku.response.BootStrapData;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/clientdevice")
public class ClientDeviceController {

    @Autowired
    com.example.heroku.services.ClientDevice clientDeviceAPI;

    @GetMapping("/bootstrap")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<BootStrapData> bootStrapData(){
        System.out.println("Get bootstrap!");
        return clientDeviceAPI.bootStrapData();
    }
}
