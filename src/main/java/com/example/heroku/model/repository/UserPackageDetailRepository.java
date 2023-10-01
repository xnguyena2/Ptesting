package com.example.heroku.model.repository;

import com.example.heroku.model.UserPackageDetail;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface UserPackageDetailRepository extends ReactiveCrudRepository<UserPackageDetail, Long> {
    @Query(value = "INSERT INTO user_package_detail(group_id, device_id, package_second_id, package_type, area_id, area_name, table_id, table_name, voucher, price, payment, discount_amount, discount_percent, ship_price, note, image, progress, status, createat) VALUES (:group_id, :device_id, :package_second_id, :package_type, :area_id, :area_name, :table_id, :table_name, :voucher, :price, :payment, :discount_amount, :discount_percent, :ship_price, :note, :image, :progress, :status, :createat) ON CONFLICT (group_id, device_id, package_second_id) DO UPDATE SET package_type = :package_type, area_id = :area_id, area_name = :area_name, table_id = :table_id, table_name = :table_name, voucher = :voucher, price = :price, payment = :payment, discount_amount = :discount_amount, discount_percent = :discount_percent, ship_price = :ship_price, note = :note, image = :image, progress = :progress, status = :status")
    Mono<UserPackageDetail> InsertOrUpdate(@Param("group_id")String group_id, @Param("device_id") String device_id, @Param("package_second_id") String package_id,
                                           @Param("package_type") String package_type, @Param("voucher") String voucher,
                                           @Param("area_id") String area_id, @Param("area_name") String area_name,
                                           @Param("table_id") String table_id, @Param("table_name") String table_name,
                                           @Param("price") float price, @Param("payment") float payment, @Param("discount_amount") float discount_amount,
                                           @Param("discount_percent") float discount_percent,
                                           @Param("ship_price") float ship_price, @Param("note") String note, @Param("image") String image,
                                           @Param("progress") String progress, @Param("status") UserPackageDetail.Status status, @Param("createat") Timestamp createat);
    @Query(value = "SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.device_id = :device_id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<UserPackageDetail> GetDevicePackageDetail(@Param("group_id") String group_id, @Param("device_id") String device_id, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<UserPackageDetail> GetAllPackageDetail(@Param("group_id") String group_id, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.device_id = :device_id AND user_package_detail.package_second_id = :package_second_id")
    Mono<UserPackageDetail> GetPackageDetail(@Param("group_id") String group_id, @Param("device_id") String device_id, @Param("package_second_id") String package_id);

    @Query(value = "SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.package_second_id = :package_second_id")
    Mono<UserPackageDetail> GetPackageDetailById(@Param("group_id") String group_id, @Param("package_second_id") String package_id);

    @Query(value = "DELETE FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.device_id = :device_id")
    Flux<UserPackageDetail> DeleteByUserID(@Param("group_id") String group_id, @Param("device_id") String device_id);

    @Query(value = "DELETE FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.device_id = :device_id AND user_package_detail.package_second_id = :package_second_id")
    Mono<UserPackageDetail> DeleteByID(@Param("group_id") String group_id, @Param("device_id") String device_id, @Param("package_second_id") String package_id);
}
