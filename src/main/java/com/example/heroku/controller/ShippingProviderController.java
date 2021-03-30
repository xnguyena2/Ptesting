package com.example.heroku.controller;

import com.example.heroku.request.ship.ShippingProviderData;
import com.example.heroku.services.ShippingProvider;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/shippingprovider")
public class ShippingProviderController {
    @Autowired
    ShippingProvider shippingProviderAPI;

    @PostMapping("/admin/update")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.model.ShippingProvider> updateProvider(@RequestBody @Valid ShippingProviderData config) throws Exception {
        System.out.println("update shipping provider: "+config.getId());
        return shippingProviderAPI.CreateShipProvider(config.getId(), config.getJson());
    }

    @GetMapping("/admin/get/{id}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.model.ShippingProvider> getProvider(@PathVariable("id") String id){
        return shippingProviderAPI.GetShipProvider(id);
    }
}
