package com.example.heroku.controller;

import com.example.heroku.model.BeerOrder;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.services.StatisticServices;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@RestController
@RequestMapping("/statistic")
public class StatisticController {

    @Autowired
    StatisticServices statisticServices;

    @PostMapping("/admin/getbyproductid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BeerOrder> getbyproductid(@RequestBody @Valid SearchQuery query) {
        System.out.println("getbyproductid: " + query.GetFilterTxt());
        return statisticServices.getByProductID(query);
    }

    @PostMapping("/admin/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BeerOrder> getAll(@RequestBody @Valid SearchQuery query) {
        System.out.println("getall: " + query.GetFilterTxt());
        return statisticServices.getAll(query);
    }

}
