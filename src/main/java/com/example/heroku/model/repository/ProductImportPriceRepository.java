package com.example.heroku.model.repository;

import com.example.heroku.model.ProductImport;
import com.example.heroku.model.productprice.ProductPriceChange;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;


public interface ProductImportPriceRepository extends ReactiveCrudRepository<ProductPriceChange, Long> {
    @Query(value = """
            SELECT product_import.*,
                   product.name AS product_name,
                   product_unit.name AS product_unit_name
            FROM
              (SELECT product_second_id,
                      product_unit_second_id,
                      json_agg(json_build_object('price', price, 'amount', amount, 'createat', createat)) AS price_change
               FROM product_import
               WHERE group_id = :group_id AND type = :type AND (createat BETWEEN :fromtime AND :totime)
               GROUP BY product_second_id,
                        product_unit_second_id) AS product_import
            LEFT JOIN
              (SELECT *
               FROM product
               WHERE group_id = :group_id) AS product ON product_import.product_second_id = product.product_second_id
            LEFT JOIN
              (SELECT *
               FROM product_unit
               WHERE group_id = :group_id) AS product_unit ON product_import.product_second_id = product_unit.product_second_id
            AND product_import.product_unit_second_id = product_unit.product_unit_second_id
            """)
    Flux<ProductPriceChange> getPriceRangeOfType(@Param("group_id") String groupID, @Param("fromtime") LocalDateTime from, @Param("totime") LocalDateTime to, @Param("type") ProductImport.ImportType type);

}
