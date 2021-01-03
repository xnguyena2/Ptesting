package com.example.heroku.model.repository;

import com.example.heroku.model.Beer;
import com.example.heroku.model.SearchToken;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SearchTokenRepository extends ReactiveCrudRepository<SearchToken, String> {
    @Query(value = "DELETE FROM search_token WHERE beer_second_id = :id")
    Mono<SearchToken> deleteBySecondId(@Param("id")String id);

    @Query(value = "INSERT INTO search_token(beer_second_id,tokens) VALUES (:id, to_tsvector(:tokens))")
    Mono<SearchToken> saveToken(@Param("id")String id, @Param("tokens")String tokens);
}
