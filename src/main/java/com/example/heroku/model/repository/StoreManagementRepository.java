package com.example.heroku.model.repository;

import com.example.heroku.model.Store;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface StoreManagementRepository extends ReactiveCrudRepository<Store, Long> {

    @Query(value = "select create_product_view(:group_id)")
    Mono<Store> createStoreProductView(@Param("group_id")String groupID);

    @Query(value = "SELECT * FROM store WHERE store.group_id = :group_id")
    Mono<Store> getStore(@Param("group_id")String groupID);

    @Query(value = "DELETE FROM store WHERE store.group_id = :group_id")
    Mono<Store> deleteStore(@Param("group_id")String groupID);

    @Query(value = "INSERT INTO store(group_id, name, time_open, address, phone, status, createat) VALUES (:group_id, :name, :time_open, :address, :phone, :status, :createat) ON CONFLICT (group_id) DO UPDATE store SET name = :name, time_open = :time_open, address = :address, phone = :phone, status = :status WHERE store.group_id = :group_id")
    Mono<Store> updateStore(@Param("group_id")String groupID, @Param("name")String name, @Param("time_open")String time_open, @Param("address")String address, @Param("phone")String phone, @Param("status")Store.Status status, @Param("createat")Timestamp createat);

}
