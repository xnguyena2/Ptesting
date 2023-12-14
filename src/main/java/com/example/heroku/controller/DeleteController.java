package com.example.heroku.controller;

import com.example.heroku.model.Users;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/delete")
public class DeleteController {

//    @PostMapping("/admin/changecolor")
//    @CrossOrigin(origins = Util.HOST_URL)
//    public Mono<ResponseEntity<Format>> deleteAll(@AuthenticationPrincipal Mono<Users> principal) {
//        System.out.println("update color: " + config.getColor());
//        return WrapPermissionGroupWithPrincipalAction.<ResponseEntity<Format>>builder()
//                .principal(principal)
//                .subject(config::getGroup_id)
//                .monoAction(() -> deviceConfigAPI.UpdateConfig(config))
//                .build().toMono();
//    }
}
