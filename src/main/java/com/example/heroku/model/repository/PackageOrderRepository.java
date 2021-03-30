package com.example.heroku.model.repository;

import com.example.heroku.model.PackageOrder;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PackageOrderRepository  extends ReactiveCrudRepository<PackageOrder, String> {

    @Query(value = "SELECT * FROM package_order WHERE package_order.status = :status LIMIT :size OFFSET (:page*:size)")
    Flux<PackageOrder> getAll(@Param("page")int page, @Param("size")int size, @Param("status") PackageOrder.Status status);

    @Query(value = "UPDATE package_order SET package_order.status = :status WHERE package_order.package_order_second_id = :id")
    Mono<PackageOrder> changeStatus(@Param("id") String id, @Param("status") PackageOrder.Status status);

}
