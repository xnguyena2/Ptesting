package com.example.heroku.model.repository;

import com.example.heroku.model.TableDetail;
import com.example.heroku.status.ActiveStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface TableDetailRepository extends ReactiveCrudRepository<TableDetail, Long> {

    @Query(value = "INSERT INTO table_detail(group_id, area_id, table_id, table_name, package_second_id, detail, status, createat) VALUES (:group_id, :area_id, :table_id, :table_name, :package_second_id, :detail, :status, :createat) ON CONFLICT (group_id, area_id, table_id) DO UPDATE SET table_name = :table_name, package_second_id = :package_second_id, detail = :detail, status = :status, createat = :createat")
    Mono<TableDetail> insertOrUpdate(@Param("group_id") String group_id, @Param("area_id") String area_id, @Param("table_id") String table_id,
                                     @Param("table_name") String table_name, @Param("package_second_id") String package_second_id,
                                     @Param("detail") String detail,
                                     @Param("status") ActiveStatus status, @Param("createat") Timestamp createat);
    //search all
    @Query(value = "SELECT * FROM table_detail WHERE table_detail.group_id = :group_id")// ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<TableDetail> findByIdNotNull(@Param("group_id")String group_id, Pageable pageable);

    @Query(value = "DELETE FROM table_detail WHERE table_detail.group_id = :group_id AND table_detail.area_id = :area_id")
    Mono<TableDetail> deleteByAreaId(@Param("group_id")String group_id, @Param("area_id") String area_id);

    @Query(value = "DELETE FROM table_detail WHERE table_detail.group_id = :group_id AND table_detail.table_id = :table_id")
    Mono<TableDetail> deleteById(@Param("group_id")String group_id, @Param("table_id") String table_id);

    @Query(value = "SELECT * FROM table_detail WHERE table_detail.group_id = :group_id AND table_detail.table_id = :table_id")
    Mono<TableDetail> getById(@Param("group_id")String group_id, @Param("table_id") String table_id);

    @Query(value = "UPDATE table_detail SET package_second_id = :package_second_id WHERE table_detail.group_id = :group_id AND table_detail.table_id = :table_id")
    Mono<TableDetail> setPackageID(@Param("group_id")String group_id, @Param("table_id") String table_id, @Param("package_second_id") String package_second_id);

    @Query(value = "SELECT * FROM table_detail WHERE table_detail.group_id = :group_id AND table_detail.area_id = :area_id")
    Flux<TableDetail> findByAreaId(@Param("group_id") String group_id, @Param("area_id") String area_id);

}
