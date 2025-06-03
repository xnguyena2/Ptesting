package com.example.heroku.model.repository;

import com.example.heroku.model.Buyer;
import com.example.heroku.model.statistics.DebtOfBuyer;
import com.example.heroku.status.ActiveStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface BuyerRepository extends ReactiveCrudRepository<Buyer, Long> {

    @Query(value = "SELECT * FROM buyer WHERE buyer.group_id = :group_id LIMIT :size OFFSET (:page * :size)")
    Flux<Buyer> findByGroupID(@Param("group_id") String group_id, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM (SELECT * FROM buyer WHERE buyer.group_id = :group_id AND (reciver_fullname <> '' OR phone_number <> '') LIMIT :size OFFSET (:page * :size)) AS buyer LEFT JOIN (SELECT device_id, SUM(CASE WHEN transaction_type = 'INCOME' THEN amount ELSE 0 END) AS in_come, SUM(CASE WHEN transaction_type = 'OUTCOME' THEN amount ELSE 0 END) AS out_come FROM debt_transaction WHERE group_id = :group_id GROUP BY device_id) AS debt ON debt.device_id = buyer.device_id")
    Flux<DebtOfBuyer> findByGroupIDWithoutEmpty(@Param("group_id") String group_id, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM buyer WHERE buyer.group_id = :group_id AND buyer.device_id = :device_id")
    Mono<Buyer> findByGroupIDAndDeviceID(@Param("group_id") String group_id, @Param("device_id") String device_id);

    @Query(value = "SELECT * FROM buyer WHERE buyer.group_id = :group_id AND buyer.meta_search LIKE :phone LIMIT :size OFFSET (:page * :size)")
    Flux<Buyer> findByPhoneOrDeviceIDContains(@Param("group_id") String group_id, @Param("phone") String phone, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM buyer WHERE buyer.group_id = :group_id AND buyer.phone_number_clean = :phone LIMIT 1")
    Mono<Buyer> findByPhoneClean(@Param("group_id") String group_id, @Param("phone") String phone);

    @Query(value = "DELETE FROM buyer WHERE buyer.group_id = :group_id AND (buyer.device_id = :phone OR buyer.phone_number_clean = :phone)")
    Mono<Buyer> deleteByPhone(@Param("group_id") String group_id, @Param("phone") String page);

    @Query(value = "UPDATE buyer SET total_price = buyer.total_price + :total_price, real_price = buyer.real_price + :real_price, ship_price = buyer.ship_price + :ship_price, discount = buyer.discount + :discount, point = buyer.point + :point WHERE buyer.group_id = :group_id AND buyer.device_id = :device_id")
    Mono<Buyer> updateMoney(@Param("group_id") String group_id, @Param("device_id") String device_id,
                               @Param("total_price") float total_price, @Param("real_price") float real_price,
                               @Param("ship_price") float ship_price, @Param("discount") float discount, @Param("point") int point);

    @Query(value = "INSERT INTO buyer(group_id, device_id, reciver_address, region_id, district_id, ward_id, reciver_fullname, phone_number, phone_number_clean, meta_search, total_price, real_price, ship_price, discount, point, status, createat) VALUES (:group_id, :device_id, :reciver_address, :region_id, :district_id, :ward_id, :reciver_fullname, :phone_number, :phone_number_clean, :meta_search, :total_price, :real_price, :ship_price, :discount, :point, :status, :createat) ON CONFLICT (group_id, device_id) DO UPDATE SET reciver_address = :reciver_address, total_price = buyer.total_price + :total_price, real_price = buyer.real_price + :real_price, ship_price = buyer.ship_price + :ship_price, discount = buyer.discount + :discount, point = buyer.point + :point, region_id = :region_id, district_id = :district_id, ward_id = :ward_id, reciver_fullname = :reciver_fullname, phone_number = :phone_number, phone_number_clean = :phone_number_clean, meta_search = :meta_search, status = :status, createat = :createat")
    Mono<Buyer> insertOrUpdate(@Param("group_id") String group_id, @Param("device_id") String device_id,
                               @Param("total_price") float total_price, @Param("real_price") float real_price,
                               @Param("ship_price") float ship_price, @Param("discount") float discount, @Param("point") int point,
                               @Param("reciver_address") String reciver_address,
                               @Param("region_id") int region_id, @Param("district_id") int district_id, @Param("ward_id") int ward_id,
                               @Param("reciver_fullname") String reciver_fullname, @Param("phone_number") String phone_number,
                               @Param("phone_number_clean") String phone_number_clean, @Param("meta_search") String meta_search,
                               @Param("status") ActiveStatus status, @Param("createat") Timestamp createat);
}
