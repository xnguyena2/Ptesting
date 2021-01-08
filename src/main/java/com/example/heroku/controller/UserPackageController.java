package com.example.heroku.controller;

import com.example.heroku.request.beer.BeerPackage;
import com.example.heroku.services.UserPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/package")
public class UserPackageController {

    @Autowired
    UserPackage userPackageAPI;

    @PostMapping("/add")
    @CrossOrigin(origins = "http://localhost:4200")
    public Mono<Object> addBeerToPackage(@RequestBody @Valid BeerPackage beerPackage) {
        System.out.println("add beer to package: "+beerPackage.getBeerID());
        return userPackageAPI.AddBeerToPackage(beerPackage);
    }

    @GetMapping("/getall/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public Flux<com.example.heroku.model.UserPackage> getIMGbyID(@PathVariable("id") String deviceID){
        return userPackageAPI.GetMyPackage(deviceID, 0, 1000);
    }
}
