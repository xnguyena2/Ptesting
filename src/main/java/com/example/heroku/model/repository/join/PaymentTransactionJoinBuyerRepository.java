package com.example.heroku.model.repository.join;

import com.example.heroku.model.joinwith.PaymentTransactionJoinBuyer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface PaymentTransactionJoinBuyerRepository extends ReactiveCrudRepository<PaymentTransactionJoinBuyer, Long> {

    @Query(value = "SELECT payment_transaction.*, buyer.device_id, buyer.reciver_address, buyer.region_id, buyer.district_id, buyer.ward_id, buyer.reciver_fullname, buyer.phone_number, buyer.phone_number_clean, buyer.total_price, buyer.real_price, buyer.ship_price, buyer.discount, buyer.point, buyer.meta_search, buyer.status AS buyer_status, buyer.group_id AS buyer_group_id, buyer.createat AS buyer_createat FROM payment_transaction LEFT JOIN (SELECT * FROM buyer WHERE group_id = :group_id) AS buyer ON payment_transaction.device_id = buyer.device_id WHERE payment_transaction.group_id = :group_id AND payment_transaction.package_second_id = :package_second_id")
    Flux<PaymentTransactionJoinBuyer> getTransactionByPackageID(@Param("group_id") String group_id, @Param("package_second_id") String package_second_id);

    @Query(value = "SELECT payment_transaction.*, buyer.device_id, buyer.reciver_address, buyer.region_id, buyer.district_id, buyer.ward_id, buyer.reciver_fullname, buyer.phone_number, buyer.phone_number_clean, buyer.total_price, buyer.real_price, buyer.ship_price, buyer.discount, buyer.point, buyer.meta_search, buyer.status AS buyer_status, buyer.group_id AS buyer_group_id, buyer.createat AS buyer_createat FROM payment_transaction LEFT JOIN (SELECT * FROM buyer WHERE group_id = :group_id) AS buyer ON payment_transaction.device_id = buyer.device_id WHERE payment_transaction.group_id = :group_id AND (payment_transaction.createat BETWEEN :fromtime AND :totime)")
    Flux<PaymentTransactionJoinBuyer> getStatictis(@Param("group_id") String groupID, @Param("fromtime") LocalDateTime from, @Param("totime") LocalDateTime to);
}
