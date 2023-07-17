package com.example.heroku.model.repository;

import com.example.heroku.model.ProductUnitOrder;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BeerUnitOrderRepository extends ReactiveCrudRepository<ProductUnitOrder, String> {
    @Query(value = "SELECT * FROM product_unit_order WHERE product_unit_order.package_order_second_id = :package AND product_unit_order.product_second_id = :beer")
    Flux<ProductUnitOrder> findByBeerAndOrder(@Param("package")String packageID, @Param("beer")String beer);
}
