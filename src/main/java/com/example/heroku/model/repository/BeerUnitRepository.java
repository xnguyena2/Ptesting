package com.example.heroku.model.repository;

import com.example.heroku.model.ProductUnit;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerUnitRepository extends ReactiveCrudRepository<ProductUnit, String> {

    @Query(value = "DELETE FROM product_unit WHERE product_unit.beer = :id")
    Mono<ProductUnit> deleteByBeerId(@Param("id")String id);

    @Query(value = "SELECT * FROM product_unit WHERE product_unit.beer = :id")
    Flux<ProductUnit> findByBeerID(@Param("id")String id);

    @Query(value = "SELECT * FROM product_unit WHERE product_unit.beer_unit_second_id = :id")
    Mono<ProductUnit> findByBeerUnitID(@Param("id")String id);

    @Query(value = "SELECT * FROM product_unit WHERE product_unit.beer_unit_second_id = :id AND (product_unit.status IS NULL OR (product_unit.status != 'SOLD_OUT' AND product_unit.status != 'HIDE'))")
    Mono<ProductUnit> findByBeerUnitIDCanOrder(@Param("id")String id);
}
