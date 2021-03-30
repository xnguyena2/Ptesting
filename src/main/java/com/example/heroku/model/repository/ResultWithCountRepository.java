package com.example.heroku.model.repository;

import com.example.heroku.model.Beer;
import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.count.ResultWithCount;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ResultWithCountRepository extends ReactiveCrudRepository<ResultWithCount, String> {

    @Query(value = "SELECT COUNT(*) as count FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search)")
    Mono<ResultWithCount> countSearchBeer(@Param("search")String search);

    @Query(value = "SELECT COUNT(*) as count FROM beer")
    Mono<ResultWithCount> countAll();

    @Query(value = "SELECT COUNT(*) as count FROM beer WHERE beer.category = :category")
    Mono<ResultWithCount> countCategory(@Param("category") Beer.Category category);

    @Query(value = "SELECT COUNT(*) as count FROM beer WHERE beer.meta_search LIKE :search")
    Mono<ResultWithCount> countSearchBeerLike(@Param("search")String search);

    @Query(value = "SELECT COUNT(*) as count FROM package_order WHERE package_order.status = :status")
    Mono<ResultWithCount> getAll(@Param("status") PackageOrder.Status status);
}
