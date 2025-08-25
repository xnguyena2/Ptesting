package com.example.heroku.model.repository;

import com.example.heroku.model.joinwith.ProductJoinWithProductUnit;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface JoinProductWithProductUnitRepository extends ReactiveCrudRepository<ProductJoinWithProductUnit, Long> {

    @Deprecated
    @Query(value = """
            SELECT
                p.*,
                pu.id AS child_id,
                pu.group_id AS child_group_id,
                pu.createat AS child_createat,
                pu.product_unit_second_id AS child_product_unit_second_id,
                pu.product_second_id AS child_product_second_id,
                pu.name AS child_name,
                pu.sku AS child_sku,
                pu.upc AS child_upc,
                pu.price AS child_price,
                pu.promotional_price AS child_promotional_price,
                pu.inventory_number AS child_inventory_number,
                pu.wholesale_number AS child_wholesale_number,
                pu.wholesale_price AS child_wholesale_price,
                pu.buy_price AS child_buy_price,
                pu.discount AS child_discount,
                pu.date_expire AS child_date_expire,
                pu.volumetric AS child_volumetric,
                pu.weight AS child_weight,
                pu.visible AS child_visible,
                pu.enable_warehouse AS child_enable_warehouse,
                pu.enable_serial AS child_enable_serial,
                pu.list_product_serial_id AS child_list_product_serial_id,
                pu.group_unit_id AS child_group_unit_id,
                pu.group_unit_naname AS child_group_unit_naname,
                pu.group_unit_number AS child_group_unit_number,
                pu.product_type AS child_product_type,
                pu.has_component AS child_has_component,
                pu.services_config AS child_services_config,
                pu.arg_action_id AS child_arg_action_id,
                pu.arg_action_type AS child_arg_action_type,
                pu.status AS child_status,
                i.imgid,
                i.tag,
                i.thumbnail,
                i.medium,
                i.large,
                i.category,
                i.id AS img_id,
                i.group_id AS img_group_id,
                i.createat AS img_createat
            FROM (
                SELECT *
                FROM product
                WHERE group_id = :group_id
                  AND status IS DISTINCT FROM 'HIDE'
                ORDER BY createat DESC
                LIMIT :size OFFSET (:page * :size)
            ) p
            LEFT JOIN product_unit pu
                ON p.product_second_id = pu.product_second_id
               AND pu.group_id = :group_id
            LEFT JOIN image i
                ON pu.product_unit_second_id = i.tag
               AND i.group_id = :group_id
            """)
    Flux<ProductJoinWithProductUnit> getIfProductNotHide(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size);

    @Query(value = """
            SELECT
                p.*,
                pu.id AS child_id,
                pu.group_id AS child_group_id,
                pu.createat AS child_createat,
                pu.product_unit_second_id AS child_product_unit_second_id,
                pu.product_second_id AS child_product_second_id,
                pu.name AS child_name,
                pu.sku AS child_sku,
                pu.upc AS child_upc,
                pu.price AS child_price,
                pu.promotional_price AS child_promotional_price,
                pu.inventory_number AS child_inventory_number,
                pu.wholesale_number AS child_wholesale_number,
                pu.wholesale_price AS child_wholesale_price,
                pu.buy_price AS child_buy_price,
                pu.discount AS child_discount,
                pu.date_expire AS child_date_expire,
                pu.volumetric AS child_volumetric,
                pu.weight AS child_weight,
                pu.visible AS child_visible,
                pu.enable_warehouse AS child_enable_warehouse,
                pu.enable_serial AS child_enable_serial,
                pu.list_product_serial_id AS child_list_product_serial_id,
                pu.group_unit_id AS child_group_unit_id,
                pu.group_unit_naname AS child_group_unit_naname,
                pu.group_unit_number AS child_group_unit_number,
                pu.product_type AS child_product_type,
                pu.has_component AS child_has_component,
                pu.services_config AS child_services_config,
                pu.arg_action_id AS child_arg_action_id,
                pu.arg_action_type AS child_arg_action_type,
                pu.status AS child_status,
                i.imgid,
                i.tag,
                i.thumbnail,
                i.medium,
                i.large,
                i.category,
                i.id AS img_id,
                i.group_id AS img_group_id,
                i.createat AS img_createat
            FROM product p
            LEFT JOIN product_unit pu
                   ON p.product_second_id = pu.product_second_id
                  AND pu.group_id = :group_id
            LEFT JOIN image i
                   ON pu.product_unit_second_id = i.tag
                  AND i.group_id = :group_id
            WHERE p.group_id = :group_id
              AND p.product_second_id = :product_second_id;
            
            """)
    Flux<ProductJoinWithProductUnit> getProductAndAllUnit(@Param("group_id")String groupID, @Param("product_second_id")String product_second_id);

    @Deprecated
    @Query(value = """
            SELECT
                product.*,
                product_unit.id AS child_id,
                product_unit.group_id AS child_group_id,
                product_unit.createat AS child_createat,
                product_unit.product_unit_second_id AS child_product_unit_second_id,
                product_unit.product_second_id AS child_product_second_id,
                product_unit.name AS child_name,
                product_unit.sku AS child_sku,
                product_unit.upc AS child_upc,
                product_unit.price AS child_price,
                product_unit.promotional_price AS child_promotional_price,
                product_unit.inventory_number AS child_inventory_number,
                product_unit.wholesale_number AS child_wholesale_number,
                product_unit.wholesale_price AS child_wholesale_price,
                product_unit.buy_price AS child_buy_price,
                product_unit.discount AS child_discount,
                product_unit.date_expire AS child_date_expire,
                product_unit.volumetric AS child_volumetric,
                product_unit.weight AS child_weight,
                product_unit.visible AS child_visible,
                product_unit.enable_warehouse AS child_enable_warehouse,
                product_unit.enable_serial AS child_enable_serial,
                product_unit.list_product_serial_id AS child_list_product_serial_id,
                product_unit.group_unit_id AS child_group_unit_id,
                product_unit.group_unit_naname AS child_group_unit_naname,
                product_unit.group_unit_number AS child_group_unit_number,
                product_unit.product_type AS child_product_type,
                product_unit.has_component AS child_has_component,
                product_unit.services_config AS child_services_config,
                product_unit.arg_action_id AS child_arg_action_id,
                product_unit.arg_action_type AS child_arg_action_type,
                product_unit.status AS child_status,
                image.imgid AS imgid,
                image.tag AS tag,
                image.thumbnail AS thumbnail,
                image.medium AS medium,
                image.large AS large,
                image.category AS category,
                image.id AS img_id,
                image.group_id AS img_group_id,
                image.createat AS img_createat
            FROM user_package
            LEFT JOIN product
                   ON product.group_id = user_package.group_id
                  AND product.product_second_id = user_package.product_second_id
            LEFT JOIN product_unit
                   ON product_unit.group_id = user_package.group_id
                  AND product_unit.product_second_id = user_package.product_second_id
                  AND product_unit.product_unit_second_id = user_package.product_unit_second_id
            LEFT JOIN image
                   ON image.group_id = user_package.group_id
                  AND image.tag = product_unit.product_unit_second_id
            WHERE user_package.group_id = :group_id
              AND user_package.package_second_id = :package_second_id;
            """)
    Flux<ProductJoinWithProductUnit> getProductAndUnitOfPackage(@Param("group_id")String groupID, @Param("package_second_id")String package_second_id);


    @Query(value = """
            SELECT
                p.*,
                pu.id AS child_id,
                pu.group_id AS child_group_id,
                pu.createat AS child_createat,
                pu.product_unit_second_id AS child_product_unit_second_id,
                pu.product_second_id AS child_product_second_id,
                pu.name AS child_name,
                pu.sku AS child_sku,
                pu.upc AS child_upc,
                pu.price AS child_price,
                pu.promotional_price AS child_promotional_price,
                pu.inventory_number AS child_inventory_number,
                pu.wholesale_number AS child_wholesale_number,
                pu.wholesale_price AS child_wholesale_price,
                pu.buy_price AS child_buy_price,
                pu.discount AS child_discount,
                pu.date_expire AS child_date_expire,
                pu.volumetric AS child_volumetric,
                pu.weight AS child_weight,
                pu.visible AS child_visible,
                pu.enable_warehouse AS child_enable_warehouse,
                pu.enable_serial AS child_enable_serial,
                pu.list_product_serial_id AS child_list_product_serial_id,
                pu.group_unit_id AS child_group_unit_id,
                pu.group_unit_naname AS child_group_unit_naname,
                pu.group_unit_number AS child_group_unit_number,
                pu.product_type AS child_product_type,
                pu.has_component AS child_has_component,
                pu.services_config AS child_services_config,
                pu.arg_action_id AS child_arg_action_id,
                pu.arg_action_type AS child_arg_action_type,
                pu.status AS child_status,
                i.imgid,
                i.tag,
                i.thumbnail,
                i.medium,
                i.large,
                i.category,
                i.id AS img_id,
                i.group_id AS img_group_id,
                i.createat AS img_createat
            FROM (
                SELECT *
                FROM product
                WHERE group_id = :group_id
                  AND status IS DISTINCT FROM 'HIDE'
                ORDER BY createat DESC
                LIMIT :size OFFSET (:page * :size)
            ) p
            LEFT JOIN product_unit pu
                ON p.product_second_id = pu.product_second_id
               AND pu.group_id = :group_id
            LEFT JOIN image i
                ON p.product_second_id = i.category
               AND i.group_id = :group_id;
            """)
    Flux<ProductJoinWithProductUnit> getIfProductNotHideAllImg(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size);

    @Query(value = """
            SELECT
                p.*,
                pu.id AS child_id,
                pu.group_id AS child_group_id,
                pu.createat AS child_createat,
                pu.product_unit_second_id AS child_product_unit_second_id,
                pu.product_second_id AS child_product_second_id,
                pu.name AS child_name,
                pu.sku AS child_sku,
                pu.upc AS child_upc,
                pu.price AS child_price,
                pu.promotional_price AS child_promotional_price,
                pu.inventory_number AS child_inventory_number,
                pu.wholesale_number AS child_wholesale_number,
                pu.wholesale_price AS child_wholesale_price,
                pu.buy_price AS child_buy_price,
                pu.discount AS child_discount,
                pu.date_expire AS child_date_expire,
                pu.volumetric AS child_volumetric,
                pu.weight AS child_weight,
                pu.visible AS child_visible,
                pu.enable_warehouse AS child_enable_warehouse,
                pu.enable_serial AS child_enable_serial,
                pu.list_product_serial_id AS child_list_product_serial_id,
                pu.group_unit_id AS child_group_unit_id,
                pu.group_unit_naname AS child_group_unit_naname,
                pu.group_unit_number AS child_group_unit_number,
                pu.product_type AS child_product_type,
                pu.has_component AS child_has_component,
                pu.services_config AS child_services_config,
                pu.arg_action_id AS child_arg_action_id,
                pu.arg_action_type AS child_arg_action_type,
                pu.status AS child_status,
                i.imgid,
                i.tag,
                i.thumbnail,
                i.medium,
                i.large,
                i.category,
                i.id AS img_id,
                i.group_id AS img_group_id,
                i.createat AS img_createat
            FROM (
                SELECT *
                FROM product
                WHERE group_id = :group_id
                  AND status IS DISTINCT FROM 'HIDE'
                  AND visible_web = TRUE
                ORDER BY createat DESC
                LIMIT :size OFFSET (:page * :size)
            ) p
            LEFT JOIN product_unit pu
                ON p.product_second_id = pu.product_second_id
               AND pu.group_id = :group_id
            LEFT JOIN image i
                ON p.product_second_id = i.category
               AND i.group_id = :group_id
            WHERE pu.inventory_number > 0;
            """)
    Flux<ProductJoinWithProductUnit> getIfProductNotHideAndForWebAllImg(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size);

    @Query(value = """
            SELECT
                p.*,
                pu.id AS child_id,
                pu.group_id AS child_group_id,
                pu.createat AS child_createat,
                pu.product_unit_second_id AS child_product_unit_second_id,
                pu.product_second_id AS child_product_second_id,
                pu.name AS child_name,
                pu.sku AS child_sku,
                pu.upc AS child_upc,
                pu.price AS child_price,
                pu.promotional_price AS child_promotional_price,
                pu.inventory_number AS child_inventory_number,
                pu.wholesale_number AS child_wholesale_number,
                pu.wholesale_price AS child_wholesale_price,
                pu.buy_price AS child_buy_price,
                pu.discount AS child_discount,
                pu.date_expire AS child_date_expire,
                pu.volumetric AS child_volumetric,
                pu.weight AS child_weight,
                pu.visible AS child_visible,
                pu.enable_warehouse AS child_enable_warehouse,
                pu.enable_serial AS child_enable_serial,
                pu.list_product_serial_id AS child_list_product_serial_id,
                pu.group_unit_id AS child_group_unit_id,
                pu.group_unit_naname AS child_group_unit_naname,
                pu.group_unit_number AS child_group_unit_number,
                pu.product_type AS child_product_type,
                pu.has_component AS child_has_component,
                pu.services_config AS child_services_config,
                pu.arg_action_id AS child_arg_action_id,
                pu.arg_action_type AS child_arg_action_type,
                pu.status AS child_status,
                i.imgid,
                i.tag,
                i.thumbnail,
                i.medium,
                i.large,
                i.category,
                i.id AS img_id,
                i.group_id AS img_group_id,
                i.createat AS img_createat
            
            FROM user_package up
            JOIN product p
                ON p.group_id = up.group_id
               AND p.product_second_id = up.product_second_id
            LEFT JOIN product_unit pu
                ON pu.group_id = up.group_id
               AND pu.product_second_id = up.product_second_id
               AND pu.product_unit_second_id = up.product_unit_second_id
            LEFT JOIN image i
                ON i.group_id = up.group_id
               AND i.category = p.product_second_id
            WHERE up.group_id = :group_id
              AND up.package_second_id = :package_second_id;
            """)
    Flux<ProductJoinWithProductUnit> getProductAndUnitOfPackageAllImg(@Param("group_id")String groupID, @Param("package_second_id")String package_second_id);
}
