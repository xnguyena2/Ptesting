package com.example.heroku.controller;

import com.example.heroku.client.payment.PaymentValid;
import com.example.heroku.model.Users;
import com.example.heroku.model.statistics.*;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.request.permission.WrapPermissionAction;
import com.example.heroku.response.BenifitOfOrderAndPaymentTransactionByDate;
import com.example.heroku.response.BenifitOfOrderAndPaymentTransactionByHour;
import com.example.heroku.services.StatisticServices;
import com.example.heroku.services.Store;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/packageorderstatistic")
public class StatisticPackageOrderDetailController {
    @Autowired
    StatisticServices statisticServices;

    @Autowired
    Store store;

    @PostMapping("/admin/getfinalbenifitbydate")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<BenifitOfOrderAndPaymentTransactionByDate> getFinalBenifitByDate(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid PackageID query) {
        System.out.println("get final benifit by date: " + query.getGroup_id() + ", from: " + query.getFrom() + ", to: " + query.getTo());
        return WrapPermissionAction.<BenifitOfOrderAndPaymentTransactionByDate>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .monoAction(q -> statisticServices.getBenifitOfPaymentByDateStatictis(query))
                .build()
                .toMono();
    }

    @PostMapping("/admin/getfinalbenifitbyhour")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<BenifitOfOrderAndPaymentTransactionByHour> getFinalBenifitByHour(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid PackageID query) {
        System.out.println("get final benifig by hour: " + query.getGroup_id() + ", from: " + query.getFrom() + ", to: " + query.getTo());
        return WrapPermissionAction.<BenifitOfOrderAndPaymentTransactionByHour>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .monoAction(q -> statisticServices.getBenifitOfPaymentByHourStatictis(query))
                .build()
                .toMono();
    }

    @PostMapping("/admin/getbyproductid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BenifitByDate> getbyproductid(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid PackageID query) {
        System.out.println("get report getbyproductid: " + query.getGroup_id() + ", from: " + query.getFrom() + ", to: " + query.getTo());
        return WrapPermissionAction.<BenifitByDate>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> PaymentValid.<BenifitByDate>builder()
                        .subject(q::getGroup_id)
                        .store(store)
                        .fluxAction(() ->
                                statisticServices.getPackageStatictis(query)
                        )
                        .filterAction(value -> {
                            value.setProfit(0);
                            return value;
                        })
                        .build().toFluxFilter())
                .build()
                .toFlux();
    }

    @PostMapping("/admin/getbyhour")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BenifitByDateHour> getbyproductidofhours(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid PackageID query) {
        System.out.println("get report getbyproductid of hours: " + query.getGroup_id() + ", from: " + query.getFrom() + ", to: " + query.getTo());
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
        System.out.println("getTotalbyproductid: " + query.getGroup_id() + ", from: " + query.getFrom() + ", to: " + query.getTo());
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
        System.out.println("get by product: " + query.getGroup_id() + ", from: " + query.getFrom() + ", to: " + query.getTo());
        return WrapPermissionAction.<BenifitByProduct>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> PaymentValid.<BenifitByProduct>builder()
                        .subject(q::getGroup_id)
                        .store(store)
                        .fluxAction(() ->
                                statisticServices.getProductBenifitStatictis(query)
                        )
                        .filterAction(value -> {
                            value.setProfit(0);
                            return value;
                        })
                        .build().toFluxFilter())
                .build()
                .toFlux();
    }

    @PostMapping("/admin/buyer")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BenifitByBuyer> getBuyerBenifitStatictis(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid PackageID query) {
        System.out.println("get by buyer: " + query.getGroup_id() + ", from: " + query.getFrom() + ", to: " + query.getTo());
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
        System.out.println("get by staff: " + query.getGroup_id() + ", from: " + query.getFrom() + ", to: " + query.getTo());
        return WrapPermissionAction.<BenifitByBuyer>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> statisticServices.getStaffBenifitStatictis(query))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/order")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<BenifitByOrder> getOrderBenifitStatictis(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid PackageID query) {
        System.out.println("get by order: " + query.getGroup_id() + ", from: " + query.getFrom() + ", to: " + query.getTo());
        return WrapPermissionAction.<BenifitByOrder>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .fluxAction(q -> PaymentValid.<BenifitByOrder>builder()
                        .subject(q::getGroup_id)
                        .store(store)
                        .fluxAction(() ->
                                statisticServices.getOrderBenifitStatictis(query)
                        )
                        .filterAction(value -> {
                            value.setProfit(0);
                            return value;
                        })
                        .build().toFluxFilter())
                .build()
                .toFlux();
    }

    @PostMapping("/admin/countreturncancel")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<CountOrderByDate> getOrderErrorStatictis(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid PackageID query) {
        System.out.println("get by order: " + query.getGroup_id() + ", from: " + query.getFrom() + ", to: " + query.getTo());
        return WrapPermissionAction.<CountOrderByDate>builder()
                .principal(principal)
                .query(SearchQuery.builder().group_id(query.getGroup_id()).build())
                .monoAction(q -> statisticServices.getCountCancelReturnStatictis(query))
                .build()
                .toMono();
    }
}
