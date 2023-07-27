package com.example.heroku.services;

import com.example.heroku.model.repository.UserPackageRepository;
import com.example.heroku.request.beer.BeerPackage;
import com.example.heroku.request.beer.PackageItemRemove;
import com.example.heroku.request.client.UserID;
import com.example.heroku.response.Format;
import com.example.heroku.response.PackgeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@Component
public class UserPackage {

    @Autowired
    com.example.heroku.services.Beer beerAPI;


    @Autowired
    UserPackageRepository userPackageRepository;

    public Mono<ResponseEntity<Format>> AddBeerToPackage(BeerPackage beerPackage) {
        com.example.heroku.model.UserPackage[] userPackages = beerPackage.getUserPackage();
        if (userPackages == null)
            return Mono.just(badRequest().body(Format.builder().response("user package empty").build()));
        return Flux.just(userPackages)
                .flatMap(userPackage ->
                        userPackageRepository.AddPackage(userPackage.getDevice_id(),userPackage.getProduct_second_id(), userPackage.getProduct_unit_second_id(), userPackage.getNumber_unit(), userPackage.getStatus())
                )
                .then(Mono.just(ok(Format.builder().response("done").build())));

    }

    public Flux<PackgeResponse> GetMyPackage(UserID userID) {
        return userPackageRepository.GetDevicePackage(userID.getId(), userID.getPage(), userID.getSize())
                .flatMap(userPackage ->
                        beerAPI.GetBeerByID(userPackage.getGroup_id(), userPackage.getProduct_second_id())
                                .map(beerSubmitData -> new PackgeResponse(userPackage).SetBeerData(beerSubmitData))
                );
    }

    public Flux<com.example.heroku.model.UserPackage> DeleteByBeerUnit(PackageItemRemove beerUnitDelete) {
        return userPackageRepository.DeleteProductByBeerUnit(beerUnitDelete.getDevice_id(), beerUnitDelete.getUnit_id());
    }

    public Flux<com.example.heroku.model.UserPackage> DeleteByUserID(UserID userID) {
        return userPackageRepository.DeleteProductByUserID(userID.getId());
    }
}
