package com.example.heroku.model.repository;

import com.example.heroku.model.ProductUnit;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

public interface BeerUnitRepository extends ReactiveCrudRepository<ProductUnit, Long> {

    @Query(value = "INSERT INTO product_unit( group_id, product_second_id, product_unit_second_id, name, sku, upc, price, wholesale_price, wholesale_number, promotional_price, inventory_number, buy_price, discount, date_expire, volumetric, weight, visible, enable_warehouse, arg_action_id, arg_action_type, status, createat ) VALUES ( :group_id, :product_second_id, :product_unit_second_id, :name, :sku, :upc, :price, :wholesale_price, :wholesale_number, :promotional_price, :inventory_number, :buy_price, :discount, :date_expire, :volumetric, :weight, :visible, :enable_warehouse, :arg_action_id, :arg_action_type, :status, :createat ) ON CONFLICT (group_id, product_second_id, product_unit_second_id) DO UPDATE SET name = :name, sku = :sku, upc = :upc, price = :price, wholesale_price = :wholesale_price, wholesale_number = :wholesale_number, promotional_price = :promotional_price, inventory_number = :inventory_number, buy_price = :buy_price, discount = :discount, date_expire = :date_expire, volumetric = :volumetric, weight = :weight, visible = :visible, enable_warehouse = :enable_warehouse, arg_action_id = :arg_action_id, arg_action_type = :arg_action_type, status = :status, createat = :createat")
    Mono<ProductUnit> saveProductUnit(@Param("group_id") String group_id, @Param("product_second_id") String product_second_id,
                                      @Param("product_unit_second_id") String product_unit_second_id, @Param("name") String name,
                                      @Param("sku") String sku, @Param("upc") String upc,
                                      @Param("price") float price, @Param("wholesale_price") float wholesale_price, @Param("wholesale_number") int wholesale_number,
                                      @Param("promotional_price") float promotional_price, @Param("inventory_number") int inventory_number,
                                      @Param("buy_price") float buy_price, @Param("discount") float discount,
                                      @Param("date_expire") Timestamp date_expire, @Param("volumetric") float volumetric,
                                      @Param("weight") float weight,
                                      @Param("visible") boolean visible, @Param("enable_warehouse") boolean enable_warehouse,
                                      @Param("arg_action_id") String arg_action_id, @Param("arg_action_type") String arg_action_type,
                                      @Param("status") ProductUnit.Status status,
                                      @Param("createat") Timestamp createat);
    @Query(value = "DELETE FROM product_unit WHERE product_unit.group_id = :group_id AND product_unit.product_second_id = :product_second_id")
    Mono<ProductUnit> deleteByBeerId(@Param("group_id")String groupID, @Param("product_second_id")String product_second_id);

    @Query(value = "UPDATE product_unit SET arg_action_id = :arg_action_id, arg_action_type = :arg_action_type WHERE product_unit.group_id = :group_id AND product_unit.product_second_id = :product_second_id")
    Flux<ProductUnit> setActionID(@Param("group_id")String groupID, @Param("product_second_id")String product_second_id, @Param("arg_action_id")String arg_action_id, @Param("arg_action_type")String arg_action_type);

    @Query(value = "UPDATE product_unit SET inventory_number = :inventory_number, enable_warehouse = :enable_warehouse, status = :status, arg_action_id = :arg_action_id, arg_action_type = :arg_action_type WHERE product_unit.group_id = :group_id AND product_unit.product_second_id = :product_second_id AND product_unit.product_unit_second_id = :product_unit_second_id")
    Mono<ProductUnit> updateInventoryAndEnableWarehouse(@Param("group_id")String groupID, @Param("product_second_id")String product_second_id, @Param("product_unit_second_id")String product_unit_second_id, @Param("inventory_number") int inventory_number, @Param("enable_warehouse") boolean enable_warehouse, @Param("status") ProductUnit.Status status,
                                                        @Param("arg_action_id")String arg_action_id, @Param("arg_action_type")String arg_action_type);

    @Query(value = "SELECT * FROM product_unit WHERE product_unit.group_id = :group_id AND product_unit.product_second_id = :product_second_id")
    Flux<ProductUnit> findByBeerID(@Param("group_id")String groupID, @Param("product_second_id")String product_second_id);

    @Query(value = "SELECT * FROM product_unit WHERE product_unit.group_id = :group_id AND product_unit.product_second_id = :product_second_id AND product_unit.product_unit_second_id = :product_unit_second_id")
    Mono<ProductUnit> findByBeerUnitID(@Param("group_id")String groupID, @Param("product_second_id")String product_second_id, @Param("product_unit_second_id")String product_unit_second_id);

    @Query(value = "SELECT * FROM product_unit WHERE product_unit.group_id = :group_id AND product_unit.product_unit_second_id = :id AND ( product_unit.status IS NULL OR ( product_unit.status != 'SOLD_OUT' AND product_unit.status != 'HIDE' ) )")
    Mono<ProductUnit> findByBeerUnitIDCanOrder(@Param("group_id")String groupID, @Param("id")String id);

    @Query(value = "SELECT * FROM product_unit WHERE product_unit.group_id = :group_id AND product_unit.product_second_id IN (:product_second_ids) AND product_unit.product_unit_second_id IN (:product_unit_second_ids) AND ( product_unit.status IS NULL OR ( product_unit.status != 'SOLD_OUT' AND product_unit.status != 'HIDE' ) )")
    Flux<ProductUnit> getListUnitByListIDs(@Param("group_id")String groupID, @Param("product_second_ids") List<String> product_second_ids, @Param("product_unit_second_ids") List<String> product_unit_second_ids);
}
