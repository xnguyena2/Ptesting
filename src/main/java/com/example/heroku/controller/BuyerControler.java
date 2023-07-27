package com.example.heroku.controller;

import com.example.heroku.model.Users;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.permission.WrapPermissionAction;
import com.example.heroku.response.BuyerData;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/buyer")
public class BuyerControler {

    @Autowired
    com.example.heroku.services.Buyer buyer;

    @PostMapping("/admin/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BuyerData> getAll(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchQuery query) {
        return WrapPermissionAction.<BuyerData>builder()
                .principal(principal)
                .query(query)
                .fluxAction(q -> buyer.GetAll(q))
                .build()
                .toFlux();
    }

}
