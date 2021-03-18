package com.example.heroku.controller;

import com.example.heroku.request.beer.BeerPackage;
import com.example.heroku.request.beer.BeerUnitDelete;
import com.example.heroku.request.client.UserID;
import com.example.heroku.response.PackgeResponse;
import com.example.heroku.services.UserPackage;
import com.example.heroku.util.Util;
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
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Object> addBeerToPackage(@RequestBody @Valid BeerPackage beerPackage) {
        System.out.println("add beer to package: "+beerPackage.getBeerID());
        return userPackageAPI.AddBeerToPackage(beerPackage);
    }


    @PostMapping("/remove")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.UserPackage> removeFromPckage(@RequestBody @Valid BeerUnitDelete beerUnitDelete){
        return userPackageAPI.DeleteByBeerUnit(beerUnitDelete);
    }


    @PostMapping("/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<PackgeResponse> getAll(@RequestBody @Valid UserID userID){
        System.out.println("Get all my package: "+userID.getId());
        return userPackageAPI.GetMyPackage(userID);
    }

    @PostMapping("/clean")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.UserPackage> clean(@RequestBody @Valid UserID userID){
        System.out.println("clean all my package: "+userID.getId());
        return userPackageAPI.DeleteByUserID(userID);
    }
}
