package com.example.heroku.model.repository;

import com.example.heroku.model.Buyer;
import com.example.heroku.model.PackageOrder;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BuyerRepository extends ReactiveCrudRepository<Buyer, String> {

    @Query(value = "SELECT buyer_main_info.*, real_price, points_discount FROM ( SELECT phone_number_clean, phone_number, reciver_address, region_id, district_id, ward_id, reciver_fullname FROM ( SELECT phone_number_clean, phone_number, reciver_address, region_id, district_id, ward_id, reciver_fullname, row_number() over( PARTITION BY phone_number_clean ORDER BY phone_number_clean DESC ) AS roworder FROM package_order WHERE package_order.group_id = :group_id ) TEMP WHERE roworder = 1 ) buyer_main_info INNER JOIN ( SELECT phone_number_clean, SUM(real_price) AS real_price, SUM(points_discount) AS points_discount FROM package_order WHERE package_order.group_id = :group_id AND status = :status GROUP BY phone_number_clean ) buyer_real_price ON buyer_main_info.phone_number_clean = buyer_real_price.phone_number_clean LIMIT :size OFFSET (:page * :size)")
    Flux<Buyer> getAll(@Param("status") PackageOrder.Status status, @Param("group_id")String group_id, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM buyer WHERE buyer.group_id = :group_id LIMIT :size OFFSET (:page * :size)")
    Flux<Buyer> findByGroupID(@Param("group_id")String group_id, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM buyer WHERE buyer.group_id = :group_id AND (buyer.device_id LIKE :phone OR buyer.phone_number_clean LIKE :phone) LIMIT :size OFFSET (:page * :size)")
    Flux<Buyer> findByPhoneOrDeviceIDContains(@Param("group_id")String group_id, @Param("phone")String phone, @Param("page")int page, @Param("size")int size);

    @Query(value = "DELETE FROM buyer WHERE buyer.group_id = :group_id AND (buyer.device_id = :phone OR buyer.phone_number_clean = :phone)")
    Mono<Buyer> deleteByPhone(@Param("group_id")String group_id, @Param("phone")String page);
}
