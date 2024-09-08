package com.example.heroku.controller;

import com.example.heroku.model.statistics.DebtOfBuyer;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.request.client.UserID;
import com.example.heroku.services.DebtTransation;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/debt")
public class DebtTransactionController {
    @Autowired
    DebtTransation debtTransation;

    @PostMapping("/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.model.DebtTransation> addTransactionInfo(@RequestBody @Valid com.example.heroku.model.DebtTransation transation) {
        System.out.println("add or update debt: " + transation.getTransaction_second_id() + ", group: " + transation.getGroup_id());
        return debtTransation.insertOrUpdate(transation);
    }

    @DeleteMapping("/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.model.DebtTransation> deleteTransaction(@RequestBody @Valid IDContainer idContainer) {
        System.out.println("delete debt: " + idContainer.getId());
        return debtTransation.delete(idContainer);
    }

    @GetMapping("/getbypackage")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.DebtTransation> getByPackageID(@RequestBody @Valid IDContainer idContainer) {
        System.out.println("get debt of package id: " + idContainer.getId());
        return debtTransation.getAllTransactionByPackageID(idContainer);
    }

    @PostMapping("/getcategory")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.DebtTransation> getCategory(@RequestBody @Valid IDContainer idContainer) {
        System.out.println("get all category of debt transaction of group id: " + idContainer.getGroup_id());
        return debtTransation.getAllCategory(idContainer);
    }

    @PostMapping("/getbytime")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.DebtTransation> getBettwenTime(@RequestBody @Valid PackageID packageID) {
        System.out.println("get debt of group id: " + packageID.getGroup_id());
        return debtTransation.getAllTransactionBettwen(packageID);
    }

    @PostMapping("/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.DebtTransation> getAll(@RequestBody @Valid UserID userID) {
        System.out.println("get all debt of group id: " + userID.getGroup_id());
        return debtTransation.getAllTransaction(userID);
    }

    @PostMapping("/getallofbuyer")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.DebtTransation> getAllOfBuyer(@RequestBody @Valid UserID userID) {
        System.out.println("get all debt of buyer group id: " + userID.getGroup_id());
        return debtTransation.getAllTransactionOfBuyer(userID);
    }

    @PostMapping("/getallbuyer")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<DebtOfBuyer> getAllOfBuyer(@RequestBody @Valid IDContainer idContainer) {
        System.out.println("get all debt of all buyer group id: " + idContainer.getGroup_id());
        return debtTransation.getDebtOfAllBuyer(idContainer);
    }
}
