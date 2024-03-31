package com.example.heroku.model.repository;

import com.example.heroku.model.ProductImport;
import com.example.heroku.model.joinwith.GroupImportJoinProductImport;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface JoinGroupImportWithProductImportRepository extends ReactiveCrudRepository<GroupImportJoinProductImport, Long> {
    @Query(value = "SELECT group_import.*, product_import.id AS child_id, product_import.group_id AS child_group_id, product_import.createat AS child_createat, product_import.group_import_second_id AS child_group_import_second_id, product_import.product_import_second_id AS child_product_import_second_id, product_import.product_second_id AS child_product_second_id, product_import.product_unit_second_id AS child_product_unit_second_id, product_import.product_unit_name_category AS child_product_unit_name_category, product_import.price AS child_price, product_import.amount AS child_amount, product_import.note AS child_note, product_import.type AS child_type, product_import.status AS child_status FROM (SELECT * FROM group_import WHERE group_import.group_id = :group_id AND group_import.status IS DISTINCT FROM :not_status ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)) AS group_import LEFT JOIN (SELECT * FROM product_import WHERE product_import.group_id = :group_id) AS product_import ON group_import.group_import_second_id = product_import.group_import_second_id")
    Flux<GroupImportJoinProductImport> getGroupIfNotStatus(@Param("group_id")String groupID, @Param("not_status") ProductImport.Status not_status, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT group_import.*, product_import.id AS child_id, product_import.group_id AS child_group_id, product_import.createat AS child_createat, product_import.group_import_second_id AS child_group_import_second_id, product_import.product_import_second_id AS child_product_import_second_id, product_import.product_second_id AS child_product_second_id, product_import.product_unit_second_id AS child_product_unit_second_id, product_import.product_unit_name_category AS child_product_unit_name_category, product_import.price AS child_price, product_import.amount AS child_amount, product_import.note AS child_note, product_import.type AS child_type, product_import.status AS child_status FROM (SELECT * FROM group_import WHERE group_import.group_id = :group_id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)) AS group_import LEFT JOIN (SELECT * FROM product_import WHERE product_import.group_id = :group_id) AS product_import ON group_import.group_import_second_id = product_import.group_import_second_id")
    Flux<GroupImportJoinProductImport> getAllProduct(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size);
}
