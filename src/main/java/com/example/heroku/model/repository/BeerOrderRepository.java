package com.example.heroku.model.repository;

import com.example.heroku.model.ProductOrder;
import com.example.heroku.response.ProductOrderStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BeerOrderRepository extends ReactiveCrudRepository<ProductOrder, String> {
    @Query(value = "SELECT * FROM product_order WHERE product_order.package_order_second_id = :id AND product_order.group_id = :group_id")
    Flux<ProductOrder> findBySecondID(@Param("group_id")String groupID, @Param("id")String packageID);

    @Query(value = "SELECT TEMP.*, package_order.status FROM ( SELECT * FROM product_order WHERE product_order.product_second_id = :id AND product_order.group_id = :group_id AND DATE_PART('day', NOW() - createat) <= :date LIMIT :size OFFSET (:page * :size) ) TEMP LEFT JOIN package_order ON package_order.package_order_second_id = TEMP.package_order_second_id")
    Flux<ProductOrderStatus> getAllByProductID(@Param("group_id")String groupID, @Param("id")String productID, @Param("page")int page, @Param("size")int size, @Param("date")int date);

    @Query(value = "SELECT TEMP.*, package_order.status FROM ( SELECT * FROM product_order WHERE product_order.group_id = :group_id AND DATE_PART('day', NOW() - createat) <= :date LIMIT :size OFFSET (:page * :size) ) TEMP LEFT JOIN package_order ON package_order.package_order_second_id = TEMP.package_order_second_id")
    Flux<ProductOrderStatus> getALL(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size, @Param("date")int date);
}
