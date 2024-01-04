package com.example.heroku.controller;

import com.example.heroku.model.Users;
import com.example.heroku.request.client.Register;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.response.BootStrapData;
import com.example.heroku.response.Format;
import com.example.heroku.services.UserDevice;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/clientdevice")
public class ClientDeviceController {

    @Autowired
    com.example.heroku.services.ClientDevice clientDeviceAPI;

    @Autowired
    UserDevice userDeviceAPI;

    @GetMapping("/bootstrap/{groupid}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<BootStrapData> bootStrapData(@PathVariable("groupid") String groupID) {
        System.out.println("Get bootstrap!");
        return clientDeviceAPI.bootStrapData(groupID);
    }

    @GetMapping("/bootstrapfull/{groupid}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<BootStrapData> fullBootStrapData(@PathVariable("groupid") String groupID) {
        System.out.println("Get full bootstrap!: " + groupID);
        return clientDeviceAPI.adminBootStrapWithoutCarouselData(groupID);
    }

    @GetMapping("/bootstrapfullbenifitcurrentdate/{groupid}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<BootStrapData> fullBootStrapDataBenifitCurrentDate(@PathVariable("groupid") String groupID) {
        System.out.println("Get full bootstrap and benifit of current date!: " + groupID);
        return clientDeviceAPI.adminBootStrapWithoutCarouselDataBenifitOfCurrentDate(groupID);
    }

    @PostMapping("/admin/register")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> register(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid Register userInfo) {
        System.out.println("register new user: " + userInfo.getId());
        return WrapPermissionGroupWithPrincipalAction.<ResponseEntity<Format>>builder()
                .principal(principal)
                .subject(userInfo::getGroup_id)
                .monoAction(() -> userDeviceAPI.CreateUserDevice(
                        com.example.heroku.model.UserDevice.builder()
                                .device_id(userInfo.getId())
                                .user_first_name(userInfo.getFirstName())
                                .user_last_name(userInfo.getLastName())
                                .group_id(userInfo.getGroup_id())
                                .build()
                ))
                .build().toMono();
    }
}
