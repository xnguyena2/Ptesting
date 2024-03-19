package com.example.heroku.model.repository;

import com.example.heroku.model.joinwith.ProductJoinWithProductUnit;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface JoinProductWithProductUnitRepository extends ReactiveCrudRepository<ProductJoinWithProductUnit, Long> {
    @Query(value = "SELECT product.*, product_unit.id AS child_id, product_unit.group_id AS child_group_id, product_unit.createat AS child_createat, product_unit.product_unit_second_id AS child_product_unit_second_id, product_unit.product_second_id AS child_product_second_id, product_unit.name AS child_name, product_unit.sku AS child_sku, product_unit.upc AS child_upc, product_unit.price AS child_price, product_unit.promotional_price AS child_promotional_price, product_unit.inventory_number AS child_inventory_number, product_unit.wholesale_number AS child_wholesale_number, product_unit.wholesale_price AS child_wholesale_price, product_unit.buy_price AS child_buy_price, product_unit.discount AS child_discount, product_unit.date_expire AS child_date_expire, product_unit.volumetric AS child_volumetric, product_unit.weight AS child_weight, product_unit.visible AS child_visible, product_unit.enable_warehouse AS child_enable_warehouse, product_unit.status AS child_status FROM (SELECT * FROM product WHERE product.group_id = :group_id AND product.status IS DISTINCT FROM 'HIDE' ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)) AS product LEFT JOIN (SELECT * FROM product_unit WHERE product_unit.group_id = :group_id) AS product_unit ON product.product_second_id = product_unit.product_second_id")
    Flux<ProductJoinWithProductUnit> getIfProductNotHide(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.*, product_unit.id AS child_id, product_unit.group_id AS child_group_id, product_unit.createat AS child_createat, product_unit.product_unit_second_id AS child_product_unit_second_id, product_unit.product_second_id AS child_product_second_id, product_unit.name AS child_name, product_unit.sku AS child_sku, product_unit.upc AS child_upc, product_unit.price AS child_price, product_unit.promotional_price AS child_promotional_price, product_unit.inventory_number AS child_inventory_number, product_unit.wholesale_number AS child_wholesale_number, product_unit.wholesale_price AS child_wholesale_price, product_unit.buy_price AS child_buy_price, product_unit.discount AS child_discount, product_unit.date_expire AS child_date_expire, product_unit.volumetric AS child_volumetric, product_unit.weight AS child_weight, product_unit.visible AS child_visible, product_unit.enable_warehouse AS child_enable_warehouse, product_unit.status AS child_status FROM (SELECT * FROM product WHERE product.group_id = :group_id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)) AS product LEFT JOIN (SELECT * FROM product_unit WHERE product_unit.group_id = :group_id) AS product_unit ON product.product_second_id = product_unit.product_second_id")
    Flux<ProductJoinWithProductUnit> getAllProduct(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.*, product_unit.id AS child_id, product_unit.group_id AS child_group_id, product_unit.createat AS child_createat, product_unit.product_unit_second_id AS child_product_unit_second_id, product_unit.product_second_id AS child_product_second_id, product_unit.name AS child_name, product_unit.sku AS child_sku, product_unit.upc AS child_upc, product_unit.price AS child_price, product_unit.promotional_price AS child_promotional_price, product_unit.inventory_number AS child_inventory_number, product_unit.wholesale_number AS child_wholesale_number, product_unit.wholesale_price AS child_wholesale_price, product_unit.buy_price AS child_buy_price, product_unit.discount AS child_discount, product_unit.date_expire AS child_date_expire, product_unit.volumetric AS child_volumetric, product_unit.weight AS child_weight, product_unit.visible AS child_visible, product_unit.enable_warehouse AS child_enable_warehouse, product_unit.status AS child_status FROM (SELECT * FROM product WHERE product.group_id = :group_id AND product.status IS DISTINCT FROM 'HIDE' AND visible_web ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)) AS product LEFT JOIN (SELECT * FROM product_unit WHERE product_unit.group_id = :group_id) AS product_unit ON product.product_second_id = product_unit.product_second_id")
    Flux<ProductJoinWithProductUnit> getIfProductNotHideAndForWeb(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size);
}
