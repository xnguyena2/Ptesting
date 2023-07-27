package com.example.heroku.model.repository;

import com.example.heroku.model.UserFCM;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface UserFCMRepository  extends ReactiveCrudRepository<UserFCM, String> {
    @Query(value = "SELECT * FROM user_fcm WHERE user_fcm.group_id = :group_id AND user_fcm.device_id = :id")
    Mono<UserFCM> findByDeviceId(@Param("group_id")String groupID, @Param("id") String id);

    @Query(value = "INSERT INTO user_fcm(group_id, device_id, fcm_id, status, createat) VALUES (:group_id, :device_id, :fcm_id, :status, :createat) ON CONFLICT (device_id) DO UPDATE SET fcm_id=:fcm_id, status=:status, createat=:createat")
    Mono<UserFCM> saveToken(@Param("group_id")String groupID, @Param("device_id") String id, @Param("fcm_id") String tokens, @Param("status") UserFCM.Status status, @Param("createat") Timestamp createat);

    @Query(value = "DELETE FROM user_fcm WHERE user_fcm.group_id = :group_id AND user_fcm.device_id = :id")
    Mono<UserFCM> deleteByDeviceId(@Param("group_id")String groupID, @Param("id")String id);
}
