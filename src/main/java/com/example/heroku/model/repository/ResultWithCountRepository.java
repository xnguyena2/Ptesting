package com.example.heroku.model.repository;

import com.example.heroku.model.Product;
import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.count.ResultWithCount;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ResultWithCountRepository extends ReactiveCrudRepository<ResultWithCount, String> {

    @Query(value = "SELECT COUNT(*) as count FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search)")
    Mono<ResultWithCount> adminCountSearchBeer(@Param("search")String search);

    @Query(value = "SELECT COUNT(*) AS COUNT FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search) AND (product.status IS NULL OR product.status != 'HIDE')")
    Mono<ResultWithCount> countSearchBeer(@Param("search")String search);

    @Query(value = "SELECT COUNT(*) as count FROM product")
    Mono<ResultWithCount> adminCountAll();

    @Query(value = "SELECT COUNT(*) as count FROM product WHERE (product.status IS NULL OR product.status != 'HIDE')")
    Mono<ResultWithCount> countAll();

    @Query(value = "SELECT COUNT(*) as count FROM product WHERE product.category = :category")
    Mono<ResultWithCount> AdminCountCategory(@Param("category") String category);

    @Query(value = "SELECT COUNT(*) as count FROM product WHERE product.category = :category AND (product.status IS NULL OR product.status != 'HIDE')")
    Mono<ResultWithCount> countCategory(@Param("category") String category);


    @Query(value = "SELECT COUNT(*) as count FROM product WHERE product.meta_search LIKE :search")
    Mono<ResultWithCount> adminCountSearchBeerLike(@Param("search")String search);

    @Query(value = "SELECT COUNT(*) as count FROM product WHERE product.meta_search LIKE :search AND (product.status IS NULL OR product.status != 'HIDE')")
    Mono<ResultWithCount> countSearchBeerLike(@Param("search")String search);

    @Query(value = "SELECT COUNT(*) as count FROM package_order WHERE package_order.status = :status AND DATE_PART('day', NOW() - createat) <= :date")
    Mono<ResultWithCount> countPackageOrder(@Param("status") PackageOrder.Status status, @Param("date")int date);


    @Query(value = "SELECT COUNT(*) as count FROM users")
    Mono<ResultWithCount> userCountAll();
}
