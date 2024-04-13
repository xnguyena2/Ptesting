package com.example.heroku.model.repository;

import com.example.heroku.model.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<Users, Long> {
    //search all
    @Query(value = "SELECT * FROM users WHERE users.group_id = :group_id")// ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Users> findByIdNotNull(@Param("group_id")String group_id, Pageable pageable);

    @Query(value = "SELECT * FROM users WHERE users.group_id = :group_id AND users.username = :username AND users.register_code = :register_code AND EXTRACT(EPOCH FROM (NOW() - createat)) < 180")// ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Mono<Users> getRegisteredAccountInTime(@Param("group_id")String group_id, @Param("username") String username, @Param("register_code")String register_code);

    //should not edit this function because system using this function
    //@Query(value = "SELECT * FROM USERS WHERE Username = $1")//, nativeQuery = true)
    Mono<Users> findByUsername(String username);

    @Query(value = "UPDATE users SET password=:newpass WHERE username = :username")//, nativeQuery = true)
    Mono<Users> updatePassword(@Param("username") String username, @Param("newpass") String newpass);

    @Query(value = "DELETE FROM users WHERE users.username = :username")
    Mono<Users> deleteByUserName(@Param("username") String username);

    @Query(value = "UPDATE users SET status=:status WHERE username = :username")
    Mono<Users> updateStatusByUserName(@Param("username") String username, @Param("status") Users.Status status);

    @Query(value = "select checkRole(:useradmin, :userstaff)")
    Mono<Boolean> isPermissionAllow(@Param("useradmin") String useradmin, @Param("userstaff") String userstaff);

}
