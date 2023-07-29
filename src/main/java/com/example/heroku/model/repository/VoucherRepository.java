package com.example.heroku.model.repository;

import com.example.heroku.model.Voucher;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoucherRepository extends ReactiveCrudRepository<Voucher, String> {

    @Query(value = "DELETE FROM voucher WHERE voucher.group_id = :group_id AND voucher.voucher_second_id = :id")
    Mono<Voucher> deleteBySecondId(@Param("group_id")String group_id, @Param("id")String id);

    @Query(value = "SELECT * FROM voucher WHERE voucher.group_id = :group_id ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Voucher> findAll(@Param("group_id")String group_id, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM voucher WHERE voucher.group_id = :group_id AND voucher.voucher_second_id = :id")
    Mono<Voucher> getVoucherBySecondID(@Param("group_id")String group_id, @Param("id")String id);


//    @Query(value = "(SELECT v.id, v.voucher_second_id, v.detail, v.discount, v.amount, case when v.reuse > v_d.reuse then v_d.reuse else v.reuse end as reuse, v.for_all_product, v.for_all_user, v.package_voucher, v.date_expire, v.status, v.createat FROM voucher v LEFT JOIN voucher_relate_product v_b ON v_b.voucher_second_id = v.voucher_second_id LEFT JOIN voucher_relate_user_device v_d ON v_d.voucher_second_id = v.voucher_second_id WHERE v.voucher_second_id = :id AND ((v.for_all_product = TRUE OR v_b.product_second_id = :beer_id) AND ((v.reuse > 0 AND v_d.reuse > 0 AND v_d.device_id = :user_device)))) UNION (SELECT * FROM voucher v WHERE NOT EXISTS (SELECT * FROM voucher_relate_user_device v_d WHERE v.voucher_second_id = v_d.voucher_second_id AND v_d.device_id = :user_device) AND v.reuse > 0 AND v.for_all_user = true AND v.voucher_second_id = :id)")
    @Query(value = "SELECT v.id, v.group_id, v.voucher_second_id, v.detail, v.discount, v.amount, case when v.reuse > v_d.reuse then v_d.reuse else v.reuse end as reuse, v.for_all_product, v.for_all_user, v.package_voucher, v.date_expire, v.status, v.createat FROM ( SELECT * FROM voucher WHERE voucher.group_id = :group_id AND voucher.package_voucher = false AND voucher.voucher_second_id = :voucher_id ) AS v LEFT JOIN ( SELECT * FROM voucher_relate_product WHERE voucher_relate_product.group_id = :group_id AND voucher_relate_product.product_second_id = :product_id ) AS v_b ON v_b.voucher_second_id = v.voucher_second_id LEFT JOIN ( SELECT * FROM voucher_relate_user_device WHERE voucher_relate_user_device.group_id = :group_id AND voucher_relate_user_device.device_id = :user_device ) AS v_d ON v_d.voucher_second_id = v.voucher_second_id WHERE ( v.reuse > 0 AND ( v_d.reuse > 0 OR v_d.reuse IS NULL ) ) AND ( v.for_all_product = true OR v_b.product_second_id = :product_id ) AND ( v.for_all_user = true OR v_d.device_id = :user_device )")
    Mono<Voucher> getVoucherBySecondIDAndUserDeviceAndBeerSecondID(@Param("group_id")String group_id, @Param("voucher_id")String voucher_id, @Param("user_device")String userDevice, @Param("product_id")String productID);

    @Query(value = "SELECT v.id, v.group_id, v.voucher_second_id, v.detail, v.discount, v.amount, CASE WHEN v.reuse > v_d.reuse THEN v_d.reuse ELSE v.reuse END AS REUSE, v.for_all_product, v.for_all_user, v.package_voucher, v.date_expire, v.status, v.createat FROM ( SELECT * FROM voucher WHERE voucher.group_id = :group_id ) v LEFT JOIN ( SELECT * FROM voucher_relate_user_device WHERE voucher_relate_user_device.group_id = :group_id AND voucher_relate_user_device.device_id = :user_device ) AS v_d ON v_d.voucher_second_id = v.voucher_second_id WHERE v.reuse > 0 AND ( v_d.reuse > 0 OR v_d.reuse IS NULL ) AND ( v.for_all_user = TRUE OR v_d.device_id = :user_device ) ORDER BY createat DESC")
    Flux<Voucher> getAllVoucherOfUserDevice(@Param("group_id")String group_id, @Param("user_device")String userDevice);

    // remove patrons voucher
    //@Query(value = "SELECT v.id, v.voucher_second_id, v.detail, v.discount, v.amount, CASE WHEN v_d.reuse < v.reuse THEN v_d.reuse ELSE v.reuse END AS REUSE, v.for_all_product, v.for_all_user, v.date_expire, v.status, v.createat FROM (SELECT voucher.* FROM voucher LEFT JOIN voucher_relate_product ON voucher.voucher_second_id = voucher_relate_product.voucher_second_id WHERE voucher.voucher_second_id = :voucher AND (voucher.for_all_product OR voucher_relate_product.product_second_id = :beer)) v LEFT JOIN ( (SELECT :device AS device_id, REUSE, voucher_second_id FROM voucher WHERE EXISTS (SELECT * FROM package_order WHERE phone_number_clean = :phoneclean) AND voucher_second_id = 'patrons') UNION (SELECT device_id, REUSE, voucher_second_id FROM voucher_relate_user_device)) v_d ON v.voucher_second_id = v_d.voucher_second_id WHERE v.for_all_user OR v_d.device_id = :device")
    @Query(value = "SELECT v.id, v.group_id, v.voucher_second_id, v.detail, v.discount, v.amount, CASE WHEN v_d.reuse < v.reuse THEN v_d.reuse ELSE v.reuse END AS REUSE, v.for_all_product, v.for_all_user, v.package_voucher, v.date_expire, v.status, v.createat FROM ( SELECT voucher.* FROM voucher LEFT JOIN voucher_relate_product ON voucher.voucher_second_id = voucher_relate_product.voucher_second_id WHERE voucher.group_id = :group_id AND voucher.voucher_second_id = :voucher AND ( voucher.for_all_product OR voucher_relate_product.product_second_id = :beer ) AND voucher.package_voucher = false ) v LEFT JOIN ( SELECT device_id, REUSE, voucher_second_id FROM voucher_relate_user_device WHERE voucher_relate_user_device.group_id = :group_id ) v_d ON v.voucher_second_id = v_d.voucher_second_id WHERE ( v.reuse > 0 AND ( v_d.reuse > 0 OR v_d.reuse IS NULL ) ) AND ( v.for_all_user OR v_d.device_id = :device )")
    Mono<Voucher> getVoucherOfUser(@Param("group_id")String group_id, @Param("voucher")String voucher, @Param("device")String device, @Param("beer")String beerSecondID);

    @Query(value = "SELECT v.id, v.group_id, v.voucher_second_id, v.detail, v.discount, v.amount, case when v.reuse > v_d.reuse then v_d.reuse else v.reuse end as reuse, v.for_all_product, v.for_all_user, v.package_voucher, v.date_expire, v.status, v.createat FROM ( SELECT * FROM voucher WHERE voucher.group_id = :group_id AND voucher.package_voucher = true AND voucher.voucher_second_id = :voucher_id ) AS v LEFT JOIN ( SELECT * FROM voucher_relate_user_device WHERE voucher_relate_user_device.group_id = :group_id AND voucher_relate_user_device.device_id = :user_device ) AS v_d ON v_d.voucher_second_id = v.voucher_second_id WHERE ( v.reuse > 0 AND ( v_d.reuse > 0 OR v_d.reuse IS NULL ) ) AND ( v.for_all_user = true OR v_d.device_id = :user_device )")
    Mono<Voucher> getPackageVoucherBySecondIDAndUserDevice(@Param("group_id")String group_id, @Param("voucher_id")String voucher_id, @Param("user_device")String userDevice);

}
