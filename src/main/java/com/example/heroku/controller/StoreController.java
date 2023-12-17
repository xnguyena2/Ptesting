package com.example.heroku.controller;

import com.example.heroku.model.Store;
import com.example.heroku.request.store.StoreInitData;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private com.example.heroku.services.Store storeServices;

    @PostMapping("/initial")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Store> initialStore(@RequestBody @Valid StoreInitData storeInitData) {
        return storeServices.initialStore(storeInitData);
    }
}
