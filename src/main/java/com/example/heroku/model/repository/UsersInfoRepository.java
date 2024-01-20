package com.example.heroku.model.repository;

import com.example.heroku.model.UsersInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsersInfoRepository extends ReactiveCrudRepository<UsersInfo, Long> {
    //search all
    @Query(value = "SELECT * FROM users_info WHERE users_info.group_id = :group_id")// ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<UsersInfo> findByIdNotNull(@Param("group_id")String group_id, Pageable pageable);

    //should not edit this function because system using this function
    //@Query(value = "SELECT * FROM USERS WHERE Username = $1")//, nativeQuery = true)
    Mono<UsersInfo> findByUsername(String username);

    @Query(value = "DELETE FROM users_info WHERE users_info.username = :username")
    Mono<UsersInfo> deleteByUserName(@Param("username") String username);

}
