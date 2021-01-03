package com.example.heroku.model.repository;

import com.example.heroku.model.Beer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerRepository extends ReactiveCrudRepository<Beer, String> {

    @Query(value = "SELECT * FROM beer WHERE beer.beer_second_id = :id")
    Mono<Beer> findBySecondID(@Param("id")String id);

    @Query(value = "DELETE FROM beer WHERE beer.beer_second_id = :id")
    Mono<Beer> deleteBySecondId(@Param("id")String id);

    @Query(value = "SELECT * FROM beer ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> findAll(@Param("page")int page, @Param("size")int size);

    Flux<Beer> findByCategory(Beer.Category category, Pageable pageable);

    @Query(value = "SELECT * FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search)")
    Flux<Beer> searchBeer(@Param("search")String search);

    @Query(value = "SELECT * FROM beer WHERE beer.meta_search LIKE :search")
    Flux<Beer> searchBeerLike(@Param("search")String search);
}
