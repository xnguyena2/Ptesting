package com.example.heroku.controller;

import com.example.heroku.model.PackageOrder;
import com.example.heroku.request.Order.OrderSearchResult;
import com.example.heroku.request.beer.PackageOrderData;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.services.BeerOrder;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
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
        return beerOrder.createOrder((packageOrderData));
    }


    @PostMapping("/admin/all")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<OrderSearchResult> getIMGbyID(@RequestBody @Valid SearchQuery query) {
        return packageOrder.CountGetAllOrder(query);
    }


    @GetMapping("/admin/detail/{id}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<OrderSearchResult.PackageOrderData> detail(@PathVariable("id") String id) {
        System.out.println("Detail of order: " + id);
        return packageOrder.getOrderDetail(id);
    }


    @PostMapping("/admin/done")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<PackageOrder> closeOrder(@RequestBody @Valid IDContainer order) {
        System.out.println("close order: " + order.getId());
        return packageOrder.UpdateStatus(order.getId(), PackageOrder.Status.DONE);
    }
}
