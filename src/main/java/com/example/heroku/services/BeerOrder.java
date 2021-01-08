package com.example.heroku.services;

import com.example.heroku.model.repository.UserPackageRepository;
import com.example.heroku.request.beer.BeerPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class BeerOrder {

    @Autowired
    UserPackageRepository userPackageRepository;

    public Mono<Object> createOrder(BeerPackage beerPackage) {
        return  null;
    }
}
