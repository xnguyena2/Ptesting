package com.example.heroku.controller;

import com.example.heroku.model.Users;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.permission.WrapPermissionAction;
import com.example.heroku.request.permission.WrapPermissionGroupWithPrincipalAction;
import com.example.heroku.services.ProductImport;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/productimport")
public class ProductImportController {

    @Autowired
    ProductImport productImport;


    @PostMapping("/admin/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.model.ProductImport> addProductImportInfo(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid com.example.heroku.model.ProductImport newRecord) {
        System.out.println("add or update product record: " + newRecord.getProduct_name());
        return WrapPermissionGroupWithPrincipalAction.<com.example.heroku.model.ProductImport>builder()
                .principal(principal)
                .subject(newRecord::getProduct_id)
                .monoAction(() -> productImport.addNewRecord(newRecord))
                .build().toMono();
    }

    @PostMapping("/admin/getbyproductid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.ProductImport> getbyproductid(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchQuery query) {
        System.out.println("getbyproductid: " + query.GetFilterTxt());
        return WrapPermissionAction.<com.example.heroku.model.ProductImport>builder()
                .principal(principal)
                .query(query)
                .fluxAction(q -> productImport.getByProductID(q))
                .build()
                .toFlux();
    }

    @PostMapping("/admin/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.ProductImport> getAll(@AuthenticationPrincipal Mono<Users> principal, @RequestBody @Valid SearchQuery query) {
        System.out.println("getall: " + query.GetFilterTxt());
        return WrapPermissionAction.<com.example.heroku.model.ProductImport>builder()
                .principal(principal)
                .query(query)
                .fluxAction(q -> productImport.getALL(q))
                .build()
                .toFlux();
    }

    @DeleteMapping("/admin/delete/{groupid}/{id}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.model.ProductImport> delete(@AuthenticationPrincipal Mono<Users> principal, @PathVariable("groupid") String groupid, @PathVariable("id") String id) {
        System.out.println("delete record: " + id);
        return WrapPermissionGroupWithPrincipalAction.<com.example.heroku.model.ProductImport>builder()
                .principal(principal)
                .subject(() -> groupid)
                .monoAction(() -> productImport.deleteRecord(groupid, id))
                .build().toMono();
    }
}
