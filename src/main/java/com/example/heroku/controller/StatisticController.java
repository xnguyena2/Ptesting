package com.example.heroku.controller;

import com.example.heroku.model.BeerOrder;
import com.example.heroku.model.statistics.StatisticsTotalOrder;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.response.BeerOrderStatus;
import com.example.heroku.services.StatisticServices;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Flux<BeerOrderStatus> getbyproductid(@RequestBody @Valid SearchQuery query) {
        System.out.println("getbyproductid: " + query.GetFilterTxt());
        return statisticServices.getByProductID(query);
    }

    @PostMapping("/admin/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BeerOrderStatus> getAll(@RequestBody @Valid SearchQuery query) {
        System.out.println("getall: " + query.GetFilterTxt());
        return statisticServices.getAll(query);
    }

    @PostMapping("/admin/gettotal")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<StatisticsTotalOrder> getTotal(@RequestBody @Valid SearchQuery query) {
        System.out.println("getTotal: " + query.GetFilterTxt());
        return statisticServices.getTotal(query);
    }
}
