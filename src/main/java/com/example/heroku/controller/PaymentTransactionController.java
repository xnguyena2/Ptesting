package com.example.heroku.controller;

import com.example.heroku.model.Users;
import com.example.heroku.request.beer.BeerInfo;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.services.PaymentTransation;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/transaction")
public class PaymentTransactionController {
    @Autowired
    PaymentTransation paymentTransation;

    @PostMapping("/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.model.PaymentTransation> addTransactionInfo(@RequestBody @Valid com.example.heroku.model.PaymentTransation transation) {
        return paymentTransation.insertOrUpdate(transation);
    }

    @DeleteMapping("/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.model.PaymentTransation> deleteTransaction(@RequestBody @Valid IDContainer idContainer) {
        System.out.println("delete transaction: " + idContainer.getId());
        return paymentTransation.delete(idContainer);
    }

    @GetMapping("/getbypackage")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.PaymentTransation> getByPackageID(@RequestBody @Valid IDContainer idContainer) {
        System.out.println("get transaction of package id: " + idContainer.getId());
        return paymentTransation.getAllTransactionByPackageID(idContainer);
    }

    @GetMapping("/getbytime")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.PaymentTransation> getBettwenTime(@RequestBody @Valid PackageID packageID) {
        System.out.println("get transaction of group id: " + packageID.getGroup_id());
        return paymentTransation.getAllTransactionBettwen(packageID);
    }
}
