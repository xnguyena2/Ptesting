package com.example.heroku.model.repository;

import com.example.heroku.model.ProductUnit;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface BeerUnitRepository extends ReactiveCrudRepository<ProductUnit, Long> {

    @Query(value = "INSERT INTO product_unit( group_id, product_second_id, product_unit_second_id, name, price, buy_price, discount, date_expire, volumetric, weight, status, createat ) VALUES ( :group_id, :product_second_id, :product_unit_second_id, :name, :price, :buy_price, :discount, :date_expire, :volumetric, :weight, :status, :createat ) ON CONFLICT (group_id, product_second_id, product_unit_second_id) DO UPDATE SET name = :name, price = :price, buy_price = :buy_price, discount = :discount, date_expire = :date_expire, volumetric = :volumetric, weight = :weight, status = :status, createat = :createat")
    Mono<ProductUnit> saveProductUnit(@Param("group_id") String group_id, @Param("product_second_id") String product_second_id,
                                      @Param("product_unit_second_id") String product_unit_second_id, @Param("name") String name,
                                      @Param("price") float price, @Param("buy_price") float buy_price, @Param("discount") float discount,
                                      @Param("date_expire") Timestamp date_expire, @Param("volumetric") float volumetric,
                                      @Param("weight") float weight, @Param("status") ProductUnit.Status status,
                                      @Param("createat") Timestamp createat);
    @Query(value = "DELETE FROM product_unit WHERE product_unit.group_id = :group_id AND product_unit.product_second_id = :product_second_id")
    Mono<ProductUnit> deleteByBeerId(@Param("group_id")String groupID, @Param("product_second_id")String product_second_id);

    @Query(value = "DELETE FROM product_unit WHERE product_unit.group_id = :group_id")
    Mono<ProductUnit> deleteByBeerByGroupId(@Param("group_id")String groupID);

    @Query(value = "SELECT * FROM product_unit WHERE product_unit.group_id = :group_id AND product_unit.product_second_id = :product_second_id")
    Flux<ProductUnit> findByBeerID(@Param("group_id")String groupID, @Param("product_second_id")String product_second_id);

    @Query(value = "SELECT * FROM product_unit WHERE product_unit.group_id = :group_id AND product_unit.product_second_id = :product_second_id AND product_unit.product_unit_second_id = :product_unit_second_id")
    Mono<ProductUnit> findByBeerUnitID(@Param("group_id")String groupID, @Param("product_second_id")String product_second_id, @Param("product_unit_second_id")String product_unit_second_id);

    @Query(value = "SELECT * FROM product_unit WHERE product_unit.group_id = :group_id AND product_unit.product_unit_second_id = :id AND ( product_unit.status IS NULL OR ( product_unit.status != 'SOLD_OUT' AND product_unit.status != 'HIDE' ) )")
    Mono<ProductUnit> findByBeerUnitIDCanOrder(@Param("group_id")String groupID, @Param("id")String id);
}
