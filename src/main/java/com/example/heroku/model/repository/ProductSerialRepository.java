package com.example.heroku.model.repository;

import com.example.heroku.model.ProductSerial;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ProductSerialRepository extends ReactiveCrudRepository<ProductSerial, Long> {

    @Query(value = "SELECT * FROM product_serial WHERE product_serial.group_id = :group_id")
    Flux<ProductSerial> getByGroupID(@Param("group_id")String group_id);

}
