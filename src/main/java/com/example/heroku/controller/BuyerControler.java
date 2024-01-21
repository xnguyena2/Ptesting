package com.example.heroku.controller;

import com.example.heroku.model.Buyer;
import com.example.heroku.model.Image;
import com.example.heroku.model.Users;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.request.permission.WrapPermissionAction;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.response.BuyerData;
import com.example.heroku.response.BuyerStatictisData;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/buyer")
public class BuyerControler {

    @Autowired
    com.example.heroku.services.Buyer buyerServices;

    @PostMapping("/admin/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BuyerData> getAll(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchQuery query) {
        return WrapPermissionAction.<BuyerData>builder()
                .principal(principal)
                .query(query)
                .fluxAction(q -> buyerServices.GetAll(q))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/getdetail")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<BuyerStatictisData> getDetail(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid PackageID packageID) {
        return WrapPermissionGroupWithPrincipalAction.<BuyerStatictisData>builder()
                .principal(principal)
                .subject(packageID::getGroup_id)
                .monoAction(() -> buyerServices.getBuyerStatictis(packageID))
                .build().toMono();
    }

    @PostMapping("/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Buyer> create(@RequestBody @Valid Buyer buyer) {
        return buyerServices.insertOrUpdate(buyer.AutoFill(),0,0,0,0);
    }

    @PostMapping("/admin/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Buyer> create(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid IDContainer idContainer) {
        return WrapPermissionGroupWithPrincipalAction.<Buyer>builder()
                .principal(principal)
                .subject(idContainer::getGroup_id)
                .monoAction(() -> buyerServices.deleteBuyer(idContainer.getGroup_id(), idContainer.getId()))
                .build().toMono();
    }

    @PostMapping("/search")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BuyerData> searchByPhone(@RequestBody @Valid SearchQuery searchQuery) {
        return buyerServices.FindByPhone(searchQuery);
    }

}
