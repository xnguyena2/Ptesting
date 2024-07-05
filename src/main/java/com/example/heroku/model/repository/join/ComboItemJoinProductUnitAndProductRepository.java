package com.example.heroku.model.repository.join;

import com.example.heroku.model.joinwith.ComboItemJoinProductUnitAndProduct;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ComboItemJoinProductUnitAndProductRepository extends ReactiveCrudRepository<ComboItemJoinProductUnitAndProduct, Long> {
    @Query(value = "SELECT product_unit.*, product.name AS product_name, product_combo_item.unit_number AS unit_number FROM (SELECT * FROM product_combo_item WHERE group_id = :group_id AND product_second_id = :product_second_id) AS product_combo_item INNER JOIN (SELECT * FROM product_unit WHERE group_id = :group_id) AS product_unit ON product_combo_item.item_product_second_id = product_unit.product_second_id AND product_combo_item.item_product_unit_second_id = product_unit.product_unit_second_id INNER JOIN (SELECT * FROM product WHERE group_id = :group_id) AS product ON product_combo_item.item_product_second_id = product.product_second_id")
    Flux<ComboItemJoinProductUnitAndProduct> findByProductID(@Param("group_id")String groupID, @Param("product_second_id") String product_second_id);

}
