package com.example.heroku.controller;

import com.example.heroku.model.Users;
import com.example.heroku.request.client.FCMToken;
import com.example.heroku.request.client.Notification;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.response.Format;
import com.example.heroku.services.UserFCMS;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/fcmtoken")
public class FCMTokenController {

    @Autowired
    UserFCMS fcmAPI;

    @PostMapping("/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> createToken(@RequestBody @Valid FCMToken token) {
        System.out.println("device add new token, should check if group_id exist!!");
        return fcmAPI.createFCMToken(token.covertModel());
    }

    @PostMapping("/admin/sendnotifi")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> sendNotifi(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid Notification notification) {
        System.out.println("admin send notification to user");
        return WrapPermissionGroupWithPrincipalAction.<ResponseEntity<Format>>builder()
                .principal(principal)
                .subject(notification::getGroup_id)
                .monoAction(() -> fcmAPI.sendNotification(notification))
                .build().toMono();
    }
}
