package com.example.heroku.model.repository;

import com.example.heroku.model.GroupImport;
import com.example.heroku.model.ProductImport;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface GroupImportRepository extends ReactiveCrudRepository<GroupImport, Long> {


    @Query(value = "INSERT INTO group_import( group_id, group_import_second_id, supplier_id, supplier_name, supplier_phone, total_price, total_amount, payment, discount_amount, discount_percent, additional_fee, note, images, type, status, createat ) VALUES ( :group_id, :group_import_second_id, :supplier_id, :supplier_name, :supplier_phone, :total_price, :total_amount, :payment, :discount_amount, :discount_percent, :additional_fee, :note, :images, :type, :status, :createat ) ON CONFLICT (group_id, group_import_second_id) DO UPDATE SET supplier_id = :supplier_id, supplier_name = :supplier_name, supplier_phone = :supplier_phone, total_price = :total_price, total_amount = :total_amount, payment = :payment, discount_amount = :discount_amount, discount_percent = :discount_percent, additional_fee = :additional_fee, note = :note, images = :images, type = :type, status = :status, createat = :createat ")
    Mono<GroupImport> inertOrUpdate(@Param("group_id") String group_id, @Param("group_import_second_id") String group_import_second_id,
                                    @Param("supplier_id") String supplier_id, @Param("supplier_name") String supplier_name, @Param("supplier_phone") String supplier_phone,
                                    @Param("total_price") float total_price,
                                    @Param("total_amount") int total_amount, @Param("payment") float payment,
                                    @Param("discount_amount") float discount_amount, @Param("discount_percent") float discount_percent,
                                    @Param("additional_fee") float additional_fee, @Param("note") String note,
                                    @Param("images") String images, @Param("type") ProductImport.ImportType type,
                                    @Param("status") ProductImport.Status status, @Param("createat") Timestamp createat);

    @Query(value = "SELECT create_group_import_for_product_import(:group_id, :group_import_second_id, :type, :status)")
    Mono<GroupImport> createGroupImportForEmpty(@Param("group_id") String group_id, @Param("group_import_second_id") String groupImportID, @Param("type") String type, @Param("status") String status);

    @Query(value = "SELECT * FROM group_import WHERE group_import.group_id = :group_id AND group_import.group_import_second_id = :id")
    Mono<GroupImport> getByID(@Param("group_id") String group_id, @Param("id") String groupImportID);

    @Query(value = "SELECT * FROM group_import WHERE group_import.group_id = :group_id LIMIT :size OFFSET (:page * :size)")
    Flux<GroupImport> getALL(@Param("group_id") String group_id, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM group_import WHERE group_import.group_id = :group_id AND DATE_PART('day', NOW() - createat) <= :date LIMIT :size OFFSET (:page * :size)")
    Flux<GroupImport> getALLBeforeNoDate(@Param("group_id") String group_id, @Param("page") int page, @Param("size") int size, @Param("date") int date);

    @Query(value = "SELECT * FROM group_import WHERE group_import.group_id = :group_id AND (group_import.createat AT TIME ZONE '+07' BETWEEN :fromtime AND :totime) LIMIT :size OFFSET (:page * :size)")
    Flux<GroupImport> getALLBetween(@Param("group_id") String group_id, @Param("fromtime") Timestamp fromtime, @Param("totime") Timestamp totime, @Param("page") int page, @Param("size") int size);

    @Query(value = "DELETE FROM group_import WHERE group_import.group_id = :group_id AND group_import.group_import_second_id = :id")
    Mono<GroupImport> deleteByImportID(@Param("group_id") String group_id, @Param("id") String groupImportID);

    @Query(value = "UPDATE group_import SET status = :status WHERE group_import.group_id = :group_id AND group_import.group_import_second_id = :id")
    Mono<GroupImport> updateStatus(@Param("group_id") String group_id, @Param("id") String groupImportID, @Param("status") ProductImport.Status status);

}
