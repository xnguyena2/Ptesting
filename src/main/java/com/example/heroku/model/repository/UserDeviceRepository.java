package com.example.heroku.model.repository;

import com.example.heroku.model.UserDevice;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface UserDeviceRepository extends ReactiveCrudRepository<UserDevice, String> {

    @Query(value = "INSERT INTO user_device( group_id, device_id, user_first_name, user_last_name, status, createat ) VALUES( :group_id, :device_id, :user_first_name, :user_last_name, :status, :createat ) ON CONFLICT (group_id, device_id) DO UPDATE SET group_id = :group_id, device_id = :device_id, user_first_name = :user_first_name, user_last_name = :user_last_name, status = :status, createat = :createat")
    Mono<UserDevice> saveDevice(@Param("group_id")String groupID, @Param("device_id")String id,
                                    @Param("user_first_name")String user_first_name, @Param("user_last_name")String user_last_name,
                                    @Param("status")UserDevice.Status status, @Param("createat") Timestamp createat);

    @Query(value = "SELECT * FROM user_device WHERE user_device.group_id = :group_id AND user_device.device_id = :id")
    Mono<UserDevice> findByDeviceId(@Param("group_id")String groupID, @Param("id")String id);

    @Query(value = "DELETE FROM user_device WHERE user_device.group_id = :group_id AND user_device.device_id = :id")
    Mono<UserDevice> deleteByDeviceId(@Param("group_id")String groupID, @Param("id")String id);

    @Query(value = "SELECT * FROM user_device WHERE user_device.group_id = :group_id LIMIT :size OFFSET (:page * :size)")
    Flux<UserDevice> findAllByGroupID(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size);
}
