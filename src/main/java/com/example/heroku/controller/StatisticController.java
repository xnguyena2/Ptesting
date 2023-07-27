package com.example.heroku.controller;

import com.example.heroku.model.Users;
import com.example.heroku.model.statistics.StatisticsTotalOrder;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.permission.WrapPermissionAction;
import com.example.heroku.response.ProductOrderStatus;
import com.example.heroku.services.StatisticServices;
import com.example.heroku.util.Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/statistic")
public class StatisticController {

    @Autowired
    StatisticServices statisticServices;

    @PostMapping("/admin/getbyproductid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<ProductOrderStatus> getbyproductid(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchQuery query) {
        System.out.println("getbyproductid: " + query.GetFilterTxt());
        return WrapPermissionAction.<ProductOrderStatus>builder()
                .principal(principal)
                .query(query)
                .fluxAction(q -> statisticServices.getByProductID(q))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<ProductOrderStatus> getAll(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchQuery query) {
        System.out.println("getall: " + query.GetFilterTxt());
        return WrapPermissionAction.<ProductOrderStatus>builder()
                .principal(principal)
                .query(query)
                .fluxAction(q -> statisticServices.getAll(q))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/gettotal")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<StatisticsTotalOrder> getTotal(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchQuery query) {
        System.out.println("getTotal: " + query.GetFilterTxt());
        return WrapPermissionAction.<StatisticsTotalOrder>builder()
                .principal(principal)
                .query(query)
                .monoAction(q -> statisticServices.getTotal(q))
                .build()
                .toMono();
    }
}