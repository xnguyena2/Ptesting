package com.example.heroku.controller;

import com.example.heroku.model.Users;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.beer.SearchResult;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Mono<SearchResult<Users>> getAll(@RequestBody @Valid SearchQuery query) {
        System.out.println("Get all account!");
        return userAccount.getAll(query);
    }
}
