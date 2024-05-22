package com.example.heroku.model.repository;

import com.example.heroku.model.joinwith.UserJoinUserInfo;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface JoinUsersWithUsersInfoRepository extends ReactiveCrudRepository<UserJoinUserInfo, Long> {
    @Query(value = "SELECT users.*, users_info.username AS child_username, users_info.user_fullname AS user_fullname, users_info.phone AS phone, users_info.title AS title, users_info.roles AS client_roles, users_info.createat AS child_createat FROM (SELECT * FROM users WHERE users.group_id = :group_id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)) AS users LEFT JOIN (SELECT * FROM users_info WHERE users_info.group_id = :group_id) AS users_info ON users.username = users_info.username")
    Flux<UserJoinUserInfo> getAllUserFullInfo(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT users.*, users_info.username AS child_username, users_info.user_fullname AS user_fullname, users_info.phone AS phone, users_info.title AS title, users_info.roles AS client_roles, users_info.createat AS child_createat FROM (SELECT * FROM users WHERE users.username = :username) AS users LEFT JOIN users_info ON users.username = users_info.username")
    Mono<UserJoinUserInfo> findByUserName(@Param("username")String username);

}
