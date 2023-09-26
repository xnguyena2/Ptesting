package com.example.heroku.model.repository;

import com.example.heroku.model.DeviceConfig;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface DeviceConfigRepository extends ReactiveCrudRepository<DeviceConfig, Long> {
    @Query(value = "SELECT * FROM device_config WHERE device_config.group_id = :group_id")
    Mono<DeviceConfig> getConfig(@Param("group_id")String group_id);

    @Query(value = "DELETE FROM device_config WHERE device_config.group_id = :group_id")
    Mono<DeviceConfig> deleteByGroupID(@Param("group_id")String group_id);
}
