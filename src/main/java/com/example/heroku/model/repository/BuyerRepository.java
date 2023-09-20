package com.example.heroku.model.repository;

import com.example.heroku.model.Buyer;
import com.example.heroku.model.PackageOrder;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface BuyerRepository extends ReactiveCrudRepository<Buyer, String> {

    @Query(value = "SELECT buyer_main_info.*, real_price, points_discount FROM ( SELECT phone_number_clean, phone_number, reciver_address, region_id, district_id, ward_id, reciver_fullname FROM ( SELECT phone_number_clean, phone_number, reciver_address, region_id, district_id, ward_id, reciver_fullname, row_number() over( PARTITION BY phone_number_clean ORDER BY phone_number_clean DESC ) AS roworder FROM package_order WHERE package_order.group_id = :group_id ) TEMP WHERE roworder = 1 ) buyer_main_info INNER JOIN ( SELECT phone_number_clean, SUM(real_price) AS real_price, SUM(points_discount) AS points_discount FROM package_order WHERE package_order.group_id = :group_id AND status = :status GROUP BY phone_number_clean ) buyer_real_price ON buyer_main_info.phone_number_clean = buyer_real_price.phone_number_clean LIMIT :size OFFSET (:page * :size)")
    Flux<Buyer> getAll(@Param("status") PackageOrder.Status status, @Param("group_id") String group_id, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM buyer WHERE buyer.group_id = :group_id LIMIT :size OFFSET (:page * :size)")
    Flux<Buyer> findByGroupID(@Param("group_id") String group_id, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM buyer WHERE buyer.group_id = :group_id AND buyer.device_id = :device_id")
    Mono<Buyer> findByGroupIDAndDeviceID(@Param("group_id") String group_id, @Param("device_id") String device_id);

    @Query(value = "SELECT * FROM buyer WHERE buyer.group_id = :group_id AND (buyer.device_id LIKE :phone OR buyer.phone_number_clean LIKE :phone) LIMIT :size OFFSET (:page * :size)")
    Flux<Buyer> findByPhoneOrDeviceIDContains(@Param("group_id") String group_id, @Param("phone") String phone, @Param("page") int page, @Param("size") int size);

    @Query(value = "DELETE FROM buyer WHERE buyer.group_id = :group_id AND (buyer.device_id = :phone OR buyer.phone_number_clean = :phone)")
    Mono<Buyer> deleteByPhone(@Param("group_id") String group_id, @Param("phone") String page);

    @Query(value = "INSERT INTO buyer(group_id, device_id, reciver_address, region_id, district_id, ward_id, reciver_fullname, phone_number, phone_number_clean, status, createat) VALUES (:group_id, :device_id, :reciver_address, :region_id, :district_id, :ward_id, :reciver_fullname, :phone_number, :phone_number_clean, :status, :createat) ON CONFLICT (group_id, device_id) DO UPDATE SET reciver_address = :reciver_address, region_id = :region_id, district_id = :district_id, ward_id = :ward_id, reciver_fullname = :reciver_fullname, phone_number = :phone_number, phone_number_clean = :phone_number_clean, status = :status, createat = :createat")
    Mono<Buyer> insertOrUpdate(@Param("group_id") String group_id, @Param("device_id") String device_id, @Param("reciver_address") String reciver_address,
                                 @Param("region_id") int region_id, @Param("district_id") int district_id, @Param("ward_id") int ward_id,
                                 @Param("reciver_fullname") String reciver_fullname, @Param("phone_number") String phone_number,
                                 @Param("phone_number_clean") String phone_number_clean,
                                 @Param("status") String status, @Param("createat") Timestamp createat);
}
