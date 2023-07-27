package com.example.heroku.model.repository;

import com.example.heroku.model.UserPackage;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserPackageRepository extends ReactiveCrudRepository<UserPackage, String> {

    //"SELECT user_package.device_id, user_package.product_second_id, user_package.product_unit_second_id, SUM(user_package.number_unit) as number_unit, MAX(user_package.createat) as createat FROM user_package WHERE user_package.device_id = :id GROUP BY user_package.device_id, user_package.product_second_id, user_package.product_unit_second_id ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)"
    @Query(value = "SELECT :group_id AS group_id, user_package.device_id, user_package.product_second_id, user_package.product_unit_second_id, SUM(user_package.number_unit) AS number_unit, MAX(user_package.createat) AS createat FROM user_package WHERE user_package.group_id = :group_id AND user_package.device_id = :device_id GROUP BY user_package.device_id, user_package.product_second_id, user_package.product_unit_second_id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<UserPackage> GetDevicePackage(@Param("group_id")String group_id, @Param("device_id") String device_id, @Param("page") int page, @Param("size") int size);


    @Query(value = "DELETE FROM user_package WHERE group_id = :group_id AND device_id = :device_id AND product_unit_second_id = :beer_unit")
    Flux<UserPackage> DeleteProductByBeerUnit(@Param("group_id")String groupID, @Param("device_id") String device_id, @Param("beer_unit") String beer_unit);

    @Query(value = "DELETE FROM user_package WHERE group_id = :group_id AND device_id = :id")
    Flux<UserPackage> DeleteProductByUserID(@Param("group_id")String groupID, @Param("id") String id);

    @Query(value = "INSERT INTO user_package( group_id, device_id, product_second_id, product_unit_second_id, number_unit, status, createat ) SELECT :group_id, :device_id, :beer_id, :beer_unit, :number_unit, :status, NOW() WHERE EXISTS ( SELECT * FROM product WHERE product.product_second_id = :beer_id AND product.status IS DISTINCT FROM 'SOLD_OUT' AND product.status IS DISTINCT FROM 'HIDE' ) AND EXISTS ( SELECT * FROM product_unit WHERE product_unit.product_second_id = :beer_id AND product_unit.product_unit_second_id = :beer_unit AND product_unit.status IS DISTINCT FROM 'SOLD_OUT' AND product_unit.status IS DISTINCT FROM 'HIDE' )")
    Mono<UserPackage> AddPackage(@Param("group_id")String groupID, @Param("device_id") String device_id, @Param("beer_id") String beer_id, @Param("beer_unit") String beer_unit, @Param("number_unit") int number_unit, @Param("status") UserPackage.Status status);

}
