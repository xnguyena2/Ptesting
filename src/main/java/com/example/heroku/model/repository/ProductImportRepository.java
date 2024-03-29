package com.example.heroku.model.repository;

import com.example.heroku.model.ProductImport;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductImportRepository extends ReactiveCrudRepository<ProductImport, Long> {

    @Query(value = "SELECT * FROM product_import WHERE product_import.group_id = :group_id AND DATE_PART('day', NOW() - createat) <= :date LIMIT :size OFFSET (:page * :size)")
    Flux<ProductImport> getALL(@Param("group_id")String group_id, @Param("page")int page, @Param("size")int size, @Param("date")int date);

    @Query(value = "SELECT * FROM product_import WHERE product_import.group_id = :group_id AND product_import.product_id = :id AND DATE_PART('day', NOW() - createat) <= :date LIMIT :size OFFSET (:page*:size)")
    Flux<ProductImport> getAllByProductID(@Param("group_id")String group_id, @Param("id")String productID, @Param("page")int page, @Param("size")int size, @Param("date")int date);

    @Query(value = "DELETE FROM product_import WHERE product_import.group_id = :group_id AND product_import.product_import_second_id = :id")
    Mono<ProductImport> deleteByImportID(@Param("group_id")String group_id, @Param("id")String productID);

}
