package com.example.heroku.model.repository;

import com.example.heroku.model.UserDevice;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserDeviceRepository extends ReactiveCrudRepository<UserDevice, String> {

    @Query(value = "SELECT * FROM user_device WHERE user_device.device_id = :id")
    Mono<UserDevice> findByDeviceId(@Param("id")String id);

    @Query(value = "DELETE FROM user_device WHERE user_device.device_id = :id")
    Mono<UserDevice> deleteByDeviceId(@Param("id")String id);
}
