package com.example.heroku.model.repository;

import com.example.heroku.model.UserPackageDetail;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface UserPackageDetailRepository extends ReactiveCrudRepository<UserPackageDetail, Long> {
    @Query(value = "INSERT INTO user_package_detail(group_id, device_id, staff_id, package_second_id, package_type, area_id, area_name, table_id, table_name, voucher, price, payment, discount_amount, discount_percent, discount_promotional, discount_by_point, additional_fee, additional_config, ship_price, cost, profit, point, note, image, progress, status, createat) VALUES (:group_id, :device_id, :staff_id, :package_second_id, :package_type, :area_id, :area_name, :table_id, :table_name, :voucher, :price, :payment, :discount_amount, :discount_percent, :discount_promotional, :discount_by_point, :additional_fee, :additional_config, :ship_price, :cost, :profit, :point, :note, :image, :progress, :status, :createat) ON CONFLICT (group_id, package_second_id) DO UPDATE SET staff_id = :staff_id, package_type = :package_type, device_id = :device_id, area_id = :area_id, area_name = :area_name, table_id = :table_id, table_name = :table_name, voucher = :voucher, price = :price, payment = :payment, discount_amount = :discount_amount, discount_percent = :discount_percent, discount_promotional = :discount_promotional, discount_by_point = :discount_by_point, additional_fee = :additional_fee, additional_config = :additional_config, ship_price = :ship_price, cost = :cost, profit = :profit, POINT = :point, note = :note, image = :image, progress = :progress, status = :status")
    Mono<UserPackageDetail> InsertOrUpdate(@Param("group_id")String group_id, @Param("device_id") String device_id, @Param("staff_id") String staff_id,
                                           @Param("package_second_id") String package_id,
                                           @Param("package_type") String package_type, @Param("voucher") String voucher,
                                           @Param("area_id") String area_id, @Param("area_name") String area_name,
                                           @Param("table_id") String table_id, @Param("table_name") String table_name,
                                           @Param("price") float price, @Param("payment") float payment, @Param("discount_amount") float discount_amount,
                                           @Param("discount_percent") float discount_percent,
                                           @Param("discount_promotional") float discount_promotional, @Param("discount_by_point") float discount_by_point,
                                           @Param("additional_fee") float additional_fee, @Param("additional_config") String additional_config,
                                           @Param("ship_price") float ship_price, @Param("cost") float cost,
                                           @Param("profit") float profit, @Param("point") int point,
                                           @Param("note") String note, @Param("image") String image,
                                           @Param("progress") String progress, @Param("status") UserPackageDetail.Status status, @Param("createat") Timestamp createat);
    @Query(value = "SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.device_id = :device_id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<UserPackageDetail> GetDevicePackageDetail(@Param("group_id") String group_id, @Param("device_id") String device_id, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.device_id = :device_id AND user_package_detail.status = :status ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<UserPackageDetail> GetDevicePackageDetailByStatus(@Param("group_id") String group_id, @Param("device_id") String device_id, @Param("status") UserPackageDetail.Status status, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<UserPackageDetail> GetAllPackageDetail(@Param("group_id") String group_id, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND status = :status ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<UserPackageDetail> GetAllPackageDetailByStatus(@Param("group_id") String group_id, @Param("status") UserPackageDetail.Status status, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND status = :status AND id < :id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<UserPackageDetail> GetAllPackageDetailByStatusAfterID(@Param("group_id") String group_id, @Param("status") UserPackageDetail.Status status, @Param("id") int id, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND (status = :status OR status = :or_status ) ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<UserPackageDetail> GetAllPackageDetailByStatus(@Param("group_id") String group_id, @Param("status") UserPackageDetail.Status status, @Param("or_status") UserPackageDetail.Status or_status, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND (status = :status OR status = :or_status) AND id < :id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<UserPackageDetail> GetAllPackageDetailByStatusAfterID(@Param("group_id") String group_id, @Param("status") UserPackageDetail.Status status, @Param("or_status") UserPackageDetail.Status or_status, @Param("id") int id, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.device_id = :device_id AND user_package_detail.package_second_id = :package_second_id")
    Mono<UserPackageDetail> GetPackageDetail(@Param("group_id") String group_id, @Param("device_id") String device_id, @Param("package_second_id") String package_id);

    @Query(value = "SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.package_second_id = :package_second_id")
    Mono<UserPackageDetail> GetPackageDetailByID(@Param("group_id") String group_id, @Param("package_second_id") String package_id);

    @Query(value = "SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.package_second_id = :package_second_id")
    Mono<UserPackageDetail> GetPackageDetailById(@Param("group_id") String group_id, @Param("package_second_id") String package_id);

    @Query(value = "SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.package_second_id = :package_second_id AND user_package_detail.table_id = :table_id AND (user_package_detail.status IS NULL OR user_package_detail.status = :status) ORDER BY createat DESC LIMIT 1")
    Mono<UserPackageDetail> GetPackageDetailByIdOfStatus(@Param("group_id") String group_id, @Param("package_second_id") String package_id, @Param("table_id") String table_id, @Param("status") UserPackageDetail.Status status);

    @Query(value = "DELETE FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.device_id = :device_id AND user_package_detail.status = :status")
    Flux<UserPackageDetail> DeleteByUserID(@Param("group_id") String group_id, @Param("device_id") String device_id, @Param("status") UserPackageDetail.Status status);

    @Query(value = "DELETE FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.device_id = :device_id AND user_package_detail.package_second_id = :package_second_id")
    Mono<UserPackageDetail> DeleteByID(@Param("group_id") String group_id, @Param("device_id") String device_id, @Param("package_second_id") String package_id);

    @Query(value = "UPDATE user_package_detail SET status = :status WHERE user_package_detail.group_id = :group_id AND user_package_detail.package_second_id = :package_second_id")
    Mono<UserPackageDetail> UpdateStatusByID(@Param("group_id") String group_id, @Param("package_second_id") String package_id, @Param("status") UserPackageDetail.Status status);
}
