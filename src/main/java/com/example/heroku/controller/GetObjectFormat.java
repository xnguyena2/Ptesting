package com.example.heroku.controller;

import com.example.heroku.model.Buyer;
import com.example.heroku.model.UserPackage;
import com.example.heroku.request.beer.ProductPackage;
import com.example.heroku.response.Format;
import com.example.heroku.util.Util;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/getobjectjs")
public class GetObjectFormat {
    @GetMapping("/productpackage")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ProductPackage> getObj() {
        return Mono.just(ProductPackage.builder()
                .buyer(Buyer.builder()
                        .build())
                .product_units(
                        new UserPackage[]{UserPackage.builder().build()}
                )
                .build());
    }
}
