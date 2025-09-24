package com.example.heroku.controller;

import com.example.heroku.model.UserPaySoDi;
import com.example.heroku.model.Users;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/userpaysodi")
public class UserPaySoDiController {

    @Autowired
    private com.example.heroku.services.UserPaySoDi userPaySoDiService;


    @GetMapping("/payments")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<UserPaySoDi> findByGroupID(@AuthenticationPrincipal Users principal) {
        System.out.println("Get SoDi payments for group: " + principal.getGroup_id());
        return userPaySoDiService.findByGroupId(principal.getGroup_id());
    }
}
