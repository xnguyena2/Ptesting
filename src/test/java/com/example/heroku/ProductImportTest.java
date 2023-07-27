package com.example.heroku;

import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.services.ProductImport;
import lombok.Builder;
import reactor.test.StepVerifier;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class ProductImportTest {
    private ProductImport productImport;

    public void Test(){
        productImport.addNewRecord(
                com.example.heroku.model.ProductImport.builder()
                        .product_import_second_id("1")
                        .price(1000)
                        .amount(10)
                        .product_id("123")
                        .group_id(Config.group)
                        .build()
        )
                .block();
        productImport.addNewRecord(
                        com.example.heroku.model.ProductImport.builder()
                                .product_import_second_id("2")
                                .price(1000)
                                .amount(5)
                                .product_id("123")
                                .group_id(Config.group)
                                .build()
                )
                .block();

        productImport.getByProductID(SearchQuery.builder().query("123").page(0).size(100).filter("10").group_id(Config.group).build())
                .sort(Comparator.comparing(com.example.heroku.model.ProductImport::getCreateat))
                .as(StepVerifier::create)
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getAmount()).isEqualTo(10);
                })
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getAmount()).isEqualTo(5);
                })
                .verifyComplete();

        productImport.getALL(SearchQuery.builder().page(0).size(100).filter("10").group_id(Config.group).build())
                .sort(Comparator.comparing(com.example.heroku.model.ProductImport::getCreateat))
                .as(StepVerifier::create)
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getAmount()).isEqualTo(10);
                })
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getAmount()).isEqualTo(5);
                })
                .verifyComplete();


        productImport.deleteRecord(Config.group, "2")
                .block();

        productImport.getByProductID(SearchQuery.builder().query("123").page(0).size(100).filter("10").group_id(Config.group).build())
                .sort(Comparator.comparing(com.example.heroku.model.ProductImport::getCreateat))
                .as(StepVerifier::create)
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getAmount()).isEqualTo(10);
                })
                .verifyComplete();

        productImport.getALL(SearchQuery.builder().page(0).size(100).filter("10").group_id(Config.group).build())
                .sort(Comparator.comparing(com.example.heroku.model.ProductImport::getCreateat))
                .as(StepVerifier::create)
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getAmount()).isEqualTo(10);
                })
                .verifyComplete();
    }
}
