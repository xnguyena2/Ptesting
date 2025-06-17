package com.example.heroku;

import com.example.heroku.model.ProductImport;
import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.warehouse.GroupImportWithItem;
import com.example.heroku.request.warehouse.SearchImportQuery;
import com.example.heroku.services.GroupImport;
import com.example.heroku.services.ProductSerial;
import lombok.Builder;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class ProductImportTest {
    private GroupImport groupImport;

    com.example.heroku.services.Beer beerAPI;

    ProductSerial productSerial;

    String group;

    public void Test(){


        AtomicReference<String> beerUnit1ID = new AtomicReference<String>();
        AtomicReference<String> beerUnit2ID = new AtomicReference<String>();
        AtomicReference<String> beerUnit3ID = new AtomicReference<String>();
        this.beerAPI.GetBeerByID(group, "123")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getProduct().getProduct_second_id()).isEqualTo("123");
                    assertThat(beerInfo.getProduct().getCategory()).isEqualTo(Category.CRAB.getName());
                    assertThat(beerInfo.getProductUnit().length).isEqualTo(3);
                    Flux.just(beerInfo.getProductUnit())
                            .sort(Comparator.comparing(com.example.heroku.model.ProductUnit::getName))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerUnit -> {
                                beerUnit1ID.set(beerUnit.getProduct_unit_second_id());
                                assertThat(beerUnit.getInventory_number()).isEqualTo(543);
                            })
                            .consumeNextWith(beerUnit -> {
                                beerUnit2ID.set(beerUnit.getProduct_unit_second_id());
                                assertThat(beerUnit.getInventory_number()).isEqualTo(345);
                            })
                            .consumeNextWith(beerUnit -> {
                                beerUnit3ID.set(beerUnit.getProduct_unit_second_id());
                                assertThat(beerUnit.getInventory_number()).isEqualTo(345);
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
                        .payment(666)
                        .discount_amount(12)
                        .discount_percent(10)
                        .additional_fee(10)
                        .progress("{}")
                        .note("note")
                        .images("images")
                        .type(ProductImport.ImportType.IMPORT)
                        .items(
                                new ProductImport[]{
                                        ProductImport.builder()
                                                .product_second_id("123")
                                                .product_unit_second_id(beerUnit1ID.get())
                                                .product_unit_name_category("lon")
                                                .price(22)
                                                .amount(33)
                                                .note("note1")
                                                .type(ProductImport.ImportType.EXPORT)
                                                .build(),
                                        ProductImport.builder()
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

        groupImport.SaveGroupImport(
                        GroupImportWithItem.builder()
                                .group_id(group)
                                .group_import_second_id("1")
                                .supplier_id("supplier")
                                .total_price(23)
                                .total_amount(32)
                                .payment(665)
                                .discount_amount(12)
                                .discount_percent(10)
                                .additional_fee(10)
                                .progress("{}")
                                .note("note")
                                .images("images")
                                .type(ProductImport.ImportType.IMPORT)
                                .items(
                                        new ProductImport[]{
                                                ProductImport.builder()
                                                        .product_second_id("123")
                                                        .product_unit_second_id(beerUnit1ID.get())
                                                        .product_unit_name_category("lon")
                                                        .price(25)
                                                        .amount(35)
                                                        .note("note1")
                                                        .type(ProductImport.ImportType.EXPORT)
                                                        .build(),
                                                ProductImport.builder()
                                                        .product_second_id("123")
                                                        .product_unit_second_id(beerUnit2ID.get())
                                                        .product_unit_name_category("thung")
                                                        .price(40)
                                                        .amount(50)
                                                        .note("note2")
                                                        .type(ProductImport.ImportType.EXPORT)
                                                        .build()
                                        }
                                )
                                .build()
                )
                .block();

        groupImport.SaveGroupImport(
                        GroupImportWithItem.builder()
                                .group_id(group)
                                .group_import_second_id("1")
                                .staff_id("staffid")
                                .staff_name("staffname")
                                .supplier_id("supplier")
                                .total_price(23)
                                .total_amount(32)
                                .payment(654)
                                .discount_amount(12)
                                .discount_percent(10)
                                .additional_fee(10)
                                .progress("{}")
                                .note("note")
                                .images("images")
                                .money_source("moneysource")
                                .type(ProductImport.ImportType.IMPORT)
                                .items(
                                        new ProductImport[]{
                                                ProductImport.builder()
                                                        .product_second_id("123")
                                                        .product_unit_second_id(beerUnit1ID.get())
                                                        .product_unit_name_category("lon")
                                                        .price(20)
                                                        .amount(30)
                                                        .note("note1")
                                                        .type(ProductImport.ImportType.EXPORT)
                                                        .build(),
                                                ProductImport.builder()
                                                        .product_second_id("123")
                                                        .product_unit_second_id(beerUnit2ID.get())
                                                        .product_unit_name_category("thung")
                                                        .price(47)
                                                        .amount(57)
                                                        .note("note2")
                                                        .type(ProductImport.ImportType.EXPORT)
                                                        .build()
                                        }
                                )
                                .build()
                )
                .block();



        groupImport.GetAllWorkingBetween(SearchImportQuery.builder().from(LocalDateTime.parse("2023-11-01T00:00:00")).to(LocalDateTime.parse("2300-11-01T00:00:00")).page(0).size(100).group_id(group).build())
                .sort(Comparator.comparing(GroupImportWithItem::getCreateat))
                .as(StepVerifier::create)
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.UPDATE_NUMBER);
                    assertThat(productImport.getTotal_amount()).isEqualTo(345 + 345 +543);

                })
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.UPDATE_NUMBER);
                    assertThat(productImport.getTotal_amount()).isEqualTo(432);

                })
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.UPDATE_NUMBER);
                    assertThat(productImport.getTotal_amount()).isEqualTo(432);

                })
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getGroup_id()).isEqualTo(group);
                    assertThat(productImport.getGroup_import_second_id()).isEqualTo("1");
                    assertThat(productImport.getStaff_id()).isEqualTo("staffid");
                    assertThat(productImport.getStaff_name()).isEqualTo("staffname");
                    assertThat(productImport.getTotal_price()).isEqualTo(23);
                    assertThat(productImport.getPayment()).isEqualTo(654);
                    assertThat(productImport.getTotal_amount()).isEqualTo(32);
                    assertThat(productImport.getDiscount_amount()).isEqualTo(12);
                    assertThat(productImport.getDiscount_percent()).isEqualTo(10);
                    assertThat(productImport.getAdditional_fee()).isEqualTo(10);
                    assertThat(productImport.getProgress()).isEqualTo("{}");
                    assertThat(productImport.getNote()).isEqualTo("note");
                    assertThat(productImport.getImages()).isEqualTo("images");
                    assertThat(productImport.getMoney_source()).isEqualTo("moneysource");
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(productImport.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(productImport.getItems().length).isEqualTo(2);
                    ProductImport[] items = productImport.getItems();
                    ProductImport first = items[0];
                    ProductImport second = items[1];
                    if(second.getProduct_unit_name_category().equals("lon")) {
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
                    assertThat(first.getPrice()).isEqualTo(20);
                    assertThat(first.getAmount()).isEqualTo(30);
                    assertThat(first.getNote()).isEqualTo("note1");

                    assertThat(second.getGroup_id()).isEqualTo(group);
                    assertThat(second.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(second.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(second.getProduct_second_id()).isEqualTo("123");
                    assertThat(second.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(second.getProduct_unit_name_category()).isEqualTo("thung");
                    assertThat(second.getPrice()).isEqualTo(47);
                    assertThat(second.getAmount()).isEqualTo(57);
                    assertThat(second.getNote()).isEqualTo("note2");

                })
                .verifyComplete();



        groupImport.GetAllWorkingBetweenAndType(SearchImportQuery.builder()
                        .from(LocalDateTime.parse("2023-11-01T00:00:00")).to(LocalDateTime.parse("2300-11-01T00:00:00"))
                        .type(ProductImport.ImportType.IMPORT)
                        .page(0).size(100).group_id(group).build())
                .sort(Comparator.comparing(GroupImportWithItem::getCreateat))
                .as(StepVerifier::create)
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getGroup_id()).isEqualTo(group);
                    assertThat(productImport.getGroup_import_second_id()).isEqualTo("1");
                    assertThat(productImport.getTotal_price()).isEqualTo(23);
                    assertThat(productImport.getPayment()).isEqualTo(654);
                    assertThat(productImport.getTotal_amount()).isEqualTo(32);
                    assertThat(productImport.getDiscount_amount()).isEqualTo(12);
                    assertThat(productImport.getDiscount_percent()).isEqualTo(10);
                    assertThat(productImport.getAdditional_fee()).isEqualTo(10);
                    assertThat(productImport.getProgress()).isEqualTo("{}");
                    assertThat(productImport.getNote()).isEqualTo("note");
                    assertThat(productImport.getImages()).isEqualTo("images");
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(productImport.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(productImport.getItems().length).isEqualTo(2);
                    ProductImport[] items = productImport.getItems();
                    ProductImport first = items[0];
                    ProductImport second = items[1];
                    if(second.getProduct_unit_name_category().equals("lon")) {
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
                    assertThat(first.getPrice()).isEqualTo(20);
                    assertThat(first.getAmount()).isEqualTo(30);
                    assertThat(first.getNote()).isEqualTo("note1");

                    assertThat(second.getGroup_id()).isEqualTo(group);
                    assertThat(second.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(second.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(second.getProduct_second_id()).isEqualTo("123");
                    assertThat(second.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(second.getProduct_unit_name_category()).isEqualTo("thung");
                    assertThat(second.getPrice()).isEqualTo(47);
                    assertThat(second.getAmount()).isEqualTo(57);
                    assertThat(second.getNote()).isEqualTo("note2");

                })
                .verifyComplete();

        groupImport.GetAllWorking(SearchQuery.builder().page(0).size(100).filter("10").group_id(group).build())
                .sort(Comparator.comparing(GroupImportWithItem::getCreateat))
                .as(StepVerifier::create)
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.UPDATE_NUMBER);
                    assertThat(productImport.getTotal_amount()).isEqualTo(345 + 345 + 543);

                })
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.UPDATE_NUMBER);
                    assertThat(productImport.getTotal_amount()).isEqualTo(432);

                })
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.UPDATE_NUMBER);
                    assertThat(productImport.getTotal_amount()).isEqualTo(432);

                })
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getGroup_id()).isEqualTo(group);
                    assertThat(productImport.getGroup_import_second_id()).isEqualTo("1");
                    assertThat(productImport.getTotal_price()).isEqualTo(23);
                    assertThat(productImport.getPayment()).isEqualTo(654);
                    assertThat(productImport.getTotal_amount()).isEqualTo(32);
                    assertThat(productImport.getDiscount_amount()).isEqualTo(12);
                    assertThat(productImport.getDiscount_percent()).isEqualTo(10);
                    assertThat(productImport.getAdditional_fee()).isEqualTo(10);
                    assertThat(productImport.getProgress()).isEqualTo("{}");
                    assertThat(productImport.getNote()).isEqualTo("note");
                    assertThat(productImport.getImages()).isEqualTo("images");
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(productImport.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(productImport.getItems().length).isEqualTo(2);
                    ProductImport[] items = productImport.getItems();
                    ProductImport first = items[0];
                    ProductImport second = items[1];
                    if(second.getProduct_unit_name_category().equals("lon")) {
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
                    assertThat(first.getPrice()).isEqualTo(20);
                    assertThat(first.getAmount()).isEqualTo(30);
                    assertThat(first.getNote()).isEqualTo("note1");

                    assertThat(second.getGroup_id()).isEqualTo(group);
                    assertThat(second.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(second.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(second.getProduct_second_id()).isEqualTo("123");
                    assertThat(second.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(second.getProduct_unit_name_category()).isEqualTo("thung");
                    assertThat(second.getPrice()).isEqualTo(47);
                    assertThat(second.getAmount()).isEqualTo(57);
                    assertThat(second.getNote()).isEqualTo("note2");

                })
                .verifyComplete();

        groupImport.GetAllWorkingAndType(SearchImportQuery.builder().page(0).size(100).type(ProductImport.ImportType.IMPORT).group_id(group).build())
                .sort(Comparator.comparing(GroupImportWithItem::getCreateat))
                .as(StepVerifier::create)
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getGroup_id()).isEqualTo(group);
                    assertThat(productImport.getGroup_import_second_id()).isEqualTo("1");
                    assertThat(productImport.getTotal_price()).isEqualTo(23);
                    assertThat(productImport.getPayment()).isEqualTo(654);
                    assertThat(productImport.getTotal_amount()).isEqualTo(32);
                    assertThat(productImport.getDiscount_amount()).isEqualTo(12);
                    assertThat(productImport.getDiscount_percent()).isEqualTo(10);
                    assertThat(productImport.getAdditional_fee()).isEqualTo(10);
                    assertThat(productImport.getProgress()).isEqualTo("{}");
                    assertThat(productImport.getNote()).isEqualTo("note");
                    assertThat(productImport.getImages()).isEqualTo("images");
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(productImport.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(productImport.getItems().length).isEqualTo(2);
                    ProductImport[] items = productImport.getItems();
                    ProductImport first = items[0];
                    ProductImport second = items[1];
                    if(second.getProduct_unit_name_category().equals("lon")) {
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
                    assertThat(first.getPrice()).isEqualTo(20);
                    assertThat(first.getAmount()).isEqualTo(30);
                    assertThat(first.getNote()).isEqualTo("note1");

                    assertThat(second.getGroup_id()).isEqualTo(group);
                    assertThat(second.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(second.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(second.getProduct_second_id()).isEqualTo("123");
                    assertThat(second.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(second.getProduct_unit_name_category()).isEqualTo("thung");
                    assertThat(second.getPrice()).isEqualTo(47);
                    assertThat(second.getAmount()).isEqualTo(57);
                    assertThat(second.getNote()).isEqualTo("note2");

                })
                .verifyComplete();



        groupImport.GetAllWorkingOfProductBetween(SearchImportQuery.builder()
                        .from(LocalDateTime.parse("2023-11-01T00:00:00")).to(LocalDateTime.parse("2300-11-01T00:00:00"))
                        .product_second_id("123")
                        .product_unit_second_id(beerUnit1ID.get())
                        .page(0).size(100).group_id(group).build())
                .sort(Comparator.comparing(GroupImportWithItem::getCreateat))
                .as(StepVerifier::create)
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.UPDATE_NUMBER);
                    assertThat(productImport.getTotal_amount()).isEqualTo(345 + 345 + 543);

                })
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.UPDATE_NUMBER);
                    assertThat(productImport.getTotal_amount()).isEqualTo(432);

                })
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.UPDATE_NUMBER);
                    assertThat(productImport.getTotal_amount()).isEqualTo(432);

                })
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getGroup_id()).isEqualTo(group);
                    assertThat(productImport.getGroup_import_second_id()).isEqualTo("1");
                    assertThat(productImport.getTotal_price()).isEqualTo(23);
                    assertThat(productImport.getPayment()).isEqualTo(654);
                    assertThat(productImport.getTotal_amount()).isEqualTo(32);
                    assertThat(productImport.getDiscount_amount()).isEqualTo(12);
                    assertThat(productImport.getDiscount_percent()).isEqualTo(10);
                    assertThat(productImport.getAdditional_fee()).isEqualTo(10);
                    assertThat(productImport.getProgress()).isEqualTo("{}");
                    assertThat(productImport.getNote()).isEqualTo("note");
                    assertThat(productImport.getImages()).isEqualTo("images");
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(productImport.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(productImport.getItems().length).isEqualTo(1);
                    ProductImport[] items = productImport.getItems();

                    ProductImport first = items[0];

                    assertThat(first.getGroup_id()).isEqualTo(group);
                    assertThat(first.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(first.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(first.getProduct_second_id()).isEqualTo("123");
                    assertThat(first.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(first.getProduct_unit_name_category()).isEqualTo("lon");
                    assertThat(first.getPrice()).isEqualTo(20);
                    assertThat(first.getAmount()).isEqualTo(30);
                    assertThat(first.getNote()).isEqualTo("note1");

                })
                .verifyComplete();



        groupImport.GetByGroupImportID(SearchImportQuery.builder()
                        .group_import_second_id("1")
                        .page(0).size(100).group_id(group).build())
                .sort(Comparator.comparing(GroupImportWithItem::getCreateat))
                .as(StepVerifier::create)
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getGroup_id()).isEqualTo(group);
                    assertThat(productImport.getGroup_import_second_id()).isEqualTo("1");
                    assertThat(productImport.getTotal_price()).isEqualTo(23);
                    assertThat(productImport.getPayment()).isEqualTo(654);
                    assertThat(productImport.getTotal_amount()).isEqualTo(32);
                    assertThat(productImport.getDiscount_amount()).isEqualTo(12);
                    assertThat(productImport.getDiscount_percent()).isEqualTo(10);
                    assertThat(productImport.getAdditional_fee()).isEqualTo(10);
                    assertThat(productImport.getProgress()).isEqualTo("{}");
                    assertThat(productImport.getNote()).isEqualTo("note");
                    assertThat(productImport.getImages()).isEqualTo("images");
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(productImport.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(productImport.getItems().length).isEqualTo(2);
                    ProductImport[] items = productImport.getItems();
                    ProductImport first = items[0];
                    ProductImport second = items[1];
                    if(second.getProduct_unit_name_category().equals("lon")) {
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
                    assertThat(first.getPrice()).isEqualTo(20);
                    assertThat(first.getAmount()).isEqualTo(30);
                    assertThat(first.getNote()).isEqualTo("note1");

                    assertThat(second.getGroup_id()).isEqualTo(group);
                    assertThat(second.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(second.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(second.getProduct_second_id()).isEqualTo("123");
                    assertThat(second.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(second.getProduct_unit_name_category()).isEqualTo("thung");
                    assertThat(second.getPrice()).isEqualTo(47);
                    assertThat(second.getAmount()).isEqualTo(57);
                    assertThat(second.getNote()).isEqualTo("note2");

                })
                .verifyComplete();



        groupImport.GetWareHouseStatictisBetween(SearchImportQuery.builder()
                        .group_import_second_id("1")
                        .from(LocalDateTime.parse("2023-11-01T00:00:00")).to(LocalDateTime.parse("2300-11-01T00:00:00"))
                        .page(0).size(100).group_id(group).build())
                .as(StepVerifier::create)
                .consumeNextWith(wareHouseIncomeOutCome -> {
                    assertThat(wareHouseIncomeOutCome.getExport_price_inside()).isEqualTo(0);
                    assertThat(wareHouseIncomeOutCome.getExport_price_outside()).isEqualTo(0);
                    assertThat(wareHouseIncomeOutCome.getImport_price_inside()).isEqualTo(18.7f);
                    assertThat(wareHouseIncomeOutCome.getImport_price_outside()).isEqualTo(0);
                    assertThat(wareHouseIncomeOutCome.getExport_amount_inside()).isEqualTo(0);
                    assertThat(wareHouseIncomeOutCome.getExport_amount_outside()).isEqualTo(0);
                    assertThat(wareHouseIncomeOutCome.getImport_amount_inside()).isEqualTo(32);
                    assertThat(wareHouseIncomeOutCome.getImport_amount_outside()).isEqualTo(0);

                })
                .verifyComplete();



        groupImport.GetAllWorkingOfProductBetweenAndType(SearchImportQuery.builder()
                        .from(LocalDateTime.parse("2023-11-01T00:00:00")).to(LocalDateTime.parse("2300-11-01T00:00:00"))
                        .type(ProductImport.ImportType.IMPORT)
                        .product_second_id("123")
                        .product_unit_second_id(beerUnit1ID.get())
                        .page(0).size(100).group_id(group).build())
                .sort(Comparator.comparing(GroupImportWithItem::getCreateat))
                .as(StepVerifier::create)
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getGroup_id()).isEqualTo(group);
                    assertThat(productImport.getGroup_import_second_id()).isEqualTo("1");
                    assertThat(productImport.getTotal_price()).isEqualTo(23);
                    assertThat(productImport.getPayment()).isEqualTo(654);
                    assertThat(productImport.getTotal_amount()).isEqualTo(32);
                    assertThat(productImport.getDiscount_amount()).isEqualTo(12);
                    assertThat(productImport.getDiscount_percent()).isEqualTo(10);
                    assertThat(productImport.getAdditional_fee()).isEqualTo(10);
                    assertThat(productImport.getProgress()).isEqualTo("{}");
                    assertThat(productImport.getNote()).isEqualTo("note");
                    assertThat(productImport.getImages()).isEqualTo("images");
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(productImport.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(productImport.getItems().length).isEqualTo(1);
                    ProductImport[] items = productImport.getItems();

                    ProductImport first = items[0];

                    assertThat(first.getGroup_id()).isEqualTo(group);
                    assertThat(first.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(first.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(first.getProduct_second_id()).isEqualTo("123");
                    assertThat(first.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(first.getProduct_unit_name_category()).isEqualTo("lon");
                    assertThat(first.getPrice()).isEqualTo(20);
                    assertThat(first.getAmount()).isEqualTo(30);
                    assertThat(first.getNote()).isEqualTo("note1");

                })
                .verifyComplete();



        groupImport.GetAllWorkingOfProduct(SearchImportQuery.builder()
                        .from(LocalDateTime.parse("2023-11-01T00:00:00")).to(LocalDateTime.parse("2300-11-01T00:00:00"))
                        .product_second_id("123")
                        .product_unit_second_id(beerUnit2ID.get())
                        .page(0).size(100).group_id(group).build())
                .sort(Comparator.comparing(GroupImportWithItem::getCreateat))
                .as(StepVerifier::create)
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.UPDATE_NUMBER);
                    assertThat(productImport.getTotal_amount()).isEqualTo(345 + 345 + 543);

                })
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getGroup_id()).isEqualTo(group);
                    assertThat(productImport.getGroup_import_second_id()).isEqualTo("1");
                    assertThat(productImport.getTotal_price()).isEqualTo(23);
                    assertThat(productImport.getPayment()).isEqualTo(654);
                    assertThat(productImport.getTotal_amount()).isEqualTo(32);
                    assertThat(productImport.getDiscount_amount()).isEqualTo(12);
                    assertThat(productImport.getDiscount_percent()).isEqualTo(10);
                    assertThat(productImport.getAdditional_fee()).isEqualTo(10);
                    assertThat(productImport.getProgress()).isEqualTo("{}");
                    assertThat(productImport.getNote()).isEqualTo("note");
                    assertThat(productImport.getImages()).isEqualTo("images");
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(productImport.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(productImport.getItems().length).isEqualTo(1);
                    ProductImport[] items = productImport.getItems();
                    ProductImport second = items[0];

                    assertThat(second.getGroup_id()).isEqualTo(group);
                    assertThat(second.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(second.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(second.getProduct_second_id()).isEqualTo("123");
                    assertThat(second.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(second.getProduct_unit_name_category()).isEqualTo("thung");
                    assertThat(second.getPrice()).isEqualTo(47);
                    assertThat(second.getAmount()).isEqualTo(57);
                    assertThat(second.getNote()).isEqualTo("note2");

                })
                .verifyComplete();

        groupImport.GetAllWorkingOfProductAndType(SearchImportQuery.builder()
                        .from(LocalDateTime.parse("2023-11-01T00:00:00")).to(LocalDateTime.parse("2300-11-01T00:00:00"))
                        .product_second_id("123")
                        .type(ProductImport.ImportType.IMPORT)
                        .product_unit_second_id(beerUnit2ID.get())
                        .page(0).size(100).group_id(group).build())
                .sort(Comparator.comparing(GroupImportWithItem::getCreateat))
                .as(StepVerifier::create)
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getGroup_id()).isEqualTo(group);
                    assertThat(productImport.getGroup_import_second_id()).isEqualTo("1");
                    assertThat(productImport.getTotal_price()).isEqualTo(23);
                    assertThat(productImport.getPayment()).isEqualTo(654);
                    assertThat(productImport.getTotal_amount()).isEqualTo(32);
                    assertThat(productImport.getDiscount_amount()).isEqualTo(12);
                    assertThat(productImport.getDiscount_percent()).isEqualTo(10);
                    assertThat(productImport.getAdditional_fee()).isEqualTo(10);
                    assertThat(productImport.getProgress()).isEqualTo("{}");
                    assertThat(productImport.getNote()).isEqualTo("note");
                    assertThat(productImport.getImages()).isEqualTo("images");
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(productImport.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(productImport.getItems().length).isEqualTo(1);
                    ProductImport[] items = productImport.getItems();
                    ProductImport second = items[0];

                    assertThat(second.getGroup_id()).isEqualTo(group);
                    assertThat(second.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(second.getStatus()).isEqualTo(ProductImport.Status.CREATE);
                    assertThat(second.getProduct_second_id()).isEqualTo("123");
                    assertThat(second.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(second.getProduct_unit_name_category()).isEqualTo("thung");
                    assertThat(second.getPrice()).isEqualTo(47);
                    assertThat(second.getAmount()).isEqualTo(57);
                    assertThat(second.getNote()).isEqualTo("note2");

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
                                .status(ProductImport.Status.DONE)
                                .items(
                                        new ProductImport[]{
                                                ProductImport.builder()
                                                        .product_second_id("123")
                                                        .product_unit_second_id(beerUnit1ID.get())
                                                        .product_unit_name_category("lon")
                                                        .price(20)
                                                        .amount(30)
                                                        .note("note1")
                                                        .type(ProductImport.ImportType.EXPORT)
                                                        .build(),
                                                ProductImport.builder()
                                                        .product_second_id("123")
                                                        .product_unit_second_id(beerUnit2ID.get())
                                                        .product_unit_name_category("thung")
                                                        .price(47)
                                                        .amount(57)
                                                        .note("note2")
                                                        .type(ProductImport.ImportType.EXPORT)
                                                        .build()
                                        }
                                )
                                .build()
                )
                .block();


        //check price
        this.beerAPI.GetBeerByID(group, "123")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getProduct().getProduct_second_id()).isEqualTo("123");
                    assertThat(beerInfo.getProduct().getCategory()).isEqualTo(Category.CRAB.getName());
                    assertThat(beerInfo.getProductUnit().length).isEqualTo(3);
                    Flux.just(beerInfo.getProductUnit())
                            .sort(Comparator.comparing(com.example.heroku.model.ProductUnit::getName))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerUnit -> {
                                assertThat(beerUnit.getInventory_number()).isEqualTo(543 + 30);
                                assertThat(beerUnit.getBuy_price()).isEqualTo(38.95288f);
                            })
                            .consumeNextWith(beerUnit -> {
                                assertThat(beerUnit.getInventory_number()).isEqualTo(345 + 57);
                                assertThat(beerUnit.getBuy_price()).isEqualTo(23.828358f);
                            })
                            .consumeNextWith(beerUnit -> {
                                assertThat(beerUnit.getInventory_number()).isEqualTo(345);
                                assertThat(beerUnit.getBuy_price()).isEqualTo(20.0f);
                            })
                            .verifyComplete();
                })
                .verifyComplete();


        groupImport.SaveGroupImport(
                        GroupImportWithItem.builder()
                                .group_id(group)
                                .group_import_second_id("2")
                                .supplier_id("supplier")
                                .total_price(23)
                                .total_amount(32)
                                .discount_amount(12)
                                .additional_fee(10)
                                .note("note")
                                .images("images")
                                .type(ProductImport.ImportType.EXPORT)
                                .status(ProductImport.Status.DONE)
                                .items(
                                        new ProductImport[]{
                                                ProductImport.builder()
                                                        .product_second_id("123")
                                                        .product_unit_second_id(beerUnit1ID.get())
                                                        .product_unit_name_category("lon")
                                                        .price(27)
                                                        .amount(3)
                                                        .note("note1")
                                                        .type(ProductImport.ImportType.EXPORT)
                                                        .build(),
                                                ProductImport.builder()
                                                        .product_second_id("123")
                                                        .product_unit_second_id(beerUnit2ID.get())
                                                        .product_unit_name_category("thung")
                                                        .price(50)
                                                        .amount(6)
                                                        .note("note2")
                                                        .type(ProductImport.ImportType.EXPORT)
                                                        .build()
                                        }
                                )
                                .build()
                )
                .block();

        this.beerAPI.GetBeerByID(group, "123")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getProduct().getProduct_second_id()).isEqualTo("123");
                    assertThat(beerInfo.getProduct().getCategory()).isEqualTo(Category.CRAB.getName());
                    assertThat(beerInfo.getProductUnit().length).isEqualTo(3);
                    Flux.just(beerInfo.getProductUnit())
                            .sort(Comparator.comparing(com.example.heroku.model.ProductUnit::getName))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerUnit -> {
                                assertThat(beerUnit.getInventory_number()).isEqualTo(543+30-3);
                                assertThat(beerUnit.getBuy_price()).isEqualTo(38.95288f);
                            })
                            .consumeNextWith(beerUnit -> {
                                assertThat(beerUnit.getInventory_number()).isEqualTo(345+57-6);
                                assertThat(beerUnit.getBuy_price()).isEqualTo(23.828358f);
                            })
                            .consumeNextWith(beerUnit -> {
                                assertThat(beerUnit.getInventory_number()).isEqualTo(345);
                                assertThat(beerUnit.getBuy_price()).isEqualTo(20.0f);
                            })
                            .verifyComplete();
                })
                .verifyComplete();


        groupImport.SaveGroupImport(
                        GroupImportWithItem.builder()
                                .group_id(group)
                                .group_import_second_id("3")
                                .supplier_id("supplier")
                                .total_price(23)
                                .total_amount(32)
                                .discount_amount(12)
                                .additional_fee(10)
                                .note("note")
                                .images("images")
                                .type(ProductImport.ImportType.EXPORT)
                                .items(
                                        new ProductImport[]{
                                                ProductImport.builder()
                                                        .product_second_id("123")
                                                        .product_unit_second_id(beerUnit1ID.get())
                                                        .product_unit_name_category("lon")
                                                        .price(27)
                                                        .amount(3)
                                                        .note("note1")
                                                        .type(ProductImport.ImportType.EXPORT)
                                                        .build(),
                                                ProductImport.builder()
                                                        .product_second_id("123")
                                                        .product_unit_second_id(beerUnit2ID.get())
                                                        .product_unit_name_category("thung")
                                                        .price(50)
                                                        .amount(6)
                                                        .note("note2")
                                                        .type(ProductImport.ImportType.EXPORT)
                                                        .build()
                                        }
                                )
                                .build()
                )
                .block();


        groupImport.GetAllDebtOfSupplier(group, ProductImport.ImportType.IMPORT, "supplier")
                .sort(Comparator.comparing(GroupImportWithItem::getCreateat))
                .as(StepVerifier::create)
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getGroup_id()).isEqualTo(group);
                    assertThat(productImport.getGroup_import_second_id()).isEqualTo("1");
                    assertThat(productImport.getTotal_price()).isEqualTo(23);
                    assertThat(productImport.getPayment()).isEqualTo(0);
                    assertThat(productImport.getTotal_amount()).isEqualTo(32);
                    assertThat(productImport.getDiscount_amount()).isEqualTo(12);
                    assertThat(productImport.getDiscount_percent()).isEqualTo(0);
                    assertThat(productImport.getAdditional_fee()).isEqualTo(10);
                    assertThat(productImport.getProgress()).isEqualTo(null);
                    assertThat(productImport.getNote()).isEqualTo("note");
                    assertThat(productImport.getImages()).isEqualTo("images");
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(productImport.getStatus()).isEqualTo(ProductImport.Status.DONE);
                    assertThat(productImport.getItems().length).isEqualTo(2);
                    ProductImport[] items = productImport.getItems();
                    ProductImport first = items[0];
                    ProductImport second = items[1];
                    if(second.getProduct_unit_name_category().equals("lon")) {
                        ProductImport temp = second;
                        second = first;
                        first = temp;
                    }
                    assertThat(first.getGroup_id()).isEqualTo(group);
                    assertThat(first.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(first.getStatus()).isEqualTo(ProductImport.Status.DONE);
                    assertThat(first.getProduct_second_id()).isEqualTo("123");
                    assertThat(first.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(first.getProduct_unit_name_category()).isEqualTo("lon");
                    assertThat(first.getPrice()).isEqualTo(20);
                    assertThat(first.getAmount()).isEqualTo(30);
                    assertThat(first.getNote()).isEqualTo("note1");

                    assertThat(second.getGroup_id()).isEqualTo(group);
                    assertThat(second.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(second.getStatus()).isEqualTo(ProductImport.Status.DONE);
                    assertThat(second.getProduct_second_id()).isEqualTo("123");
                    assertThat(second.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(second.getProduct_unit_name_category()).isEqualTo("thung");
                    assertThat(second.getPrice()).isEqualTo(47);
                    assertThat(second.getAmount()).isEqualTo(57);
                    assertThat(second.getNote()).isEqualTo("note2");

                })
                .verifyComplete();


        groupImport.GetAllDebtOfSupplier(group, ProductImport.ImportType.EXPORT, "supplier")
                .sort(Comparator.comparing(GroupImportWithItem::getCreateat))
                .as(StepVerifier::create)
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getGroup_id()).isEqualTo(group);
                    assertThat(productImport.getGroup_import_second_id()).isEqualTo("2");
                    assertThat(productImport.getTotal_price()).isEqualTo(23);
                    assertThat(productImport.getPayment()).isEqualTo(0);
                    assertThat(productImport.getTotal_amount()).isEqualTo(32);
                    assertThat(productImport.getDiscount_amount()).isEqualTo(12);
                    assertThat(productImport.getDiscount_percent()).isEqualTo(0);
                    assertThat(productImport.getAdditional_fee()).isEqualTo(10);
                    assertThat(productImport.getProgress()).isEqualTo(null);
                    assertThat(productImport.getNote()).isEqualTo("note");
                    assertThat(productImport.getImages()).isEqualTo("images");
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.EXPORT);
                    assertThat(productImport.getStatus()).isEqualTo(ProductImport.Status.DONE);
                    assertThat(productImport.getItems().length).isEqualTo(2);
                    ProductImport[] items = productImport.getItems();
                    ProductImport first = items[0];
                    ProductImport second = items[1];
                    if(second.getProduct_unit_name_category().equals("lon")) {
                        ProductImport temp = second;
                        second = first;
                        first = temp;
                    }
                    assertThat(first.getGroup_id()).isEqualTo(group);
                    assertThat(first.getType()).isEqualTo(ProductImport.ImportType.EXPORT);
                    assertThat(first.getStatus()).isEqualTo(ProductImport.Status.DONE);
                    assertThat(first.getProduct_second_id()).isEqualTo("123");
                    assertThat(first.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(first.getProduct_unit_name_category()).isEqualTo("lon");
                    assertThat(first.getPrice()).isEqualTo(27);
                    assertThat(first.getAmount()).isEqualTo(3);
                    assertThat(first.getNote()).isEqualTo("note1");

                    assertThat(second.getGroup_id()).isEqualTo(group);
                    assertThat(second.getType()).isEqualTo(ProductImport.ImportType.EXPORT);
                    assertThat(second.getStatus()).isEqualTo(ProductImport.Status.DONE);
                    assertThat(second.getProduct_second_id()).isEqualTo("123");
                    assertThat(second.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(second.getProduct_unit_name_category()).isEqualTo("thung");
                    assertThat(second.getPrice()).isEqualTo(50);
                    assertThat(second.getAmount()).isEqualTo(6);
                    assertThat(second.getNote()).isEqualTo("note2");

                })
                .verifyComplete();


        groupImport.GetAllDebtOfSupplier(group, ProductImport.ImportType.UN_KNOW, "supplier")
                .sort(Comparator.comparing(GroupImportWithItem::getCreateat))
                .as(StepVerifier::create)
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getGroup_id()).isEqualTo(group);
                    assertThat(productImport.getGroup_import_second_id()).isEqualTo("1");
                    assertThat(productImport.getTotal_price()).isEqualTo(23);
                    assertThat(productImport.getPayment()).isEqualTo(0);
                    assertThat(productImport.getTotal_amount()).isEqualTo(32);
                    assertThat(productImport.getDiscount_amount()).isEqualTo(12);
                    assertThat(productImport.getDiscount_percent()).isEqualTo(0);
                    assertThat(productImport.getAdditional_fee()).isEqualTo(10);
                    assertThat(productImport.getProgress()).isEqualTo(null);
                    assertThat(productImport.getNote()).isEqualTo("note");
                    assertThat(productImport.getImages()).isEqualTo("images");
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(productImport.getStatus()).isEqualTo(ProductImport.Status.DONE);
                    assertThat(productImport.getItems().length).isEqualTo(2);
                    ProductImport[] items = productImport.getItems();
                    ProductImport first = items[0];
                    ProductImport second = items[1];
                    if(second.getProduct_unit_name_category().equals("lon")) {
                        ProductImport temp = second;
                        second = first;
                        first = temp;
                    }
                    assertThat(first.getGroup_id()).isEqualTo(group);
                    assertThat(first.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(first.getStatus()).isEqualTo(ProductImport.Status.DONE);
                    assertThat(first.getProduct_second_id()).isEqualTo("123");
                    assertThat(first.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(first.getProduct_unit_name_category()).isEqualTo("lon");
                    assertThat(first.getPrice()).isEqualTo(20);
                    assertThat(first.getAmount()).isEqualTo(30);
                    assertThat(first.getNote()).isEqualTo("note1");

                    assertThat(second.getGroup_id()).isEqualTo(group);
                    assertThat(second.getType()).isEqualTo(ProductImport.ImportType.IMPORT);
                    assertThat(second.getStatus()).isEqualTo(ProductImport.Status.DONE);
                    assertThat(second.getProduct_second_id()).isEqualTo("123");
                    assertThat(second.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(second.getProduct_unit_name_category()).isEqualTo("thung");
                    assertThat(second.getPrice()).isEqualTo(47);
                    assertThat(second.getAmount()).isEqualTo(57);
                    assertThat(second.getNote()).isEqualTo("note2");

                })
                .consumeNextWith(productImport -> {
                    assertThat(productImport.getGroup_id()).isEqualTo(group);
                    assertThat(productImport.getGroup_import_second_id()).isEqualTo("2");
                    assertThat(productImport.getTotal_price()).isEqualTo(23);
                    assertThat(productImport.getPayment()).isEqualTo(0);
                    assertThat(productImport.getTotal_amount()).isEqualTo(32);
                    assertThat(productImport.getDiscount_amount()).isEqualTo(12);
                    assertThat(productImport.getDiscount_percent()).isEqualTo(0);
                    assertThat(productImport.getAdditional_fee()).isEqualTo(10);
                    assertThat(productImport.getProgress()).isEqualTo(null);
                    assertThat(productImport.getNote()).isEqualTo("note");
                    assertThat(productImport.getImages()).isEqualTo("images");
                    assertThat(productImport.getType()).isEqualTo(ProductImport.ImportType.EXPORT);
                    assertThat(productImport.getStatus()).isEqualTo(ProductImport.Status.DONE);
                    assertThat(productImport.getItems().length).isEqualTo(2);
                    ProductImport[] items = productImport.getItems();
                    ProductImport first = items[0];
                    ProductImport second = items[1];
                    if(second.getProduct_unit_name_category().equals("lon")) {
                        ProductImport temp = second;
                        second = first;
                        first = temp;
                    }
                    assertThat(first.getGroup_id()).isEqualTo(group);
                    assertThat(first.getType()).isEqualTo(ProductImport.ImportType.EXPORT);
                    assertThat(first.getStatus()).isEqualTo(ProductImport.Status.DONE);
                    assertThat(first.getProduct_second_id()).isEqualTo("123");
                    assertThat(first.getProduct_unit_second_id()).isEqualTo(beerUnit1ID.get());
                    assertThat(first.getProduct_unit_name_category()).isEqualTo("lon");
                    assertThat(first.getPrice()).isEqualTo(27);
                    assertThat(first.getAmount()).isEqualTo(3);
                    assertThat(first.getNote()).isEqualTo("note1");

                    assertThat(second.getGroup_id()).isEqualTo(group);
                    assertThat(second.getType()).isEqualTo(ProductImport.ImportType.EXPORT);
                    assertThat(second.getStatus()).isEqualTo(ProductImport.Status.DONE);
                    assertThat(second.getProduct_second_id()).isEqualTo("123");
                    assertThat(second.getProduct_unit_second_id()).isEqualTo(beerUnit2ID.get());
                    assertThat(second.getProduct_unit_name_category()).isEqualTo("thung");
                    assertThat(second.getPrice()).isEqualTo(50);
                    assertThat(second.getAmount()).isEqualTo(6);
                    assertThat(second.getNote()).isEqualTo("note2");

                })
                .verifyComplete();

        groupImport.Return(IDContainer.builder().group_id(group).id("3").build()).block();

        groupImport.Return(IDContainer.builder().group_id(group).id("2").build()).block();

        groupImport.Return(IDContainer.builder().group_id(group).id("1").build()).block();


        groupImport.SaveGroupImport(
                        GroupImportWithItem.builder()
                                .group_id(group)
                                .group_import_second_id("4")
                                .supplier_id("supplier")
                                .total_price(23)
                                .total_amount(32)
                                .discount_amount(12)
                                .additional_fee(10)
                                .note("note")
                                .images("images")
                                .type(ProductImport.ImportType.CHECK_WAREHOUSE)
                                .items(
                                        new ProductImport[]{
                                                ProductImport.builder()
                                                        .product_second_id("123")
                                                        .product_unit_second_id(beerUnit1ID.get())
                                                        .product_unit_name_category("lon")
                                                        .price(27)
                                                        .amount(387)
                                                        .note("note1")
                                                        .type(ProductImport.ImportType.EXPORT)
                                                        .build(),
                                                ProductImport.builder()
                                                        .product_second_id("123")
                                                        .product_unit_second_id(beerUnit2ID.get())
                                                        .product_unit_name_category("thung")
                                                        .price(50)
                                                        .amount(687)
                                                        .note("note2")
                                                        .type(ProductImport.ImportType.EXPORT)
                                                        .build()
                                        }
                                )
                                .build()
                )
                .block();

        this.beerAPI.GetBeerByID(group, "123")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getProduct().getProduct_second_id()).isEqualTo("123");
                    assertThat(beerInfo.getProduct().getCategory()).isEqualTo(Category.CRAB.getName());
                    assertThat(beerInfo.getProductUnit().length).isEqualTo(3);
                    Flux.just(beerInfo.getProductUnit())
                            .sort(Comparator.comparing(com.example.heroku.model.ProductUnit::getName))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerUnit -> {
                                assertThat(beerUnit.getInventory_number()).isEqualTo(387);
                                assertThat(beerUnit.getBuy_price()).isEqualTo(40);
                            })
                            .consumeNextWith(beerUnit -> {
                                assertThat(beerUnit.getInventory_number()).isEqualTo(687);
                                assertThat(beerUnit.getBuy_price()).isEqualTo(20);
                            })
                            .consumeNextWith(beerUnit -> {
                                assertThat(beerUnit.getInventory_number()).isEqualTo(345);
                                assertThat(beerUnit.getBuy_price()).isEqualTo(20.0f);
                            })
                            .verifyComplete();
                })
                .verifyComplete();

        groupImport.Return(IDContainer.builder().group_id(group).id("4").build()).block();


        groupImport.SaveGroupImport(
                        GroupImportWithItem.builder()
                                .group_id(group)
                                .group_import_second_id("5")
                                .supplier_id("supplier")
                                .total_price(23)
                                .total_amount(32)
                                .discount_amount(12)
                                .additional_fee(10)
                                .note("note")
                                .images("images")
                                .type(ProductImport.ImportType.CHECK_WAREHOUSE)
                                .items(
                                        new ProductImport[]{
                                                ProductImport.builder()
                                                        .product_second_id("123")
                                                        .product_unit_second_id(beerUnit1ID.get())
                                                        .product_unit_name_category("lon")
                                                        .price(27)
                                                        .amount(543)
                                                        .note("note1")
                                                        .type(ProductImport.ImportType.EXPORT)
                                                        .build(),
                                                ProductImport.builder()
                                                        .product_second_id("123")
                                                        .product_unit_second_id(beerUnit2ID.get())
                                                        .product_unit_name_category("thung")
                                                        .price(50)
                                                        .amount(345)
                                                        .note("note2")
                                                        .type(ProductImport.ImportType.EXPORT)
                                                        .build()
                                        }
                                )
                                .build()
                )
                .block();

        this.beerAPI.GetBeerByID(group, "123")
                .map(BeerSubmitData::GetBeerInfo)
                .as(StepVerifier::create)
                .consumeNextWith(beerInfo -> {
                    assertThat(beerInfo.getProduct().getProduct_second_id()).isEqualTo("123");
                    assertThat(beerInfo.getProduct().getCategory()).isEqualTo(Category.CRAB.getName());
                    assertThat(beerInfo.getProductUnit().length).isEqualTo(3);
                    Flux.just(beerInfo.getProductUnit())
                            .sort(Comparator.comparing(com.example.heroku.model.ProductUnit::getName))
                            .as(StepVerifier::create)
                            .consumeNextWith(beerUnit -> {
                                assertThat(beerUnit.getInventory_number()).isEqualTo(543);
                                assertThat(beerUnit.getBuy_price()).isEqualTo(40);
                            })
                            .consumeNextWith(beerUnit -> {
                                assertThat(beerUnit.getInventory_number()).isEqualTo(345);
                                assertThat(beerUnit.getBuy_price()).isEqualTo(20);
                            })
                            .consumeNextWith(beerUnit -> {
                                assertThat(beerUnit.getInventory_number()).isEqualTo(345);
                                assertThat(beerUnit.getBuy_price()).isEqualTo(20.0f);
                            })
                            .verifyComplete();
                })
                .verifyComplete();
    }
}
