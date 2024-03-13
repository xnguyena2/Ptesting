package com.example.heroku.model.repository;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.joinwith.UserPackageDetailJoinWithUserPackage;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface JoinUserPackageDetailWithUserPackgeRepository extends ReactiveCrudRepository<UserPackageDetailJoinWithUserPackage, Long> {
    @Query(value = "SELECT user_package_detail.*, user_package.id AS child_id, user_package.group_id AS child_group_id, user_package.createat AS child_createat, user_package.package_second_id AS child_package_second_id, user_package.device_id AS child_device_id, user_package.product_second_id AS child_product_second_id, user_package.product_unit_second_id AS child_product_unit_second_id, user_package.number_unit AS child_number_unit, user_package.price AS child_price, user_package.buy_price AS child_buy_price, user_package.discount_amount AS child_discount_amount, user_package.discount_percent AS child_discount_percent, user_package.discount_promotional AS child_discount_promotional, user_package.note AS child_note, user_package.status AS child_status FROM (SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND (status = :status OR status = :or_status) ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)) AS user_package_detail LEFT JOIN (SELECT * FROM user_package WHERE user_package.group_id = :group_id) AS user_package ON user_package_detail.package_second_id = user_package.package_second_id")
    Flux<UserPackageDetailJoinWithUserPackage> getUserPackgeDetailAndPackageItem(@Param("group_id") String group_id, @Param("status") UserPackageDetail.Status status, @Param("or_status") UserPackageDetail.Status or_status, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT user_package_detail.*, user_package.id AS child_id, user_package.group_id AS child_group_id, user_package.createat AS child_createat, user_package.package_second_id AS child_package_second_id, user_package.device_id AS child_device_id, user_package.product_second_id AS child_product_second_id, user_package.product_unit_second_id AS child_product_unit_second_id, user_package.number_unit AS child_number_unit, user_package.price AS child_price, user_package.buy_price AS child_buy_price, user_package.discount_amount AS child_discount_amount, user_package.discount_percent AS child_discount_percent, user_package.discount_promotional AS child_discount_promotional, user_package.note AS child_note, user_package.status AS child_status FROM (SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND (status = :status OR status = :or_status) AND id < :id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)) AS user_package_detail LEFT JOIN (SELECT * FROM user_package WHERE user_package.group_id = :group_id) AS user_package ON user_package_detail.package_second_id = user_package.package_second_id")
    Flux<UserPackageDetailJoinWithUserPackage> getUserPackgeDetailAndPackageItemAferID(@Param("group_id") String group_id, @Param("status") UserPackageDetail.Status status, @Param("or_status") UserPackageDetail.Status or_status, @Param("id") int id, @Param("page") int page, @Param("size") int size);
}
