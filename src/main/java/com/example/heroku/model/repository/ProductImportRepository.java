package com.example.heroku.model.repository;

import com.example.heroku.model.ProductImport;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface ProductImportRepository extends ReactiveCrudRepository<ProductImport, Long> {


    @Query(value = "INSERT INTO product_import ( group_id, group_import_second_id, product_second_id, product_unit_second_id, product_unit_name_category, price, amount, note, type, status, createat ) VALUES ( :group_id, :group_import_second_id, :product_second_id, :product_unit_second_id, :product_unit_name_category, :price, :amount, :note, :type, :status, :createat ) ON CONFLICT ON CONSTRAINT UQ_product_import_second_id DO UPDATE SET product_unit_name_category = :product_unit_name_category, price = :price, amount = :amount, note = :note, type = :type, status = :status, createat = :createat ")
    Mono<ProductImport> inertOrUpdate(@Param("group_id") String group_id, @Param("group_import_second_id") String group_import_second_id,
                                      @Param("product_second_id") String product_second_id, @Param("product_unit_second_id") String product_unit_second_id,
                                      @Param("product_unit_name_category") String product_unit_name_category,
                                      @Param("price") float price, @Param("amount") float amount,
                                      @Param("note") String note, @Param("type") ProductImport.ImportType type,
                                      @Param("status") ProductImport.Status status, @Param("createat") Timestamp createat);

    @Query(value = "SELECT * FROM product_import WHERE product_import.group_id = :group_id LIMIT :size OFFSET (:page * :size)")
    Flux<ProductImport> getALL(@Param("group_id") String group_id, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM product_import WHERE product_import.group_id = :group_id AND DATE_PART('day', NOW() - createat) <= :date LIMIT :size OFFSET (:page * :size)")
    Flux<ProductImport> getALLBeforeNoDate(@Param("group_id") String group_id, @Param("page") int page, @Param("size") int size, @Param("date") int date);

    @Query(value = "SELECT * FROM product_import WHERE product_import.group_id = :group_id AND (product_import.createat AT TIME ZONE '+07' BETWEEN :fromtime AND :totime) LIMIT :size OFFSET (:page * :size)")
    Flux<ProductImport> getALLBetween(@Param("group_id") String group_id, @Param("fromtime") Timestamp fromtime, @Param("totime") Timestamp totime, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM product_import WHERE product_import.group_id = :group_id AND product_import.product_id = :id AND DATE_PART('day', NOW() - createat) <= :date LIMIT :size OFFSET (:page*:size)")
    Flux<ProductImport> getAllByProductID(@Param("group_id") String group_id, @Param("id") String productID, @Param("page") int page, @Param("size") int size, @Param("date") int date);

    @Query(value = "DELETE FROM product_import WHERE product_import.group_id = :group_id AND product_import.group_import_second_id = :id")
    Flux<ProductImport> deleteByGroupID(@Param("group_id") String group_id, @Param("id") String groupImportID);

    @Query(value = "UPDATE product_import SET status = :status WHERE product_import.group_id = :group_id AND product_import.group_import_second_id = :id")
    Flux<ProductImport> changeStatus(@Param("group_id") String group_id, @Param("id") String groupImportID, @Param("status") ProductImport.Status status);

}
