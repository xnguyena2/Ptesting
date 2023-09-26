package com.example.heroku.model.repository;

import com.example.heroku.model.Store;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface StoreManagementRepository extends ReactiveCrudRepository<Store, Long> {

    @Query(value = "select create_product_view(:group_id)")
    Mono<Store> createStoreProductView(@Param("group_id")String groupID);
}
