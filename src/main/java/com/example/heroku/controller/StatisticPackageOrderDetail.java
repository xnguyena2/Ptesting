package com.example.heroku.controller;

import com.example.heroku.model.Users;
import com.example.heroku.model.statistics.BenifitByDate;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.request.permission.WrapPermissionAction;
import com.example.heroku.response.ProductOrderStatus;
import com.example.heroku.services.StatisticServices;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/packageorderstatistic")
public class StatisticPackageOrderDetail {
    @Autowired
    StatisticServices statisticServices;

    @PostMapping("/admin/getbyproductid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BenifitByDate> getbyproductid(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid PackageID query) {
        System.out.println("getbyproductid: " + query.getGroup_id());
        return WrapPermissionAction.<BenifitByDate>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> statisticServices.getPackageStatictis(query))
                .build()
                .toFlux();
    }
}
