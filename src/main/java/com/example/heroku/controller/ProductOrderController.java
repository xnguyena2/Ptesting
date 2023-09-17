package com.example.heroku.controller;

import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.Users;
import com.example.heroku.request.order.CloseOrderRequest;
import com.example.heroku.request.order.OrderSearchResult;
import com.example.heroku.request.beer.PackageOrderData;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.permission.WrapPermissionAction;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.services.BeerOrder;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/order")
public class ProductOrderController {

    @Autowired
    BeerOrder beerOrder;

    @Autowired
    com.example.heroku.services.PackageOrder packageOrder;


    @PostMapping("/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<PackageOrder> addBeerInfo(@RequestBody @Valid PackageOrderData packageOrderData) {
        System.out.println("create new order: " + packageOrderData.getPackageOrder().getUser_device_id());
        return beerOrder.createOrder(packageOrderData);
    }


    @PostMapping("/admin/all")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<OrderSearchResult> getIMGbyID(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchQuery query) {
        return WrapPermissionAction.<OrderSearchResult>builder()
                .principal(principal)
                .query(query)
                .monoAction(q -> packageOrder.CountGetAllOrder(q))
                .build()
                .toMono();
    }


    @GetMapping("/admin/detail/{groupid}/{id}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<OrderSearchResult.PackageOrderData> detail(@AuthenticationPrincipal Mono<Users> principal, @PathVariable("groupid") String groupid, @PathVariable("id") String id) {
        System.out.println("Detail of order: " + id);
        return WrapPermissionGroupWithPrincipalAction.<OrderSearchResult.PackageOrderData>builder()
                .principal(principal)
                .subject(() -> groupid)
                .monoAction(() -> packageOrder.getOrderDetail(groupid, id))
                .build().toMono();
    }


    @PostMapping("/admin/close")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<PackageOrder> closeOrder(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid CloseOrderRequest order) {
        System.out.println("close order: " + order.getId());
        PackageOrder.Status status = PackageOrder.Status.get(order.getStatus());
        return WrapPermissionGroupWithPrincipalAction.<PackageOrder>builder()
                .principal(principal)
                .subject(order::getGroup_id)
                .monoAction(() -> packageOrder.UpdateStatus(order.getGroup_id(), order.getId(), status))
                .build().toMono();
    }
}
