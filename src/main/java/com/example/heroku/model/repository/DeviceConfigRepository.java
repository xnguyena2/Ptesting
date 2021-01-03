package com.example.heroku.model.repository;

import com.example.heroku.model.DeviceConfig;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface DeviceConfigRepository extends ReactiveCrudRepository<DeviceConfig, String> {
    @Query(value = "SELECT * FROM device_config")
    Mono<DeviceConfig> getConfig();
}
