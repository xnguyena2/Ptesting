package com.example.heroku.controller;

import com.example.heroku.model.PackageOrder;
import com.example.heroku.request.beer.PackageOrderData;
import com.example.heroku.services.BeerOrder;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/order")
public class ProductOrderController {
    @Autowired
    BeerOrder beerOrder;


    @PostMapping("/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<PackageOrder> addBeerInfo(@RequestBody @Valid PackageOrderData packageOrderData) {
        System.out.println("create new order: " + packageOrderData.getPackageOrder().getUser_device_id());
        return beerOrder.createOrder((packageOrderData));
    }
}