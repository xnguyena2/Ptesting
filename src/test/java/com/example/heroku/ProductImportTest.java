package com.example.heroku;

import com.example.heroku.model.BeerUnit;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.services.ProductImport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class ProductImportTest {
    private ProductImport productImport;

    public void Test(){
        productImport.addNewRecord(
                com.example.heroku.model.ProductImport.builder()
                        .amount(10)
                        .product_id("123")
                        .build()
        )
                .block();
        productImport.addNewRecord(
                        com.example.heroku.model.ProductImport.builder()
                                .amount(5)
                                .product_id("123")
                                .build()
                )
                .block();

        productImport.getByProductID(SearchQuery.builder().query("123").page(0).size(100).filter("10").build())
                .sort(Comparator.comparing(com.example.heroku.model.ProductImport::getCreateat))
                .as(StepVerifier::create)
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getAmount()).isEqualTo(10);
                })
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getAmount()).isEqualTo(5);
                })
                .verifyComplete();
    }
}
