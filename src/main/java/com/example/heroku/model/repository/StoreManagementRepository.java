package com.example.heroku.model.repository;

import com.example.heroku.model.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


public interface StoreManagementRepository extends ReactiveCrudRepository<Store, Long> {

    @Query(value = "select create_product_view(:group_id)")
    Mono<Store> createStoreProductView(@Param("group_id")String groupID);

    @Query(value = "SELECT * FROM store WHERE store.group_id = :group_id")
    Mono<Store> getStore(@Param("group_id")String groupID);

    @Query(value = "SELECT * FROM store WHERE LOWER(store.group_id) LIKE :group_id")
    Flux<Store> findStore(@Param("group_id")String groupID);

    @Query(value = "SELECT * FROM store WHERE store.domain_url = :domain_url")
    Mono<Store> getStoreDomainUrl(@Param("domain_url")String domainUrl);

    @Query(value = "DELETE FROM store WHERE store.group_id = :group_id")
    Mono<Store> deleteStore(@Param("group_id")String groupID);

    @Query(value = "INSERT INTO store(group_id, name, time_open, address, phone ,domain_url, status, store_type, createat) VALUES (:group_id, :name, :time_open, :address, :phone, :domain_url, :status, :store_type, :createat) ON CONFLICT ON CONSTRAINT UQ_store DO UPDATE SET name = :name, time_open = :time_open, address = :address, phone = :phone, domain_url = :domain_url, status = :status, store_type = :store_type")
    Mono<Store> insertOrUpdate(@Param("group_id")String groupID, @Param("name")String name, @Param("time_open")String time_open, @Param("address")String address, @Param("phone")String phone, @Param("domain_url")String domain_url, @Param("status")Store.Status status, @Param("store_type")Store.StoreType storeType, @Param("createat") LocalDateTime createat);

    @Query(value = "UPDATE store SET name = :name, time_open = :time_open, address = :address, phone = :phone, domain_url = :domain_url, status = :status, store_type = :store_type WHERE group_id = :group_id")
    Mono<Store> update(@Param("group_id")String groupID, @Param("name")String name, @Param("time_open")String time_open, @Param("address")String address, @Param("phone")String phone, @Param("domain_url")String domain_url, @Param("status")Store.Status status, @Param("store_type")Store.StoreType storeType);

    @Query(value = "UPDATE store SET payment_status = :payment_status WHERE group_id = :group_id")
    Mono<Store> updatePaymentStatus(@Param("group_id")String groupID, @Param("payment_status")String name);

    @Query(value = "SELECT store.* FROM (SELECT DISTINCT user_package_detail.group_id FROM user_package_detail WHERE meta_search LIKE :meta_search AND user_package_detail.status = 'DONE') AS groupid INNER JOIN (SELECT * FROM store WHERE store.payment_status IS NULL) AS store ON store.group_id = groupid.group_id")
    Flux<Store> getAllStoreBaseonDonePackage(@Param("meta_search")String metaSearch);

    @Query(value = "SELECT * FROM store WHERE store.createat BETWEEN :fromtime AND :totime")
    Flux<Store> getAllStoreCreateBetween(@Param("fromtime") LocalDateTime from, @Param("totime") LocalDateTime to);

    @Query(value = "SELECT * FROM store")
    Flux<Store> findAll(Pageable pageable);

}
