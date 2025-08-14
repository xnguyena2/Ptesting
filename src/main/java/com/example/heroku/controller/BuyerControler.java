package com.example.heroku.controller;

import com.example.heroku.model.Buyer;
import com.example.heroku.model.Users;
import com.example.heroku.model.statistics.DebtOfBuyer;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.request.permission.WrapPermissionAction;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.response.BuyerData;
import com.example.heroku.response.BuyerStatictisData;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Flux<DebtOfBuyer> getAll(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchQuery query) {
        System.out.println("group_id: " + query.getGroup_id() + ", buyer getall, " + " page: " + query.getPage() + " size: " + query.getSize());
        return WrapPermissionAction.<DebtOfBuyer>builder()
                .principal(principal)
                .query(query)
                .fluxAction(q -> buyerServices.GetAllDirectWithoutEmpty(q))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/getdetail")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<BuyerStatictisData> getDetail(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid PackageID packageID) {
        System.out.println("group_id: " + packageID.getGroup_id() + ", buyer getdetail, " + " id: " + packageID.getDevice_id() + ", from: " + packageID.getFrom() + ", to: " + packageID.getTo());
        return WrapPermissionGroupWithPrincipalAction.<BuyerStatictisData>builder()
                .principal(principal)
                .subject(packageID::getGroup_id)
                .monoAction(() -> buyerServices.getBuyerStatictis(packageID))
                .build().toMono();
    }

    @PostMapping("/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Buyer> create(@RequestBody @Valid Buyer buyer) {
        System.out.println("group_id: " + buyer.getGroup_id() + ", buyer create, " + " id: " + buyer.getDevice_id());
        return buyerServices.insertOrUpdate(buyer.AutoFill(), 0, 0, 0, 0, 0);
    }

    @PostMapping("/admin/delete")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Buyer> create(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid IDContainer idContainer) {
        System.out.println("group_id: " + idContainer.getGroup_id() + ", buyer delete, " + " id: " + idContainer.getId());
        return WrapPermissionGroupWithPrincipalAction.<Buyer>builder()
                .principal(principal)
                .subject(idContainer::getGroup_id)
                .monoAction(() -> buyerServices.deleteBuyer(idContainer.getGroup_id(), idContainer.getId()))
                .build().toMono();
    }

    @PostMapping("/search")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BuyerData> searchByPhone(@RequestBody @Valid SearchQuery searchQuery) {
        System.out.println("group_id: " + searchQuery.getGroup_id() + ", buyer search, " + " query: " + searchQuery.getQuery());
        return buyerServices.FindByPhone(searchQuery);
    }

    @PostMapping("/admin/getbyid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<BuyerData> getByID(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid IDContainer idContainer) {
        System.out.println("group_id: " + idContainer.getGroup_id() + ", buyer getbyid, " + " id: " + idContainer.getId());
        return WrapPermissionGroupWithPrincipalAction.<BuyerData>builder()
                .principal(principal)
                .subject(idContainer::getGroup_id)
                .monoAction(() -> buyerServices.FindByDeviceID(idContainer.getGroup_id(), idContainer.getId()))
                .build()
                .toMono();
    }

}
