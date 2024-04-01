package com.example.heroku;

import com.example.heroku.model.ProductImport;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.warehouse.GroupImportWithItem;
import com.example.heroku.services.GroupImport;
import lombok.Builder;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class ProductImportTest {
    private GroupImport groupImport;

    com.example.heroku.services.Beer beerAPI;

    String group;

    public void Test(){


        AtomicReference<String> beerUnit1ID = new AtomicReference<String>();
        AtomicReference<String> beerUnit2ID = new AtomicReference<String>();
        this.beerAPI.GetBeerByID(group, "123")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getProduct().getProduct_second_id()).isEqualTo("123");
                    assertThat(beerInfo.getProduct().getCategory()).isEqualTo(Category.CRAB.getName());
                    assertThat(beerInfo.getProductUnit().length).isEqualTo(2);
                    Flux.just(beerInfo.getProductUnit())
                            .sort(Comparator.comparing(com.example.heroku.model.ProductUnit::getName))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    beerUnit1ID.set(beerUnit.getProduct_unit_second_id());
                                    assertThat(beerUnit.getInventory_number()).isEqualTo(543);
                                } else {
                                    beerUnit2ID.set(beerUnit.getProduct_unit_second_id());
                                    assertThat(beerUnit.getInventory_number()).isEqualTo(345);
                                }
                            })
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    beerUnit1ID.set(beerUnit.getProduct_unit_second_id());
                                    assertThat(beerUnit.getInventory_number()).isEqualTo(543);
                                } else {
                                    beerUnit2ID.set(beerUnit.getProduct_unit_second_id());
                                    assertThat(beerUnit.getInventory_number()).isEqualTo(345);
                                }
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        groupImport.SaveGroupImport(
                GroupImportWithItem.builder()
                        .group_id(group)
                        .group_import_second_id("1")
                        .supplier_id("supplier")
                        .total_price(23)
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
                                                .product_unit_second_id(beerUnit1ID.get())
                                                .product_unit_name_category("lon")
                                                .price(22)
                                                .amount(33)
                                                .note("note1")
                                                .type(ProductImport.ImportType.EXPORT)
                                                .build(),
                                        ProductImport.builder()
                                                .product_import_second_id("22")
                                                .product_second_id("123")
                                                .product_unit_second_id(beerUnit2ID.get())
                                                .product_unit_name_category("thung")
                                                .price(44)
                                                .amount(55)
                                                .note("note2")
                                                .type(ProductImport.ImportType.EXPORT)
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
                    assertThat(productImport.getGroup_import_second_id()).isEqualTo("1");;
                    assertThat(productImport.getTotal_price()).isEqualTo(23);;
                    assertThat(productImport.getTotal_amount()).isEqualTo(32);;
                    assertThat(productImport.getDiscount_amount()).isEqualTo(12);;
                    assertThat(productImport.getAdditional_fee()).isEqualTo(10);;
                    assertThat(productImport.getNote()).isEqualTo("note");;
                    assertThat(productImport.getImages()).isEqualTo("images");
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(productImport.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(productImport.getItems().length).isEqualTo(2);
                    ProductImport[] items = productImport.getItems();
                    ProductImport first = items[0];
                    ProductImport second = items[1];
                    if(second.getProduct_import_second_id().equals("11")){
                        ProductImport temp = second;
                        second = first;
                        first = temp;
                    }
                    assertThat(first.getGroup_id()).isEqualTo(group);
                    assertThat(first.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(first.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(first.getProduct_second_id()).isEqualTo("123");
                    assertThat(first.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(first.getProduct_unit_name_category()).isEqualTo("lon");
                    assertThat(first.getPrice()).isEqualTo(22);
                    assertThat(first.getAmount()).isEqualTo(33);
                    assertThat(first.getNote()).isEqualTo("note1");

                    assertThat(second.getGroup_id()).isEqualTo(group);
                    assertThat(second.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(second.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(second.getProduct_second_id()).isEqualTo("123");
                    assertThat(second.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(second.getProduct_unit_name_category()).isEqualTo("thung");
                    assertThat(second.getPrice()).isEqualTo(44);
                    assertThat(second.getAmount()).isEqualTo(55);
                    assertThat(second.getNote()).isEqualTo("note2");

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

        this.beerAPI.GetBeerByID(group, "123")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getProduct().getProduct_second_id()).isEqualTo("123");
                    assertThat(beerInfo.getProduct().getCategory()).isEqualTo(Category.CRAB.getName());
                    assertThat(beerInfo.getProductUnit().length).isEqualTo(2);
                    Flux.just(beerInfo.getProductUnit())
                            .sort(Comparator.comparing(com.example.heroku.model.ProductUnit::getName))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    assertThat(beerUnit.getInventory_number()).isEqualTo(543+33);
                                } else {
                                    assertThat(beerUnit.getInventory_number()).isEqualTo(345+55);
                                }
                            })
                            .consumeNextWith(beerUnit -> {
                                if (beerUnit.getName().equals("lon")) {
                                    assertThat(beerUnit.getInventory_number()).isEqualTo(543+33);
                                } else {
                                    assertThat(beerUnit.getInventory_number()).isEqualTo(345+55);
                                }
                            })
                            .verifyComplete();
                })
                .verifyComplete();
    }
}
