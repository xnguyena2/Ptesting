package com.example.heroku.model.repository;

import com.example.heroku.model.UserPackage;
import com.example.heroku.model.UserPackageDetail;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface UserPackageRepository extends ReactiveCrudRepository<UserPackage, Long> {

    //"SELECT user_package.device_id, user_package.product_second_id, user_package.product_unit_second_id, SUM(user_package.number_unit) as number_unit, MAX(user_package.createat) as createat FROM user_package WHERE user_package.device_id = :id GROUP BY user_package.device_id, user_package.product_second_id, user_package.product_unit_second_id ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)"
    @Query(value = "SELECT * FROM user_package WHERE user_package.group_id = :group_id AND user_package.device_id = :device_id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<UserPackage> GetDevicePackage(@Param("group_id") String group_id, @Param("device_id") String device_id, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM user_package WHERE user_package.group_id = :group_id AND user_package.status = :status AND user_package.product_second_id = :product_second_id AND user_package.product_unit_second_id = :product_unit_second_id AND (user_package.createat AT TIME ZONE '+07' BETWEEN :fromtime AND :totime) LIMIT :size OFFSET (:page * :size)")
    Flux<UserPackage> GetByStatusBetween(@Param("group_id") String group_id, @Param("product_second_id") String product_second_id, @Param("product_unit_second_id") String product_unit_second_id, @Param("fromtime") Timestamp from, @Param("totime") Timestamp to, @Param("status") UserPackageDetail.Status status, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM user_package WHERE user_package.group_id = :group_id AND user_package.status = :status AND user_package.product_second_id = :product_second_id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<UserPackage> GetByStatusOfProduct(@Param("group_id") String group_id, @Param("product_second_id") String product_second_id, @Param("status") UserPackageDetail.Status status, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM user_package WHERE user_package.group_id = :group_id AND user_package.status = :status AND user_package.product_second_id = :product_second_id AND id < :id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<UserPackage> GetByStatusOfProductAfterID(@Param("group_id") String group_id, @Param("product_second_id") String product_second_id, @Param("id") long id, @Param("status") UserPackageDetail.Status status, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM user_package WHERE user_package.group_id = :group_id AND user_package.package_second_id = :package_second_id")
    Flux<UserPackage> GetDevicePackageWithID(@Param("group_id") String group_id, @Param("package_second_id") String package_second_id);

    @Query(value = "DELETE FROM user_package WHERE group_id = :group_id AND device_id = :device_id AND product_unit_second_id = :product_unit_second_id AND status = :status")
    Flux<UserPackage> DeleteProductByBeerUnit(@Param("group_id") String groupID, @Param("device_id") String device_id, @Param("product_unit_second_id") String product_unit_second_id, @Param("status") UserPackageDetail.Status status);

    @Query(value = "DELETE FROM user_package WHERE group_id = :group_id AND device_id = :id AND status = :status")
    Flux<UserPackage> DeleteProductByUserID(@Param("group_id") String groupID, @Param("id") String id, @Param("status") UserPackageDetail.Status status);

    @Query(value = "DELETE FROM user_package WHERE group_id = :group_id AND package_second_id = :package_second_id")
    Flux<UserPackage> DeleteProductByPackageID(@Param("group_id") String groupID, @Param("package_second_id") String package_second_id);

    @Query(value = "INSERT INTO user_package(group_id, device_id, package_second_id, product_second_id, product_unit_second_id, product_name, product_unit_name, product_group_unit_name, number_services_unit, number_unit, status, createat) (SELECT :group_id, :device_id, :package_id, :product_second_id, :product_unit_second_id, :product_name, :product_unit_name, :product_group_unit_name, :number_services_unit, :number_unit, :status, NOW() WHERE EXISTS (SELECT * FROM product WHERE product.product_second_id = :product_second_id AND product.status IS DISTINCT FROM 'SOLD_OUT' AND product.status IS DISTINCT FROM 'HIDE' ) AND EXISTS (SELECT * FROM product_unit WHERE product_unit.product_second_id = :product_second_id AND product_unit.product_unit_second_id = :product_unit_second_id AND product_unit.status IS DISTINCT FROM 'SOLD_OUT' AND product_unit.status IS DISTINCT FROM 'HIDE' ) ) ON CONFLICT (group_id, package_second_id, product_second_id, product_unit_second_id) DO UPDATE SET number_unit = user_package.number_unit + :number_unit")
    Mono<UserPackage> BuyerHimSelfAddPackage(@Param("group_id") String groupID, @Param("device_id") String device_id, @Param("package_id") String package_id, @Param("product_second_id") String product_second_id, @Param("product_unit_second_id") String product_unit_second_id,
                                             @Param("product_name") String product_name, @Param("product_unit_name") String product_unit_name, @Param("product_group_unit_name") String product_group_unit_name,
                                             @Param("number_services_unit") float number_services_unit,
                                             @Param("number_unit") float number_unit, @Param("status") UserPackageDetail.Status status);

    @Query(value = "INSERT INTO user_package(group_id, device_id, package_second_id, product_second_id, product_unit_second_id, product_name, product_unit_name, product_group_unit_name, number_services_unit, number_unit, buy_price, price, discount_amount, discount_percent, discount_promotional, note, status, createat) (SELECT :group_id, :device_id, :package_second_id, :product_second_id, :product_unit_second_id, :product_name, :product_unit_name, :product_group_unit_name, :number_services_unit, :number_unit, :buy_price, :price, :discount_amount, :discount_percent, :discount_promotional, :note, :status, :createat WHERE EXISTS (SELECT * FROM product WHERE product.product_second_id = :product_second_id AND product.status IS DISTINCT FROM 'SOLD_OUT' AND product.status IS DISTINCT FROM 'HIDE' ) AND EXISTS (SELECT * FROM product_unit WHERE product_unit.product_second_id = :product_second_id AND product_unit.product_unit_second_id = :product_unit_second_id AND product_unit.status IS DISTINCT FROM 'SOLD_OUT' AND product_unit.status IS DISTINCT FROM 'HIDE' ) ) ON CONFLICT (group_id, package_second_id, product_second_id, product_unit_second_id) DO UPDATE SET number_unit = user_package.number_unit + :number_unit, buy_price = :buy_price, price = :price, discount_amount = :discount_amount, discount_percent = :discount_percent, discount_promotional = :discount_promotional, note = :note, product_name = :product_name, product_unit_name = :product_unit_name, product_group_unit_name = :product_group_unit_name, number_services_unit = :number_services_unit, status = :status")
    Mono<UserPackage> InsertOrUpdatePackage(@Param("group_id") String groupID, @Param("device_id") String device_id, @Param("package_second_id") String package_second_id,
                                            @Param("product_second_id") String product_second_id, @Param("product_unit_second_id") String product_unit_second_id,
                                            @Param("product_name") String product_name, @Param("product_unit_name") String product_unit_name, @Param("product_group_unit_name") String product_group_unit_name,
                                            @Param("number_services_unit") float number_services_unit, @Param("number_unit") float number_unit,
                                            @Param("buy_price") float buy_price, @Param("price") float price,
                                            @Param("discount_amount") float discount_amount, @Param("discount_percent") float discount_percent, @Param("discount_promotional") float discount_promotional,
                                            @Param("note") String note, @Param("status") UserPackageDetail.Status status, @Param("createat") Timestamp createat);

    @Query(value = "INSERT INTO user_package(group_id, device_id, package_second_id, product_second_id, product_unit_second_id, product_name, product_unit_name, product_group_unit_name, number_services_unit, number_unit, buy_price, price, discount_amount, discount_percent, discount_promotional, note, status, createat) VALUES (:group_id, :device_id, :package_second_id, :product_second_id, :product_unit_second_id, :product_name, :product_unit_name, :product_group_unit_name, :number_services_unit, :number_unit, :buy_price, :price, :discount_amount, :discount_percent, :discount_promotional, :note, :status, :createat) ON CONFLICT (group_id, package_second_id, product_second_id, product_unit_second_id) DO UPDATE SET number_unit = user_package.number_unit + :number_unit, buy_price = :buy_price, price = :price, discount_amount = :discount_amount, discount_percent = :discount_percent, discount_promotional = :discount_promotional, note = :note, product_name = :product_name, product_unit_name = :product_unit_name, product_group_unit_name = :product_group_unit_name, number_services_unit = :number_services_unit, status = :status")
    Mono<UserPackage> InsertOrUpdatePackageWithoutCheck(@Param("group_id") String groupID, @Param("device_id") String device_id, @Param("package_second_id") String package_second_id,
                                                        @Param("product_second_id") String product_second_id, @Param("product_unit_second_id") String product_unit_second_id,
                                                        @Param("product_name") String product_name, @Param("product_unit_name") String product_unit_name, @Param("product_group_unit_name") String product_group_unit_name,
                                                        @Param("number_services_unit") float number_services_unit, @Param("number_unit") float number_unit,
                                                        @Param("buy_price") float buy_price, @Param("price") float price, @Param("discount_amount") float discount_amount, @Param("discount_percent") float discount_percent, @Param("discount_promotional") float discount_promotional,
                                                        @Param("note") String note, @Param("status") UserPackageDetail.Status status, @Param("createat") Timestamp createat);

    @Query(value = "UPDATE user_package SET status = :status WHERE group_id = :group_id AND package_second_id = :package_second_id")
    Flux<UserPackage> UpdateStatusByPackgeID(@Param("group_id") String groupID, @Param("package_second_id") String package_second_id, @Param("status") UserPackageDetail.Status status);
}
