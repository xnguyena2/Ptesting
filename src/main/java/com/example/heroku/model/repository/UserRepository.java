package com.example.heroku.model.repository;

import com.example.heroku.model.Users;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<Users, String> {

    //@Query(value = "SELECT * FROM USERS WHERE Username = $1")//, nativeQuery = true)
    Mono<Users> findByUsername(String username);

}
