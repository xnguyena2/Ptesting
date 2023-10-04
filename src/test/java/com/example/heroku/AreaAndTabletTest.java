package com.example.heroku;

import com.example.heroku.model.Buyer;
import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.request.beer.ProductPackage;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.client.AreaID;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.request.client.UserID;
import com.example.heroku.response.AreaData;
import com.example.heroku.response.TableDetailData;
import com.example.heroku.services.Area;
import com.example.heroku.services.UserPackage;
import com.example.heroku.util.Util;
import lombok.Builder;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class AreaAndTabletTest {

    UserPackage userPackageAPI;

    Area area;

    com.example.heroku.services.Buyer buyer;

    public void Test(String group) {
        area.createTableDetail(
                AreaData.builder().build()
        ).block();

        area.createTableDetail(
                        AreaData.builder()
                                .group_id(group)
                                .listTable(Arrays.asList(new TableDetailData[]{
                                        TableDetailData.builder().build()
                                }))
                                .build()
                )
                .as(StepVerifier::create)
                .expectError();

        area.createTableDetail(
                        AreaData.builder()
                                .group_id(group)
                                .listTable(Arrays.asList(new TableDetailData[]{
                                        TableDetailData.builder()
                                                .area_id("not exist")
                                                .table_name("table 1 of area 1")
                                                .build()
                                }))
                                .build()
                )
                .as(StepVerifier::create)
                .expectError();

        area.getAll(UserID.builder()
                        .group_id(group)
                        .page(0)
                        .size(10000)
                        .build())
                .as(StepVerifier::create)
                .verifyComplete();


        AtomicReference<String> are1ID = new AtomicReference<>(null);
        AtomicReference<String> are2ID = new AtomicReference<>(null);

        area.justCreateOrUpdateArea(
                        AreaData.builder()
                                .group_id(group)
                                .area_name("Tầng 1")
                                .listTable(Arrays.asList(new TableDetailData[]{
                                        TableDetailData.builder()
                                                .area_id("not exist")
                                                .table_name("table 1 of area 1")
                                                .build()
                                }))
                                .build()
                )
                .block();

        area.justCreateOrUpdateArea(
                        AreaData.builder()
                                .group_id(group)
                                .area_name("Tầng 2")
                                .listTable(Arrays.asList(new TableDetailData[]{
                                        TableDetailData.builder()
                                                .area_id("not exist")
                                                .table_name("table 1 of area 2")
                                                .build()
                                }))
                                .build()
                )
                .block();

        area.getAll(UserID.builder()
                        .group_id(group)
                        .page(0)
                        .size(10000)
                        .build())
                .sort(Comparator.comparing(AreaData::getArea_name))
                .as(StepVerifier::create)
                .consumeNextWith(areaData -> {
                    assertThat(Util.getInstance().RemoveAccent(areaData.getArea_name())).isEqualTo("tang 1");
                    assertThat(areaData.getMeta_search()).isEqualTo("tang 1");
                    are1ID.set(areaData.getArea_id());
                })
                .consumeNextWith(areaData -> {
                    assertThat(Util.getInstance().RemoveAccent(areaData.getArea_name())).isEqualTo("tang 2");
                    assertThat(areaData.getMeta_search()).isEqualTo("tang 2");
                    are2ID.set(areaData.getArea_id());
                })
                .verifyComplete();

        area.getAreaById(AreaID.builder()
                        .group_id(group)
                        .area_id(are1ID.get())
                        .build())
                .as(StepVerifier::create)
                .consumeNextWith(areaData -> {
                    assertThat(Util.getInstance().RemoveAccent(areaData.getArea_name())).isEqualTo("tang 1");
                    assertThat(areaData.getMeta_search()).isEqualTo("tang 1");
                    assertThat(areaData.getListTable().size()).isEqualTo(0);
                })
                .verifyComplete();

        area.findArea(SearchQuery.builder()
                        .group_id(group)
                        .query("tang")
                        .page(0)
                        .size(10000)
                        .build())
                .sort(Comparator.comparing(AreaData::getArea_name))
                .as(StepVerifier::create)
                .consumeNextWith(areaData -> {
                    assertThat(Util.getInstance().RemoveAccent(areaData.getArea_name())).isEqualTo("tang 1");
                    assertThat(areaData.getMeta_search()).isEqualTo("tang 1");
                })
                .consumeNextWith(areaData -> {
                    assertThat(Util.getInstance().RemoveAccent(areaData.getArea_name())).isEqualTo("tang 2");
                    assertThat(areaData.getMeta_search()).isEqualTo("tang 2");
                })
                .verifyComplete();

        area.createTableDetail(
                AreaData.builder()
                        .group_id(group)
                        .area_id(are1ID.get())
                        .listTable(Arrays.asList(new TableDetailData[]{
                                TableDetailData.builder()
                                        .table_name("table 1 of area 1")
                                        .build(),
                                TableDetailData.builder()
                                        .table_name("table 2 of area 1")
                                        .build(),
                                TableDetailData.builder()
                                        .table_name("table 3 of area 1")
                                        .build()
                        }))
                        .build()
        ).block();

        area.createTableDetail(
                AreaData.builder()
                        .group_id(group)
                        .area_id(are2ID.get())
                        .listTable(Arrays.asList(new TableDetailData[]{
                                TableDetailData.builder()
                                        .table_name("table 1 of area 2")
                                        .build(),
                                TableDetailData.builder()
                                        .table_name("table 2 of area 2")
                                        .build(),
                                TableDetailData.builder()
                                        .table_name("table 3 of area 2")
                                        .build()
                        }))
                        .build()
        ).block();

        AtomicReference<String> table1OfArea1 = new AtomicReference<>(null);

        area.getAreaById(AreaID.builder()
                        .group_id(group)
                        .area_id(are1ID.get())
                        .build())
                .as(StepVerifier::create)
                .consumeNextWith(areaData -> {
                    assertThat(Util.getInstance().RemoveAccent(areaData.getArea_name())).isEqualTo("tang 1");
                    assertThat(areaData.getMeta_search()).isEqualTo("tang 1");

                    List<TableDetailData> listTable = areaData.getListTable();
                    listTable.sort(Comparator.comparing(TableDetailData::getTable_name));
                    assertThat(listTable.size()).isEqualTo(3);
                    table1OfArea1.set(listTable.get(0).getTable_id());
                    ;
                    assertThat(listTable.get(0).getTable_name()).isEqualTo("table 1 of area 1");
                    assertThat(listTable.get(1).getTable_name()).isEqualTo("table 2 of area 1");
                    assertThat(listTable.get(2).getTable_name()).isEqualTo("table 3 of area 1");
                })
                .verifyComplete();

        area.deleteTableDetail(TableDetailData.builder()
                .group_id(group)
                .table_id(table1OfArea1.get())
                .build()).block();

        area.getAreaById(AreaID.builder()
                        .group_id(group)
                        .area_id(are1ID.get())
                        .build())
                .as(StepVerifier::create)
                .consumeNextWith(areaData -> {
                    assertThat(Util.getInstance().RemoveAccent(areaData.getArea_name())).isEqualTo("tang 1");
                    assertThat(areaData.getMeta_search()).isEqualTo("tang 1");

                    List<TableDetailData> listTable = areaData.getListTable();
                    listTable.sort(Comparator.comparing(TableDetailData::getTable_name));
                    assertThat(listTable.size()).isEqualTo(2);
                    assertThat(listTable.get(0).getTable_name()).isEqualTo("table 2 of area 1");
                    assertThat(listTable.get(1).getTable_name()).isEqualTo("table 3 of area 1");
                })
                .verifyComplete();

        area.createOrUpdateTableDetail(TableDetailData.builder()
                .group_id(group)
                .area_id(are1ID.get())
                .table_id(table1OfArea1.get())
                .table_name("table 1 of area 1")
                .build()).block();

        area.getAreaById(AreaID.builder()
                        .group_id(group)
                        .area_id(are1ID.get())
                        .build())
                .as(StepVerifier::create)
                .consumeNextWith(areaData -> {
                    assertThat(Util.getInstance().RemoveAccent(areaData.getArea_name())).isEqualTo("tang 1");
                    assertThat(areaData.getMeta_search()).isEqualTo("tang 1");

                    List<TableDetailData> listTable = areaData.getListTable();
                    listTable.sort(Comparator.comparing(TableDetailData::getTable_name));
                    assertThat(listTable.size()).isEqualTo(3);

                    assertThat(listTable.get(0).getTable_name()).isEqualTo("table 1 of area 1");
                    assertThat(listTable.get(1).getTable_name()).isEqualTo("table 2 of area 1");
                    assertThat(listTable.get(2).getTable_name()).isEqualTo("table 3 of area 1");
                })
                .verifyComplete();

        area.createOrUpdateTableDetail(TableDetailData.builder()
                .group_id(group)
                .area_id(are1ID.get())
                .table_id(table1OfArea1.get())
                .table_name("table 1 update of area 1")
                .build()).block();

        area.getAreaById(AreaID.builder()
                        .group_id(group)
                        .area_id(are1ID.get())
                        .build())
                .as(StepVerifier::create)
                .consumeNextWith(areaData -> {
                    assertThat(Util.getInstance().RemoveAccent(areaData.getArea_name())).isEqualTo("tang 1");
                    assertThat(areaData.getMeta_search()).isEqualTo("tang 1");

                    List<TableDetailData> listTable = areaData.getListTable();
                    listTable.sort(Comparator.comparing(TableDetailData::getTable_name));
                    assertThat(listTable.size()).isEqualTo(3);

                    assertThat(listTable.get(0).getTable_name()).isEqualTo("table 1 update of area 1");
                    assertThat(listTable.get(1).getTable_name()).isEqualTo("table 2 of area 1");
                    assertThat(listTable.get(2).getTable_name()).isEqualTo("table 3 of area 1");
                })
                .verifyComplete();

        area.deleteArea(AreaData.builder()
                .group_id(group)
                .area_id(are1ID.get())
                .build()).block();

        area.getAreaById(AreaID.builder()
                        .group_id(group)
                        .area_id(are1ID.get())
                        .build())
                .as(StepVerifier::create)
                .verifyComplete();

        area.findArea(SearchQuery.builder()
                        .group_id(group)
                        .query("tang")
                        .page(0)
                        .size(10000)
                        .build())
                .sort(Comparator.comparing(AreaData::getArea_name))
                .as(StepVerifier::create)
                .consumeNextWith(areaData -> {
                    assertThat(Util.getInstance().RemoveAccent(areaData.getArea_name())).isEqualTo("tang 2");
                    assertThat(areaData.getMeta_search()).isEqualTo("tang 2");
                })
                .verifyComplete();

        area.findArea(SearchQuery.builder()
                        .group_id(group)
                        .query("tang 1")
                        .page(0)
                        .size(10000)
                        .build())
                .sort(Comparator.comparing(AreaData::getArea_name))
                .as(StepVerifier::create)
                .verifyComplete();


        area.createTableDetail(
                AreaData.builder()
                        .group_id(group)
                        .area_id(are2ID.get())
                        .listTable(Arrays.asList(new TableDetailData[]{
                                TableDetailData.builder()
                                        .table_name("table 4 of area 2")
                                        .build()
                        }))
                        .build()
        ).block();

        area.createOrUpdateTableDetail(TableDetailData.builder()
                        .group_id(group)
                        .area_id(are2ID.get())
                        .table_name("table 5 of area 2")
                        .build())
                .block();

        area.justCreateOrUpdateArea(
                        AreaData.builder()
                                .group_id(group)
                                .area_name("Tầng 3")
                                .listTable(Arrays.asList(new TableDetailData[]{
                                        TableDetailData.builder()
                                                .area_id("not exist")
                                                .table_name("table 1 of area 2")
                                                .build()
                                }))
                                .build()
                )
                .block();

        area.getAll(UserID.builder()
                        .group_id(group)
                        .page(0)
                        .size(10000)
                        .build())
                .sort(Comparator.comparing(AreaData::getArea_name))
                .as(StepVerifier::create)
                .consumeNextWith(areaData -> {
                    assertThat(Util.getInstance().RemoveAccent(areaData.getArea_name())).isEqualTo("tang 2");
                    assertThat(areaData.getMeta_search()).isEqualTo("tang 2");

                    List<TableDetailData> listTable = areaData.getListTable();
                    listTable.sort(Comparator.comparing(TableDetailData::getTable_name));
                    assertThat(listTable.size()).isEqualTo(5);

                    assertThat(listTable.get(0).getTable_name()).isEqualTo("table 1 of area 2");
                    assertThat(listTable.get(1).getTable_name()).isEqualTo("table 2 of area 2");
                    assertThat(listTable.get(2).getTable_name()).isEqualTo("table 3 of area 2");
                    assertThat(listTable.get(3).getTable_name()).isEqualTo("table 4 of area 2");
                    assertThat(listTable.get(4).getTable_name()).isEqualTo("table 5 of area 2");
                })
                .consumeNextWith(areaData -> {
                    assertThat(Util.getInstance().RemoveAccent(areaData.getArea_name())).isEqualTo("tang 3");
                    assertThat(areaData.getMeta_search()).isEqualTo("tang 3");
                })
                .verifyComplete();

        AtomicReference<String> table4OfArea2 = new AtomicReference<>(null);
        AtomicReference<String> table5OfArea2 = new AtomicReference<>(null);

        area.getAreaById(AreaID.builder()
                        .group_id(group)
                        .area_id(are2ID.get())
                        .build())
                .as(StepVerifier::create)
                .consumeNextWith(areaData -> {
                    assertThat(Util.getInstance().RemoveAccent(areaData.getArea_name())).isEqualTo("tang 2");
                    assertThat(areaData.getMeta_search()).isEqualTo("tang 2");

                    List<TableDetailData> listTable = areaData.getListTable();
                    listTable.sort(Comparator.comparing(TableDetailData::getTable_name));
                    assertThat(listTable.size()).isEqualTo(5);

                    assertThat(listTable.get(0).getTable_name()).isEqualTo("table 1 of area 2");
                    assertThat(listTable.get(1).getTable_name()).isEqualTo("table 2 of area 2");
                    assertThat(listTable.get(2).getTable_name()).isEqualTo("table 3 of area 2");
                    assertThat(listTable.get(3).getTable_name()).isEqualTo("table 4 of area 2");
                    assertThat(listTable.get(4).getTable_name()).isEqualTo("table 5 of area 2");
                    assertThat(listTable.get(3).getPrice()).isEqualTo(-1);
                    assertThat(listTable.get(4).getPrice()).isEqualTo(-1);
                    table4OfArea2.set(listTable.get(3).getTable_id());
                    table5OfArea2.set(listTable.get(4).getTable_id());
                })
                .verifyComplete();


        ProductPackage productPackage = ProductPackage.builder()
                .group_id(group)
                .package_second_id("save_pack_of_table5_area2")
                .note("notettt here!!")
                .image("img.jpeg")
                .discount_amount(10000)
                .discount_percent(10)
                .ship_price(20000)
                .voucher("voucher_1d")
                .package_type("deliver")
                .progress("{}")
                .status(UserPackageDetail.Status.CREATE)
                .area_id("area_1")
                .area_name("tang 1")
                .table_id("table_1")
                .table_name("ban 1")
                .payment(7868)
                .price(5544)
                .buyer(Buyer.builder()
                        .phone_number("555555")
                        .reciver_fullname("test create package")
                        .region_id(294)
                        .district_id(484)
                        .ward_id(10379)
                        .build())
                .product_units(new com.example.heroku.model.UserPackage[]{
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("noooooo")
                                .product_unit_second_id("noooooo")
                                .number_unit(3)
                                .discount_amount(19)
                                .discount_percent(10)
                                .note("note hhh")
                                .build(),
                        com.example.heroku.model.UserPackage.builder()
                                .product_second_id("noooooo")
                                .product_unit_second_id("noooooo")
                                .number_unit(4)
                                .build()
                })
                .build();
        userPackageAPI.SavePackage(productPackage)
                .block();

        area.setPackageID(TableDetailData.builder()
                        .group_id(group)
                        .area_id(are2ID.get())
                        .table_id(table5OfArea2.get())
                        .package_second_id("save_pack_of_table5_area2")
                        .build())
                .block();

        area.getAreaById(AreaID.builder()
                        .group_id(group)
                        .area_id(are2ID.get())
                        .build())
                .as(StepVerifier::create)
                .consumeNextWith(areaData -> {
                    assertThat(Util.getInstance().RemoveAccent(areaData.getArea_name())).isEqualTo("tang 2");
                    assertThat(areaData.getMeta_search()).isEqualTo("tang 2");

                    List<TableDetailData> listTable = areaData.getListTable();
                    listTable.sort(Comparator.comparing(TableDetailData::getTable_name));
                    assertThat(listTable.size()).isEqualTo(5);

                    assertThat(listTable.get(0).getTable_name()).isEqualTo("table 1 of area 2");
                    assertThat(listTable.get(1).getTable_name()).isEqualTo("table 2 of area 2");
                    assertThat(listTable.get(2).getTable_name()).isEqualTo("table 3 of area 2");
                    assertThat(listTable.get(3).getTable_name()).isEqualTo("table 4 of area 2");
                    assertThat(listTable.get(4).getTable_name()).isEqualTo("table 5 of area 2");
                    assertThat(listTable.get(3).getPrice()).isEqualTo(-1);
                    assertThat(listTable.get(4).getPrice()).isEqualTo(5544);
                })
                .verifyComplete();

        area.setPackageID(TableDetailData.builder()
                        .group_id(group)
                        .area_id(are2ID.get())
                        .table_id(table4OfArea2.get())
                        .package_second_id("save_pack_of_table5_area2")
                        .build())
                .block();

        area.getAreaById(AreaID.builder()
                        .group_id(group)
                        .area_id(are2ID.get())
                        .build())
                .as(StepVerifier::create)
                .consumeNextWith(areaData -> {
                    assertThat(Util.getInstance().RemoveAccent(areaData.getArea_name())).isEqualTo("tang 2");
                    assertThat(areaData.getMeta_search()).isEqualTo("tang 2");

                    List<TableDetailData> listTable = areaData.getListTable();
                    listTable.sort(Comparator.comparing(TableDetailData::getTable_name));
                    assertThat(listTable.size()).isEqualTo(5);

                    assertThat(listTable.get(0).getTable_name()).isEqualTo("table 1 of area 2");
                    assertThat(listTable.get(1).getTable_name()).isEqualTo("table 2 of area 2");
                    assertThat(listTable.get(2).getTable_name()).isEqualTo("table 3 of area 2");
                    assertThat(listTable.get(3).getTable_name()).isEqualTo("table 4 of area 2");
                    assertThat(listTable.get(4).getTable_name()).isEqualTo("table 5 of area 2");
                    assertThat(listTable.get(3).getPrice()).isEqualTo(5544);
                    assertThat(listTable.get(4).getPrice()).isEqualTo(-1);
                })
                .verifyComplete();

        userPackageAPI.DeletePackage(PackageID.builder()
                        .group_id(group)
                        .device_id("555555")
                        .package_id("save_pack_of_table5_area2")
                        .build())
                .block();

        buyer.deleteBuyer(group, "555555").block();
    }
}
