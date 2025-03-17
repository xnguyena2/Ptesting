package com.example.heroku.controller;

import com.example.heroku.firebase.MyFireBase;
import com.example.heroku.request.client.Notification;
import com.example.heroku.request.logs.Logs;
import com.example.heroku.response.Format;
import com.example.heroku.services.UserFCMS;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/getobjectjs")
public class GetObjectFormat {

    @Value("${admin.group.name}")
    private String adminGroupFcm;

    @Autowired
    UserFCMS userFCMS;

    @PostMapping("/logs")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> getPackageObj(@RequestBody @Valid Logs logs) {
        System.out.println(logs.getLogs());
        if (logs.getLogs() != null && logs.getLogs().startsWith("******************* Request login code from phone: ")) {
            String phone = logs.getLogs().substring(48);
            return userFCMS.sendNotificationToGroup(Notification.builder().group_id(adminGroupFcm).title("Request login code from phone: " + phone).msg("Request login code from phone: " + phone).build());
        }
        return
                Mono.just(ok(Format.builder().response("Done!").build()));
    }
}
