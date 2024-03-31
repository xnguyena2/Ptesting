package com.example.heroku;

import com.example.heroku.model.ProductImport;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.warehouse.GroupImportWithItem;
import com.example.heroku.services.GroupImport;
import lombok.Builder;
import reactor.test.StepVerifier;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class ProductImportTest {
    private GroupImport groupImport;

    String group;

    public void Test(){
        groupImport.SaveGroupImport(
                GroupImportWithItem.builder()
                        .group_id(group)
                        .group_import_second_id("1")
                        .supplier_id("supplier")
                        .total_amount(23)
                        .total_amount(32)
                        .discount_amount(12)
                        .additional_fee(10)
                        .note("note")
                        .images("images")
                        .type(ProductImport.ImportType.IMPORT)
                        .items(
                                new ProductImport[]{
                                        ProductImport.builder()
                                                .product_import_second_id("11")
                                                .product_second_id("123")
                                                .product_unit_second_id("234")
                                                .product_unit_name_category("lon")
                                                .price(22)
                                                .amount(33)
                                                .note("note")
                                                .type(ProductImport.ImportType.IMPORT)
                                                .build(),
                                        ProductImport.builder()
                                                .product_import_second_id("22")
                                                .product_second_id("123")
                                                .product_unit_second_id("345")
                                                .product_unit_name_category("thung")
                                                .price(44)
                                                .amount(55)
                                                .note("note")
                                                .type(ProductImport.ImportType.IMPORT)
                                                .build()
                                }
                        )
                        .build()
        )
                .block();

//        productImport.getByProductID(SearchQuery.builder().query("123").page(0).size(100).filter("10").group_id(group).build())
//                .sort(Comparator.comparing(com.example.heroku.model.ProductImport::getCreateat))
//                .as(StepVerifier::create)
//                .consumeNextWith(productImport -> {
//                    assertThat(productImport.getAmount()).isEqualTo(10);
//                })
//                .consumeNextWith(productImport -> {
//                    assertThat(productImport.getAmount()).isEqualTo(5);
//                })
//                .verifyComplete();

        groupImport.GetAllWorking(SearchQuery.builder().page(0).size(100).filter("10").group_id(group).build())
                .sort(Comparator.comparing(GroupImportWithItem::getCreateat))
                .as(StepVerifier::create)
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getGroup_id()).isEqualTo(group);
                    assertThat(productImport.getGroup_import_second_id()).isEqualTo("1");
                    assertThat(productImport.getItems().length).isEqualTo(2);
                })
                .verifyComplete();
//
//
//        productImport.deleteRecord(group, "2")
//                .block();
//
//        productImport.getByProductID(SearchQuery.builder().query("123").page(0).size(100).filter("10").group_id(group).build())
//                .sort(Comparator.comparing(com.example.heroku.model.ProductImport::getCreateat))
//                .as(StepVerifier::create)
//                .consumeNextWith(productImport -> {
//                    assertThat(productImport.getAmount()).isEqualTo(10);
//                })
//                .verifyComplete();
//
//        productImport.GetAllWorking(SearchQuery.builder().page(0).size(100).filter("10").group_id(group).build())
//                .sort(Comparator.comparing(com.example.heroku.model.ProductImport::getCreateat))
//                .as(StepVerifier::create)
//                .consumeNextWith(productImport -> {
//                    assertThat(productImport.getAmount()).isEqualTo(10);
//                })
//                .verifyComplete();
    }
}
