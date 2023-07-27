package com.example.heroku.model.repository;

import com.example.heroku.model.Product;
import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.count.ResultWithCount;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ResultWithCountRepository extends ReactiveCrudRepository<ResultWithCount, String> {

    @Query(value = "SELECT COUNT(*) as count FROM ( SELECT * FROM product WHERE product.group_id = :group_id ) product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search)")
    Mono<ResultWithCount> adminCountSearchBeer(@Param("group_id")String group_id, @Param("search")String search);

    @Query(value = "SELECT COUNT(*) AS COUNT FROM ( SELECT * FROM product WHERE product.group_id = :group_id ) product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search) AND product.status IS DISTINCT FROM 'HIDE'")
    Mono<ResultWithCount> countSearchBeer(@Param("group_id")String group_id, @Param("search")String search);

    @Query(value = "SELECT COUNT(*) as count FROM product WHERE product.group_id = :group_id")
    Mono<ResultWithCount> adminCountAll(@Param("group_id")String group_id);

    @Query(value = "SELECT COUNT(*) as count FROM product WHERE product.group_id = :group_id AND product.status IS DISTINCT FROM 'HIDE'")
    Mono<ResultWithCount> countAll(@Param("group_id")String group_id);

    @Query(value = "SELECT COUNT(*) as count FROM product WHERE product.group_id = :group_id AND product.category = :category")
    Mono<ResultWithCount> AdminCountCategory(@Param("group_id")String group_id, @Param("category") String category);

    @Query(value = "SELECT COUNT(*) as count FROM product WHERE product.group_id = :group_id AND product.category = :category AND product.status IS DISTINCT FROM 'HIDE'")
    Mono<ResultWithCount> countCategory(@Param("group_id")String group_id, @Param("category") String category);


    @Query(value = "SELECT COUNT(*) as count FROM product WHERE product.group_id = :group_id AND product.meta_search LIKE :search")
    Mono<ResultWithCount> adminCountSearchBeerLike(@Param("group_id")String group_id, @Param("search")String search);

    @Query(value = "SELECT COUNT(*) as count FROM product WHERE product.group_id = :group_id AND product.meta_search LIKE :search AND product.status IS DISTINCT FROM 'HIDE'")
    Mono<ResultWithCount> countSearchBeerLike(@Param("group_id")String group_id, @Param("search")String search);

    @Query(value = "SELECT COUNT(*) as count FROM package_order WHERE package_order.group_id = :group_id AND package_order.status = :status AND DATE_PART('day', NOW() - createat) <= :date")
    Mono<ResultWithCount> countPackageOrder(@Param("group_id")String group_id, @Param("status") PackageOrder.Status status, @Param("date")int date);


    @Query(value = "SELECT COUNT(*) as count FROM users WHERE users.group_id = :group_id")
    Mono<ResultWithCount> userCountAll(@Param("group_id")String group_id);
}
