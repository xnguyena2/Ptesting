package com.example.heroku.model.repository;

import com.example.heroku.model.UserFCM;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


public interface UserFCMRepository  extends ReactiveCrudRepository<UserFCM, Long> {
    @Query(value = "SELECT * FROM user_fcm WHERE user_fcm.group_id = :group_id")
    Flux<UserFCM> findByGroupId(@Param("group_id")String groupID);

    @Query(value = "SELECT * FROM user_fcm WHERE user_fcm.group_id = :group_id AND user_fcm.device_id = :id")
    Flux<UserFCM> findByDeviceId(@Param("group_id")String groupID, @Param("id") String id);

    @Query(value = "INSERT INTO user_fcm(group_id, device_id, fcm_id, status, createat) VALUES (:group_id, :device_id, :fcm_id, :status, :createat) ON CONFLICT ON CONSTRAINT UQ_user_fcm_device_id DO UPDATE SET fcm_id=:fcm_id, status=:status, createat=:createat")
    Mono<UserFCM> saveToken(@Param("group_id")String groupID, @Param("device_id") String id, @Param("fcm_id") String tokens, @Param("status") UserFCM.Status status, @Param("createat") LocalDateTime createat);

    @Query(value = "DELETE FROM user_fcm WHERE user_fcm.group_id = :group_id AND user_fcm.device_id = :id")
    Mono<UserFCM> deleteByDeviceId(@Param("group_id")String groupID, @Param("id")String id);
}
