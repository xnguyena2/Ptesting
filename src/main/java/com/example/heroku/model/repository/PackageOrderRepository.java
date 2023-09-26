package com.example.heroku.model.repository;

import com.example.heroku.model.PackageOrder;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PackageOrderRepository extends ReactiveCrudRepository<PackageOrder, Long> {

    @Query(value = "SELECT * FROM package_order WHERE package_order.group_id = :group_id AND package_order.status = :status AND DATE_PART('day', NOW() - createat) <= :date LIMIT :size OFFSET (:page * :size)")
    Flux<PackageOrder> getAll(@Param("group_id")String group_id, @Param("page")int page, @Param("size")int size, @Param("status") PackageOrder.Status status, @Param("date") int date);

    @Query(value = "SELECT * FROM package_order WHERE package_order.group_id = :group_id AND package_order.package_order_second_id = :id")
    Mono<PackageOrder> getByID(@Param("group_id")String group_id, @Param("id")String id);

    @Query(value = "UPDATE package_order SET status = :status WHERE package_order.group_id = :group_id AND package_order.package_order_second_id = :id")
    Mono<PackageOrder> changeStatus(@Param("group_id")String group_id, @Param("id") String id, @Param("status") PackageOrder.Status status);

}
