package com.example.heroku.controller.root;

import com.example.heroku.model.Store;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/root/store")
public class RootStoreController {

    @Autowired
    private com.example.heroku.services.Store storeServices;

    @PostMapping("/updatepaymentstatus")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Store> updateStore(@RequestBody @Valid Store store) {
        return storeServices.updatePaymentStatus(store.getGroup_id(), store.getPayment_status());
    }

    @GetMapping("/search/{txt}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Store> updateStore(@PathVariable("txt") String txt) {
        return storeServices.findStore(txt);
    }
}
