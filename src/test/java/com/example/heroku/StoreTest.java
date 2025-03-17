package com.example.heroku;

import com.example.heroku.services.Store;
import lombok.Builder;
import reactor.test.StepVerifier;

import java.sql.Timestamp;

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
                .build().AutoFill(group)).block();
        storeServices.getStore(group)
                .as(StepVerifier::create)
                .consumeNextWith(store -> {
                    assertThat(store.getGroup_id()).isEqualTo(group);
                    assertThat(store.getDomain_url()).isEqualTo(group);
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
                .build().AutoFill(group)).block();
        storeServices.getStore(group)
                .as(StepVerifier::create)
                .consumeNextWith(store -> {
                    assertThat(store.getGroup_id()).isEqualTo(group);
                    assertThat(store.getDomain_url()).isEqualTo(group);
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
                .domain_url(group + "hello")
                .name("store name4")
                .time_open("time open4")
                .address("address4")
                .status(com.example.heroku.model.Store.Status.CLOSE)
                .store_type(com.example.heroku.model.Store.StoreType.DONTHAVETABLE)
                .build()).block();
        storeServices.updatePaymentStatus(group, "NOT_PAID")
                .block();
        storeServices.getStore(group)
                .as(StepVerifier::create)
                .consumeNextWith(store -> {
                    assertThat(store.getGroup_id()).isEqualTo(group);
                    assertThat(store.getDomain_url()).isEqualTo(group + "hello");
                    assertThat(store.getPhone()).isEqualTo("12121211213344");
                    assertThat(store.getName()).isEqualTo("store name4");
                    assertThat(store.getTime_open()).isEqualTo("time open4");
                    assertThat(store.getAddress()).isEqualTo("address4");
                    assertThat(store.getPayment_status()).isEqualTo("NOT_PAID");
                    assertThat(store.getStatus()).isEqualTo(com.example.heroku.model.Store.Status.CLOSE);
                    assertThat(store.getStore_type()).isEqualTo(com.example.heroku.model.Store.StoreType.DONTHAVETABLE);

                })
                .verifyComplete();
        storeServices.findStore(group)
                .as(StepVerifier::create)
                .consumeNextWith(store -> {
                    assertThat(store.getGroup_id()).isEqualTo(group);
                    assertThat(store.getDomain_url()).isEqualTo(group + "hello");
                    assertThat(store.getPhone()).isEqualTo("12121211213344");
                    assertThat(store.getName()).isEqualTo("store name4");
                    assertThat(store.getTime_open()).isEqualTo("time open4");
                    assertThat(store.getAddress()).isEqualTo("address4");
                    assertThat(store.getPayment_status()).isEqualTo("NOT_PAID");
                    assertThat(store.getStatus()).isEqualTo(com.example.heroku.model.Store.Status.CLOSE);
                    assertThat(store.getStore_type()).isEqualTo(com.example.heroku.model.Store.StoreType.DONTHAVETABLE);

                })
                .verifyComplete();
        storeServices.getAllStoreBaseonDonePackage("")
                .as(StepVerifier::create)
                .verifyComplete();

        storeServices.getAllStoreCreateBetween(com.example.heroku.request.warehouse.SearchImportQuery.builder()
                        .from(Timestamp.valueOf("2023-11-01 00:00:00")).to(Timestamp.valueOf("2300-11-01 00:00:00")).build())
                .filter(store -> store.getGroup_id().equals(group))
                .as(StepVerifier::create)
                .consumeNextWith(store -> {
                    assertThat(store.getGroup_id()).isEqualTo(group);
                    assertThat(store.getDomain_url()).isEqualTo(group + "hello");
                    assertThat(store.getPhone()).isEqualTo("12121211213344");
                    assertThat(store.getName()).isEqualTo("store name4");
                    assertThat(store.getTime_open()).isEqualTo("time open4");
                    assertThat(store.getAddress()).isEqualTo("address4");
                    assertThat(store.getPayment_status()).isEqualTo("NOT_PAID");
                    assertThat(store.getStatus()).isEqualTo(com.example.heroku.model.Store.Status.CLOSE);
                    assertThat(store.getStore_type()).isEqualTo(com.example.heroku.model.Store.StoreType.DONTHAVETABLE);

                })
                .verifyComplete();
    }
}
