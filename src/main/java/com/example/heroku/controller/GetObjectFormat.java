package com.example.heroku.controller;

import com.example.heroku.model.Buyer;
import com.example.heroku.model.Image;
import com.example.heroku.model.UserPackage;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.ProductPackage;
import com.example.heroku.request.beer.SearchResult;
import com.example.heroku.response.AreaData;
import com.example.heroku.response.Format;
import com.example.heroku.response.TableDetailData;
import com.example.heroku.util.Util;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/getobjectjs")
public class GetObjectFormat {
    @GetMapping("/productpackage")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<ProductPackage> getPackageObj() {
        return Mono.just(ProductPackage.builder()
                .buyer(Buyer.builder()
                        .build())
                .product_units(
                        new UserPackage[]{UserPackage.builder().build()}
                )
                .build());
    }

    @GetMapping("/area")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<AreaData> getAreaObj() {
        return Mono.just(
                AreaData.builder()
                        .listTable(Arrays.asList(new TableDetailData[]{
                                TableDetailData.builder()
                                        .area_id("not exist")
                                        .table_name("table 1 of area 1")
                                        .build()
                        }))
                        .build());
    }

    @GetMapping("/product")
    @CrossOrigin(origins = Util.HOST_URL)
    public Mono<SearchResult<BeerSubmitData>> getProductObj() {
        SearchResult<BeerSubmitData> result = new SearchResult<>();
        result.Add(BeerSubmitData.builder()
                        .listUnit(new BeerSubmitData.BeerUnit[]{
                                BeerSubmitData.BeerUnit.builder().build()
                        })
                        .images(
                                Arrays.asList(new Image[]{
                                        Image.builder().build()
                                })
                        )
                .build());
        return Mono.just(result);
    }
}
