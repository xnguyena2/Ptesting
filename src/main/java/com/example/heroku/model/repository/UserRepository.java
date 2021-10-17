package com.example.heroku.model.repository;

import com.example.heroku.model.Users;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<Users, String> {

    //@Query(value = "SELECT * FROM USERS WHERE Username = $1")//, nativeQuery = true)
    Mono<Users> findByUsername(String username);

    @Query(value = "UPDATE users SET password=:newpass WHERE username = :username")//, nativeQuery = true)
    Mono<Users> updatePassword(@Param("username") String username, @Param("newpass") String newpass);

    @Query(value = "DELETE FROM users WHERE users.username = :username")
    Mono<Users> deleteByUserNameAndPassword(@Param("username") String username);

}
