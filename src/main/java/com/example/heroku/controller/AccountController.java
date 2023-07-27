package com.example.heroku.controller;

import com.example.heroku.model.Users;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.beer.SearchResult;
import com.example.heroku.request.permission.WrapPermissionAction;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    com.example.heroku.services.UserAccount userAccount;

    @PostMapping("/admin/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<SearchResult<Users>> getAll(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchQuery query) {
        System.out.println("Get all account!");
        return WrapPermissionAction.<SearchResult<Users>>builder()
                .principal(principal)
                .query(query)
                .monoAction(q -> userAccount.getAll(q))
                .build()
                .toMono();
    }
}
