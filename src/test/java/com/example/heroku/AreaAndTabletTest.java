package com.example.heroku;

import com.example.heroku.request.client.AreaID;
import com.example.heroku.request.client.UserID;
import com.example.heroku.response.AreaData;
import com.example.heroku.response.TableDetailData;
import com.example.heroku.services.Area;
import lombok.Builder;
import org.assertj.core.api.Assert;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class AreaAndTabletTest {
    Area area;

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
        ).block();

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
        ).block();

        area.getAll(UserID.builder()
                        .group_id(group)
                        .page(0)
                        .size(10000)
                        .build())
                .as(StepVerifier::create)
                .verifyComplete();


        AtomicReference<String> are1ID = new AtomicReference<>(null);
        area.justCreateOrUpdateArea(
                        AreaData.builder()
                                .group_id(group)
                                .group_id(group)
                                .area_name("tang 1")
                                .listTable(Arrays.asList(new TableDetailData[]{
                                        TableDetailData.builder()
                                                .area_id("not exist")
                                                .table_name("table 1 of area 1")
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
                .as(StepVerifier::create)
                .consumeNextWith(areaData -> {
                    assertThat(areaData.getArea_name()).isEqualTo("tang 1");
                    are1ID.set(areaData.getArea_id());
                })
                .verifyComplete();

        area.getAreaById(AreaID.builder()
                        .group_id(group)
                        .area_id(are1ID.get())
                        .build())
                .as(StepVerifier::create)
                .consumeNextWith(areaData -> {
                    assertThat(areaData.getArea_name()).isEqualTo("tang 1");
                })
                .verifyComplete();
    }
}
