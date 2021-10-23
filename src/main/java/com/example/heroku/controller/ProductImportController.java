package com.example.heroku.controller;

import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.response.Format;
import com.example.heroku.services.ProductImport;
import com.example.heroku.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/productimport")
public class ProductImportController {

    @Autowired
    ProductImport productImport;

    @GetMapping("/admin/generateid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ResponseEntity<Format>> generateID() {
        return productImport.generateID();
    }

    @PostMapping("/admin/create")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.model.ProductImport> addProductImportInfo(@RequestBody @Valid com.example.heroku.model.ProductImport newRecord) {
        System.out.println("add or update product record: " + newRecord.getProduct_name());
        return productImport.addNewRecord(newRecord);
    }

    @PostMapping("/admin/getbyproductid")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.ProductImport> getbyproductid(@RequestBody @Valid SearchQuery query) {
        System.out.println("getbyproductid: " + query.GetFilterTxt());
        return productImport.getByProductID(query);
    }

    @PostMapping("/admin/getall")
    @CrossOrigin(origins = Util.HOST_URL)
    public Flux<com.example.heroku.model.ProductImport> getAll(@RequestBody @Valid SearchQuery query) {
        System.out.println("getall: " + query.GetFilterTxt());
        return productImport.getALL(query);
    }

    @DeleteMapping("/admin/delete/{id}")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<com.example.heroku.model.ProductImport> delete(@PathVariable("id") String id) {
        System.out.println("delete record: " + id);
        return productImport.deleteRecord(id);
    }
}
