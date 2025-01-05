package com.example.heroku.controller;

import com.example.heroku.model.Users;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.response.Format;
import com.example.heroku.services.DeviceConfig;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public Mono<ResponseEntity<Format>> updateDeviceColor(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid com.example.heroku.model.DeviceConfig config) {
        System.out.println("group: " + config.getGroup_id() + ", update config: " + config.getConfig());
        return WrapPermissionGroupWithPrincipalAction.<ResponseEntity<Format>>builder()
                .principal(principal)
                .subject(config::getGroup_id)
                .monoAction(() -> deviceConfigAPI.UpdateConfig(config))
                .build().toMono();
    }

    @GetMapping("/get/{groupid}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.model.DeviceConfig> getDeviceColor(@PathVariable("groupid") String groupID){
        return deviceConfigAPI.GetConfig(groupID);
    }
}
