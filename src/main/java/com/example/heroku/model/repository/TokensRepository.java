package com.example.heroku.model.repository;

import com.example.heroku.model.Tokens;
import com.example.heroku.status.ActiveStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TokensRepository extends ReactiveCrudRepository<Tokens, Long> {

    //search all
    @Query(value = "SELECT * FROM tokens WHERE tokens.group_id = :group_id ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Tokens> findByIdNotNull(@Param("group_id")String group_id, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM tokens WHERE tokens.token_second_id = :token_second_id")
    Mono<Tokens> getById(@Param("token_second_id")String token_second_id);

    @Query(value = "SELECT * FROM tokens WHERE tokens.group_id = :group_id AND tokens.username = :username")
    Flux<Tokens> getByUserID(@Param("group_id")String group_id, @Param("username")String username);

    @Query(value = "UPDATE tokens SET status = :status WHERE tokens.group_id = :group_id AND tokens.token_second_id = :token_second_id")
    Mono<Tokens> updateStatus(@Param("group_id")String group_id, @Param("token_second_id") String token_second_id, @Param("status") ActiveStatus status);

    @Query(value = "DELETE FROM tokens WHERE tokens.group_id = :group_id")
    Mono<Tokens> deleteByGroupID(@Param("group_id")String group_id);

    @Query(value = "DELETE FROM tokens WHERE tokens.group_id = :group_id AND tokens.token_second_id = :token_second_id")
    Mono<Tokens> deleteByID(@Param("group_id")String group_id, @Param("token_second_id")String token_second_id);

    @Query(value = "DELETE FROM tokens WHERE tokens.group_id = :group_id AND tokens.username = :username")
    Mono<Tokens> deleteByUserID(@Param("group_id")String group_id, @Param("username")String username);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM tokens WHERE token = :token AND status = :status)")
    Mono<Boolean> isTokenMatchStatus(@Param("token")String token, @Param("status") ActiveStatus status);

}
