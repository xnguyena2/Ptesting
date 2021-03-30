package com.example.heroku.controller;

import com.example.heroku.model.Image;
import com.example.heroku.model.repository.PackageOrderRepository;
import com.example.heroku.request.Order.OrderSearchResult;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.services.PackageOrder;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/adminapi")
public class AdminController {
    @Autowired
    PackageOrderRepository packageOrderRepository;

    @Autowired
    PackageOrder packageOrder;


    @PostMapping("/orderer/all")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<OrderSearchResult> getIMGbyID(@RequestBody @Valid SearchQuery query) {
        return packageOrder.CountGetAllBeer(query);
    }

}
