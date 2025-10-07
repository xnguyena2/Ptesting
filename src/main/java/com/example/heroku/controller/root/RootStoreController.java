package com.example.heroku.controller.root;

import com.example.heroku.model.Store;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.warehouse.SearchImportQuery;
import com.example.heroku.services.DeleteAllData;
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

    @Autowired
    private DeleteAllData deleteAllData;

    @PostMapping("/updatepaymentstatus")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Store> updateStore(@RequestBody @Valid Store store) {
        System.out.println("ADMIN Update store payment status: " + store.getGroup_id() + " to " + store.getPayment_status());
        return storeServices.updatePaymentStatus(store.getGroup_id(), store.getPayment_status());
    }

    @GetMapping("/search/{txt}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Store> search(@PathVariable("txt") String txt) {
        System.out.println("ADMIN Search store: " + txt);
        return storeServices.findStore(txt);
    }

    @GetMapping("/searchbypackage/{txt}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Store> searchbypackage(@PathVariable("txt") String txt) {
        System.out.println("ADMIN Search store by package: " + txt);
        return storeServices.getAllStoreBaseonDonePackage(txt);
    }

    @GetMapping("/allpaid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Store> getAllPaid() {
        System.out.println("ADMIN get all paid!");
        return storeServices.getAllPaid();
    }

    @PostMapping("/getallbetween")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Store> getAllBetween(@RequestBody @Valid SearchImportQuery searchImportQuery) {
        System.out.println("ADMIN Get all store between: " + searchImportQuery.getFrom() + " and " + searchImportQuery.getTo());
        return storeServices.getAllStoreCreateBetween(searchImportQuery);
    }

    @PostMapping("/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<Store> getAll(@RequestBody @Valid SearchImportQuery searchImportQuery) {
        System.out.println("ADMIN Get all store: " + searchImportQuery.getSearch_txt());
        return storeServices.getAllStore(searchImportQuery);
    }

    @PostMapping("/deleteallstoredata")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<Boolean> deleteAllStoreData(@RequestBody @Valid IDContainer idContainer) {
        System.out.println("ADMIN Delete all store data: " + idContainer.getGroup_id());
        return deleteAllData.deleteByGroupID(idContainer.getGroup_id());
    }
}
