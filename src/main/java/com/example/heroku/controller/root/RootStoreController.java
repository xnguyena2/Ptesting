package com.example.heroku.controller.root;

import com.example.heroku.model.Store;
import com.example.heroku.request.warehouse.SearchImportQuery;
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
    public Flux<Store> search(@PathVariable("txt") String txt) {
        return storeServices.findStore(txt);
    }

    @GetMapping("/searchbypackage/{txt}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Store> searchbypackage(@PathVariable("txt") String txt) {
        return storeServices.getAllStoreBaseonDonePackage(txt);
    }

    @PostMapping("/getallbetween")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Store> getAllBetween(@RequestBody @Valid SearchImportQuery searchImportQuery) {
        return storeServices.getAllStoreCreateBetween(searchImportQuery);
    }
}
