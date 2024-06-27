package com.example.heroku.model.repository;

import com.example.heroku.model.ProductComboItem;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductComboItemRepository extends ReactiveCrudRepository<ProductComboItem, Long> {
    @Query(value = "SELECT * FROM product_combo_item WHERE product_combo_item.group_id = :group_id AND product_combo_item.product_second_id = :product_second_id")
    Flux<ProductComboItem> findByProductID(@Param("group_id") String groupID, @Param("product_second_id") String product_second_id);

    @Query(value = "DELETE FROM product_combo_item WHERE product_combo_item.group_id = :group_id AND product_combo_item.product_second_id = :product_second_id")
    Mono<ProductComboItem> deleteByProductID(@Param("group_id") String groupID, @Param("product_second_id") String product_second_id);
}
