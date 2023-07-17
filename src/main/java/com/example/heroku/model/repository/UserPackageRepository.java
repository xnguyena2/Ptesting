package com.example.heroku.model.repository;

import com.example.heroku.model.UserPackage;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserPackageRepository extends ReactiveCrudRepository<UserPackage, String> {

    //"SELECT user_package.device_id, user_package.product_second_id, user_package.product_unit_second_id, SUM(user_package.number_unit) as number_unit, MAX(user_package.createat) as createat FROM user_package WHERE user_package.device_id = :id GROUP BY user_package.device_id, user_package.product_second_id, user_package.product_unit_second_id ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)"
    @Query(value = "SELECT user_package.device_id, user_package.product_second_id, user_package.product_unit_second_id, SUM(user_package.number_unit) AS number_unit, MAX(user_package.createat) AS createat FROM user_package WHERE user_package.device_id = :id GROUP BY user_package.device_id, user_package.product_second_id, user_package.product_unit_second_id ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<UserPackage> GetDevicePackage(@Param("id") String id, @Param("page") int page, @Param("size") int size);


    @Query(value = "DELETE FROM user_package WHERE device_id = :device_id AND product_unit_second_id = :beer_unit")
    Flux<UserPackage> DeleteProductByBeerUnit(@Param("device_id") String device_id, @Param("beer_unit") String beer_unit);

    @Query(value = "DELETE FROM user_package WHERE device_id = :id")
    Flux<UserPackage> DeleteProductByUserID(@Param("id") String id);

    @Query(value = "INSERT INTO user_package(device_id, product_second_id, product_unit_second_id, number_unit, status, createat) SELECT :device_id, :beer_id, :beer_unit, :number_unit, :status, NOW() WHERE EXISTS (SELECT * FROM product WHERE product_second_id=:beer_id) AND EXISTS (SELECT * FROM product_unit WHERE product_unit.product_second_id=:beer_id AND product_unit.product_unit_second_id=:beer_unit) AND NOT EXISTS (SELECT * FROM product WHERE product_second_id=:beer_id AND (status = 'SOLD_OUT' OR status = 'HIDE')) AND NOT EXISTS (SELECT * FROM product_unit WHERE product_unit.product_second_id=:beer_id AND product_unit.product_unit_second_id=:beer_unit AND (status = 'SOLD_OUT' OR status = 'HIDE'))")
    Mono<UserPackage> AddPackage(@Param("device_id") String device_id, @Param("beer_id") String beer_id, @Param("beer_unit") String beer_unit, @Param("number_unit") int number_unit, @Param("status") UserPackage.Status status);

}
