package com.example.heroku.model.repository;

import com.example.heroku.model.MapKeyValue;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface MapKeyValueRepository extends ReactiveCrudRepository<MapKeyValue, Long> {

    @Query(value = "INSERT INTO map_key_value( group_id, id_o, value_o, createat ) VALUES ( :group_id, :id_o, :value_o, :createat ) ON CONFLICT ON CONSTRAINT UQ_map_key_value DO UPDATE SET value_o = :value_o, createat = :createat")
    Mono<MapKeyValue> insert(@Param("group_id") String group_id, @Param("id_o") String id_o, @Param("value_o") String value_o, @Param("createat") Timestamp createat);

    @Query(value = "SELECT * FROM map_key_value WHERE group_id = :group_id AND id_o = :id_o")
    Mono<MapKeyValue> get(@Param("group_id") String group_id, @Param("id_o") String id_o);

    @Query(value = "DELETE FROM map_key_value WHERE group_id = :group_id AND id_o = :id_o")
    Mono<MapKeyValue> deleteByID(@Param("group_id") String group_id, @Param("id_o") String id_o);

    @Query(value = "SELECT * FROM map_key_value WHERE group_id = :group_id AND id_o LIKE :id_o")
    Flux<MapKeyValue> search(@Param("group_id") String group_id, @Param("id_o") String id_o);

}
