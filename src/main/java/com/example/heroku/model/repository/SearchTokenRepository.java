package com.example.heroku.model.repository;

import com.example.heroku.model.SearchToken;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


public interface SearchTokenRepository extends ReactiveCrudRepository<SearchToken, Long> {
    @Query(value = "DELETE FROM search_token WHERE product_second_id = :id")
    Mono<SearchToken> deleteBySecondId(@Param("id")String id);

    @Query(value = "INSERT INTO search_token(group_id, product_second_id, tokens, createat) VALUES ( :group_id, :product_second_id, to_tsvector(:tokens), :createat ) ON CONFLICT ON CONSTRAINT UQ_search_token_product_second_id DO UPDATE SET tokens = to_tsvector(:tokens), createat = :createat")
    Mono<SearchToken> saveToken(@Param("group_id")String group_id, @Param("product_second_id")String product_second_id,
                                @Param("tokens")String tokens, @Param("createat") LocalDateTime createat);
}
