package com.example.heroku.controller;

import com.example.heroku.request.beer.ProductPackage;
import com.example.heroku.request.beer.PackageItemRemove;
import com.example.heroku.request.client.UserID;
import com.example.heroku.response.Format;
import com.example.heroku.response.PackageDataResponse;
import com.example.heroku.response.ProductInPackageResponse;
import com.example.heroku.services.UserPackage;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public Mono<ResponseEntity<Format>> addBeerToPackage(@RequestBody @Valid ProductPackage productPackage) {
        System.out.println("add beer to package: "+ productPackage.getPackage_second_id());
        return userPackageAPI.AddProductToPackage(productPackage);
    }

    @PostMapping("/update")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> addOrUpdatePackage(@RequestBody @Valid ProductPackage productPackage) {
        System.out.println("save package: "+ productPackage.getPackage_second_id());
        return userPackageAPI.SavePackage(productPackage);
    }


    @PostMapping("/remove")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.UserPackage> removeFromPckage(@RequestBody @Valid PackageItemRemove beerUnitDelete){
        return userPackageAPI.DeleteByBeerUnit(beerUnitDelete);
    }


    @PostMapping("/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<PackageDataResponse> getAll(@RequestBody @Valid UserID userID){
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
