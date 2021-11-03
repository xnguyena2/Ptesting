package com.example.heroku.model.repository;

import com.example.heroku.model.BeerOrder;
import com.example.heroku.response.BeerOrderStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BeerOrderRepository extends ReactiveCrudRepository<BeerOrder, String> {
    @Query(value = "SELECT * FROM beer_order WHERE beer_order.package_order_second_id = :id")
    Flux<BeerOrder> findBySecondID(@Param("id")String packageID);

    @Query(value = "SELECT TEMP.*, package_order.status FROM (SELECT * FROM beer_order WHERE beer_order.beer_second_id = :id AND DATE_PART('day', NOW() - createat) <= :date LIMIT :size OFFSET (:page*:size)) TEMP LEFT JOIN package_order ON package_order.package_order_second_id = TEMP.package_order_second_id")
    Flux<BeerOrderStatus> getAllByProductID(@Param("id")String productID, @Param("page")int page, @Param("size")int size, @Param("date")int date);

    @Query(value = "SELECT TEMP.*, package_order.status FROM (SELECT * FROM beer_order WHERE DATE_PART('day', NOW() - createat) <= :date LIMIT :size OFFSET (:page*:size)) TEMP LEFT JOIN package_order ON package_order.package_order_second_id = TEMP.package_order_second_id")
    Flux<BeerOrderStatus> getALL(@Param("page")int page, @Param("size")int size, @Param("date")int date);
}
