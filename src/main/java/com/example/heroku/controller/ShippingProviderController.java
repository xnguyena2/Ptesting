package com.example.heroku.controller;

import com.example.heroku.model.Users;
import com.example.heroku.request.Order.OrderSearchResult;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.request.ship.ShippingProviderData;
import com.example.heroku.services.ShippingProvider;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public Mono<com.example.heroku.model.ShippingProvider> updateProvider(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid ShippingProviderData config) throws Exception {
        System.out.println("update shipping provider: "+config.getId());
        return WrapPermissionGroupWithPrincipalAction.<com.example.heroku.model.ShippingProvider>builder()
                .principal(principal)
                .subject(config::getGroup_id)
                .monoAction(() -> shippingProviderAPI.CreateShipProvider(config))
                .build().toMono();
    }

    @GetMapping("/admin/get/{groupid}/{id}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.model.ShippingProvider> getProvider(@AuthenticationPrincipal Mono<Users> principal, @PathVariable("groupid") String groupid, @PathVariable("id") String id){
        return WrapPermissionGroupWithPrincipalAction.<com.example.heroku.model.ShippingProvider>builder()
                .principal(principal)
                .subject(() -> groupid)
                .monoAction(() -> shippingProviderAPI.GetShipProvider(groupid, id))
                .build().toMono();
    }
}
