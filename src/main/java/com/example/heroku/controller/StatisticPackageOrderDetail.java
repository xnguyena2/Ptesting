package com.example.heroku.controller;

import com.example.heroku.model.Users;
import com.example.heroku.model.statistics.*;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.request.permission.WrapPermissionAction;
import com.example.heroku.response.ProductOrderStatus;
import com.example.heroku.services.StatisticServices;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/packageorderstatistic")
public class StatisticPackageOrderDetail {
    @Autowired
    StatisticServices statisticServices;

    @PostMapping("/admin/getbyproductid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BenifitByDate> getbyproductid(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid PackageID query) {
        System.out.println("getbyproductid: " + query.getGroup_id());
        return WrapPermissionAction.<BenifitByDate>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> statisticServices.getPackageStatictis(query))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/getbyhour")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BenifitByDateHour> getbyproductidofhours(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid PackageID query) {
        System.out.println("getbyproductid of hours: " + query.getGroup_id());
        return WrapPermissionAction.<BenifitByDateHour>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> statisticServices.getPackageStatictisByHour(query))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/totalinmonth")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<BenifitByMonth> getTotalbyproductid(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid PackageID query) {
        System.out.println("getTotalbyproductid: " + query.getGroup_id());
        return WrapPermissionAction.<BenifitByMonth>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .monoAction(q -> statisticServices.getPackageTotalStatictis(query))
                .build()
                .toMono();
    }

    @PostMapping("/admin/product")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BenifitByProduct> getProductBenifitStatictis(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid PackageID query) {
        System.out.println("get by product: " + query.getGroup_id());
        return WrapPermissionAction.<BenifitByProduct>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> statisticServices.getProductBenifitStatictis(query))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/buyer")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BenifitByBuyer> getBuyerBenifitStatictis(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid PackageID query) {
        System.out.println("get by buyer: " + query.getGroup_id());
        return WrapPermissionAction.<BenifitByBuyer>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> statisticServices.getBuyerBenifitStatictis(query))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/staff")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BenifitByBuyer> getStaffBenifitStatictis(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid PackageID query) {
        System.out.println("get by staff: " + query.getGroup_id());
        return WrapPermissionAction.<BenifitByBuyer>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> statisticServices.getStaffBenifitStatictis(query))
                .build()
                .toFlux();
    }
}
