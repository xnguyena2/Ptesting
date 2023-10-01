package com.example.heroku.model.repository;

import com.example.heroku.model.Area;
import com.example.heroku.status.ActiveStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface AreaRepository extends ReactiveCrudRepository<Area, Long> {

    @Query(value = "INSERT INTO area(group_id, area_id, area_name, detail, meta_search, status, createat) VALUES (:group_id, :area_id, :area_name, :detail, :meta_search, :status, :createat) ON CONFLICT (group_id, area_id) DO UPDATE SET area_name = :area_name, detail = :detail, meta_search = :meta_search, status = :status, createat = :createat")
    Mono<Area> insertOrUpdate(@Param("group_id") String group_id, @Param("area_id") String area_id, @Param("area_name") String area_name,
                               @Param("detail") String detail, @Param("meta_search") String meta_search,
                               @Param("status") ActiveStatus status, @Param("createat") Timestamp createat);
    //search all
    @Query(value = "SELECT * FROM area WHERE area.group_id = :group_id")// ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Area> findByIdNotNull(@Param("group_id")String group_id, Pageable pageable);

    @Query(value = "UPDATE area SET area_name = :area_name WHERE area.group_id = :group_id AND area.area_id = :area_id")
    Mono<Area> updatename(@Param("group_id")String group_id, @Param("area_id") String area_id, @Param("area_name") String area_name);

    @Query(value = "DELETE FROM area WHERE area.group_id = :group_id AND area.area_id = :area_id")
    Mono<Area> deleteById(@Param("group_id")String group_id, @Param("area_id") String area_id);

    @Query(value = "SELECT * FROM area WHERE area.group_id = :group_id AND area.area_id = :area_id")
    Mono<Area> getById(@Param("group_id")String group_id, @Param("area_id") String area_id);

    @Query(value = "SELECT * FROM area WHERE area.group_id = :group_id AND meta_search LIKE :meta_search")
    Flux<Area> findByName(@Param("group_id") String group_id, @Param("meta_search") String meta_search, Pageable pageable);

}
