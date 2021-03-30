package com.example.heroku.model.repository;

import com.example.heroku.model.BeerOrder;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BeerOrderRepository extends ReactiveCrudRepository<BeerOrder, String> {
    @Query(value = "SELECT * FROM beer_order WHERE beer_order.package_order_second_id = :id")
    Flux<BeerOrder> findBySecondID(@Param("id")String packageID);
}
