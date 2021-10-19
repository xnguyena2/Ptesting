package com.example.heroku.controller;

import com.example.heroku.response.Format;
import com.example.heroku.services.DeviceConfig;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/deviceconfig")
public class DeviceConfigController {

    @Autowired
    DeviceConfig deviceConfigAPI;

    @PostMapping("/admin/changecolor")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> updateDeviceColor(@RequestBody @Valid com.example.heroku.model.DeviceConfig config) {
        System.out.println("update color: "+config.getColor());
        return deviceConfigAPI.UpdateConfig(config);
    }

    @GetMapping("/get")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.model.DeviceConfig> getDeviceColor(){
        return deviceConfigAPI.GetConfig();
    }
}
