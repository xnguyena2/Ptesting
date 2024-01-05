package com.example.heroku;

import com.example.heroku.request.beer.BeerSubmitData;
import com.example.heroku.services.Store;
import lombok.Builder;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@Builder
public class StoreTest {
    Store storeServices;

    String group;

    public void test() {
        storeServices.createStoreProductView(group).block();
        group = group + "_new";
        storeServices.createOrUpdateStore(com.example.heroku.model.Store.builder()
                .group_id(group)
                .phone("1212121121")
                .name("store name")
                .time_open("time open")
                .address("address")
                .status(com.example.heroku.model.Store.Status.ACTIVE)
                .store_type(com.example.heroku.model.Store.StoreType.DONTHAVETABLE)
                .build()).block();
        storeServices.getStore(group)
                .as(StepVerifier::create)
                .consumeNextWith(store -> {
                    assertThat(store.getGroup_id()).isEqualTo(group);
                    assertThat(store.getPhone()).isEqualTo("1212121121");
                    assertThat(store.getName()).isEqualTo("store name");
                    assertThat(store.getTime_open()).isEqualTo("time open");
                    assertThat(store.getAddress()).isEqualTo("address");
                    assertThat(store.getStatus()).isEqualTo(com.example.heroku.model.Store.Status.ACTIVE);
                    assertThat(store.getStore_type()).isEqualTo(com.example.heroku.model.Store.StoreType.DONTHAVETABLE);

                })
                .verifyComplete();
        storeServices.createOrUpdateStore(com.example.heroku.model.Store.builder()
                .group_id(group)
                .phone("121212112133")
                .name("store name")
                .time_open("time open")
                .address("address")
                .status(com.example.heroku.model.Store.Status.ACTIVE)
                .store_type(com.example.heroku.model.Store.StoreType.HAVETABLE)
                .build()).block();
        storeServices.getStore(group)
                .as(StepVerifier::create)
                .consumeNextWith(store -> {
                    assertThat(store.getGroup_id()).isEqualTo(group);
                    assertThat(store.getPhone()).isEqualTo("121212112133");
                    assertThat(store.getName()).isEqualTo("store name");
                    assertThat(store.getTime_open()).isEqualTo("time open");
                    assertThat(store.getAddress()).isEqualTo("address");
                    assertThat(store.getStatus()).isEqualTo(com.example.heroku.model.Store.Status.ACTIVE);
                    assertThat(store.getStore_type()).isEqualTo(com.example.heroku.model.Store.StoreType.HAVETABLE);

                })
                .verifyComplete();
        storeServices.update(com.example.heroku.model.Store.builder()
                .group_id(group)
                .phone("12121211213344")
                .name("store name4")
                .time_open("time open4")
                .address("address4")
                .status(com.example.heroku.model.Store.Status.CLOSE)
                .store_type(com.example.heroku.model.Store.StoreType.DONTHAVETABLE)
                .build()).block();
        storeServices.getStore(group)
                .as(StepVerifier::create)
                .consumeNextWith(store -> {
                    assertThat(store.getGroup_id()).isEqualTo(group);
                    assertThat(store.getPhone()).isEqualTo("12121211213344");
                    assertThat(store.getName()).isEqualTo("store name4");
                    assertThat(store.getTime_open()).isEqualTo("time open4");
                    assertThat(store.getAddress()).isEqualTo("address4");
                    assertThat(store.getStatus()).isEqualTo(com.example.heroku.model.Store.Status.CLOSE);
                    assertThat(store.getStore_type()).isEqualTo(com.example.heroku.model.Store.StoreType.DONTHAVETABLE);

                })
                .verifyComplete();
    }
}
