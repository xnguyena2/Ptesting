package com.example.heroku.model.repository;

import com.example.heroku.model.BeerUnitOrder;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BeerUnitOrderRepository extends ReactiveCrudRepository<BeerUnitOrder, String> {
    @Query(value = "SELECT * FROM beer_unit_order WHERE beer_unit_order.package_order_second_id = :package AND beer_unit_order.beer_second_id = :beer")
    Flux<BeerUnitOrder> findByBeerAndOrder(@Param("package")String packageID, @Param("beer")String beer);
}
