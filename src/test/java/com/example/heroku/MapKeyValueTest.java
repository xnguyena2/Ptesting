package com.example.heroku;

import com.example.heroku.services.MapKeyValue;
import lombok.Builder;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class MapKeyValueTest {
    MapKeyValue mapKeyValueServices;

    String group;

    public void test() {
        mapKeyValueServices.insert(com.example.heroku.model.MapKeyValue.builder()
                .group_id(group)
                .id_o("id1")
                .value_o("vl1")
                .build()).block();

        mapKeyValueServices.insert(com.example.heroku.model.MapKeyValue.builder()
                .group_id(group)
                .id_o("id2")
                .value_o("vl2")
                .build()).block();

        mapKeyValueServices.getByID(group, "id1")
                .as(StepVerifier::create)
                .consumeNextWith(store -> {
                    assertThat(store.getGroup_id()).isEqualTo(group);
                    assertThat(store.getValue_o()).isEqualTo("vl1");
                    assertThat(store.getId_o()).isEqualTo("id1");
                })
                .verifyComplete();

        mapKeyValueServices.getByID(group, "id2")
                .as(StepVerifier::create)
                .consumeNextWith(store -> {
                    assertThat(store.getGroup_id()).isEqualTo(group);
                    assertThat(store.getValue_o()).isEqualTo("vl2");
                    assertThat(store.getId_o()).isEqualTo("id2");
                })
                .verifyComplete();

        mapKeyValueServices.deleteByID(group, "id2").block();

        mapKeyValueServices.getByID(group, "id1")
                .as(StepVerifier::create)
                .consumeNextWith(store -> {
                    assertThat(store.getGroup_id()).isEqualTo(group);
                    assertThat(store.getValue_o()).isEqualTo("vl1");
                    assertThat(store.getId_o()).isEqualTo("id1");
                })
                .verifyComplete();

        mapKeyValueServices.getByID(group, "id2")
                .as(StepVerifier::create)
                .verifyComplete();
    }
}
