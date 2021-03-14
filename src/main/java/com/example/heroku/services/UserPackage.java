package com.example.heroku.services;

import com.example.heroku.model.repository.UserPackageRepository;
import com.example.heroku.request.beer.BeerPackage;
import com.example.heroku.request.beer.BeerUnitDelete;
import com.example.heroku.request.client.UserID;
import com.example.heroku.response.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.ok;

@Component
public class UserPackage {
    @Autowired
    UserPackageRepository userPackageRepository;

    public Mono<Object> AddBeerToPackage(BeerPackage beerPackage) {
        com.example.heroku.model.UserPackage[] userPackages = beerPackage.getUserPackage();
        if(userPackages == null)
            return Mono.just(org.springframework.http.ResponseEntity.badRequest());
        return Flux.just(userPackages)
                .flatMap(userPackage ->
                        userPackageRepository.save(userPackage)
                )
                .then(Mono.just(ok(Format.builder().response("done").build())));

    }

    public Flux<com.example.heroku.model.UserPackage> GetMyPackage(UserID userID){
        return userPackageRepository.GetDevicePackage(userID.getId(), userID.getPage(), userID.getSize());
    }

    public Flux<com.example.heroku.model.UserPackage> DeleteByBeerUnit(BeerUnitDelete beerUnitDelete) {
        return userPackageRepository.DeleteProductByBeerUnit(beerUnitDelete.getId());
    }

        public Flux<com.example.heroku.model.UserPackage> DeleteByUserID(UserID userID){
            return userPackageRepository.DeleteProductByUserID(userID.getId());
    }
}
