package com.example.heroku.model.repository;

import com.example.heroku.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerRepository extends ReactiveCrudRepository<Product, String> {

    @Query(value = "SELECT * FROM product WHERE product.product_second_id = :id")
    Mono<Product> findBySecondID(@Param("id")String id);

    @Query(value = "SELECT * FROM product WHERE product.product_second_id = :id AND (product.status IS NULL OR (product.status != 'SOLD_OUT' AND product.status != 'HIDE'))")
    Mono<Product> findBySecondIDCanOrder(@Param("id")String id);

    @Query(value = "DELETE FROM product WHERE product.product_second_id = :id")
    Mono<Product> deleteBySecondId(@Param("id")String id);


    //search all
    @Query(value = "SELECT * FROM product WHERE (product.status IS NULL OR product.status != 'HIDE') ORDER BY :property DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> findByIdNotNullDESC(@Param("page")int page, @Param("size")int size, @Param("property")String property);
    //search all
    @Query(value = "SELECT * FROM product WHERE (product.status IS NULL OR product.status != 'HIDE') ORDER BY :property ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> findByIdNotNullASC(@Param("page")int page, @Param("size")int size, @Param("property")String property);

    //search all
    @Query(value = "SELECT * FROM product WHERE (product.status IS NULL OR product.status != 'HIDE') ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> findByIdNotNull(@Param("page")int page, @Param("size")int size);

    //search all
    //@Query(value = "SELECT * FROM product ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> findByIdNotNull(Pageable pageable);

    @Query(value = "SELECT product.* FROM (SELECT product.product_second_id, MAX(product_unit.price) AS price FROM product LEFT JOIN product_unit ON product.product_second_id = product_unit.product_second_id GROUP BY product.product_second_id ORDER BY price ASC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> adminGetAllBeerSortByPriceASC(@Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT product.product_second_id, MAX(product_unit.price) AS price FROM product LEFT JOIN product_unit ON product.product_second_id = product_unit.product_second_id GROUP BY product.product_second_id ORDER BY price DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> adminGetAllBeerSortByPriceDESC(@Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT product.product_second_id, CASE WHEN SUM(product_unit_order.number_unit) IS NULL THEN 0 ELSE SUM(product_unit_order.number_unit) END AS sold FROM product LEFT JOIN product_unit_order ON product.product_second_id = product_unit_order.product_second_id GROUP BY product.product_second_id ORDER BY sold DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> adminGetAllBeerSortBySoldDESC(@Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT product.product_second_id, MAX(product_unit.price) AS price FROM product LEFT JOIN product_unit ON product.product_second_id = product_unit.product_second_id WHERE (product.status IS NULL OR product.status != 'HIDE') GROUP BY product.product_second_id ORDER BY price ASC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> getAllBeerSortByPriceASC(@Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT product.product_second_id, MAX(product_unit.price) AS price FROM product LEFT JOIN product_unit ON product.product_second_id = product_unit.product_second_id WHERE (product.status IS NULL OR product.status != 'HIDE') GROUP BY product.product_second_id ORDER BY price DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> getAllBeerSortByPriceDESC(@Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT product.product_second_id, CASE WHEN SUM(product_unit_order.number_unit) IS NULL THEN 0 ELSE SUM(product_unit_order.number_unit) END AS sold FROM product LEFT JOIN product_unit_order ON product.product_second_id = product_unit_order.product_second_id WHERE (product.status IS NULL OR product.status != 'HIDE') GROUP BY product.product_second_id ORDER BY sold DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> getAllBeerSortBySoldDESC(@Param("page")int page, @Param("size")int size);

    //search by category
    Flux<Product> findByCategory(Product.Category category, Pageable pageable);

    @Query(value = "SELECT product.* FROM (SELECT product.product_second_id, MAX(product_unit.price) AS price FROM product LEFT JOIN product_unit ON product.product_second_id = product_unit.product_second_id WHERE product.category = :category GROUP BY product.product_second_id ORDER BY price ASC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> AdminFindByCategoryBeerSortByPriceASC(@Param("category") Product.Category category, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT product.product_second_id, MAX(product_unit.price) AS price FROM product LEFT JOIN product_unit ON product.product_second_id = product_unit.product_second_id WHERE product.category = :category GROUP BY product.product_second_id ORDER BY price DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> AdminFindByCategoryBeerSortByPriceDESC(@Param("category") Product.Category category, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT product.product_second_id, CASE WHEN SUM(product_unit_order.number_unit) IS NULL THEN 0 ELSE SUM(product_unit_order.number_unit) END AS sold FROM product LEFT JOIN product_unit_order ON product.product_second_id = product_unit_order.product_second_id WHERE product.category = :category GROUP BY product.product_second_id ORDER BY sold DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> AdminFindByCategoryBeerSortBySoldDESC(@Param("category") Product.Category category, @Param("page")int page, @Param("size")int size);


    //search by category
    @Query(value = "SELECT * FROM product WHERE (product.category = :category) AND (product.status IS NULL OR product.status != 'HIDE') ORDER BY :property DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> findByCategoryDESC(@Param("category") Product.Category category, @Param("page")int page, @Param("size")int size, @Param("property")String property);
    //search by category
    @Query(value = "SELECT * FROM product WHERE (product.category = :category) AND (product.status IS NULL OR product.status != 'HIDE') ORDER BY :property ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> findByCategoryASC(@Param("category") Product.Category category, @Param("page")int page, @Param("size")int size, @Param("property")String property);

    @Query(value = "SELECT product.* FROM (SELECT product.product_second_id, MAX(product_unit.price) AS price FROM product LEFT JOIN product_unit ON product.product_second_id = product_unit.product_second_id WHERE product.category = :category AND (product.status IS NULL OR product.status != 'HIDE') GROUP BY product.product_second_id ORDER BY price ASC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> findByCategoryBeerSortByPriceASC(@Param("category") Product.Category category, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT product.product_second_id, MAX(product_unit.price) AS price FROM product LEFT JOIN product_unit ON product.product_second_id = product_unit.product_second_id WHERE product.category = :category AND (product.status IS NULL OR product.status != 'HIDE') GROUP BY product.product_second_id ORDER BY price DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> findByCategoryBeerSortByPriceDESC(@Param("category") Product.Category category, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT product.product_second_id, CASE WHEN SUM(product_unit_order.number_unit) IS NULL THEN 0 ELSE SUM(product_unit_order.number_unit) END AS sold FROM product LEFT JOIN product_unit_order ON product.product_second_id = product_unit_order.product_second_id WHERE product.category = :category AND (product.status IS NULL OR product.status != 'HIDE') GROUP BY product.product_second_id ORDER BY sold DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> findByCategoryBeerSortBySoldDESC(@Param("category") Product.Category category, @Param("page")int page, @Param("size")int size);



    //search by query for admin
    @Query(value = "SELECT * FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search) LIMIT :size OFFSET (:page*:size)")
    Flux<Product> adminSearchBeer(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT beer_s.product_second_id, MAX(product_unit.price) AS price FROM (SELECT product.product_second_id FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search)) beer_s LEFT JOIN product_unit ON beer_s.product_second_id = product_unit.product_second_id GROUP BY beer_s.product_second_id ORDER BY price ASC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> adminSearchBeerSortByPriceASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT beer_s.product_second_id, MAX(product_unit.price) AS price FROM (SELECT product.product_second_id FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search)) beer_s LEFT JOIN product_unit ON beer_s.product_second_id = product_unit.product_second_id GROUP BY beer_s.product_second_id ORDER BY price DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> adminSearchBeerSortByPriceDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search) ORDER BY name ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> adminSearchBeerSortByNameASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search) ORDER BY name DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> adminSearchBeerSortByNameDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search)  ORDER BY createat ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> adminSearchBeerSortByCreateASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search)  ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> adminSearchBeerSortByCreateDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT beer_s.product_second_id, CASE WHEN SUM(product_unit_order.number_unit) IS NULL THEN 0 ELSE SUM(product_unit_order.number_unit) END AS sold from (SELECT product.product_second_id FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search)) beer_s LEFT JOIN product_unit_order ON beer_s.product_second_id = product_unit_order.product_second_id GROUP BY beer_s.product_second_id ORDER BY sold DESC LIMIT :size OFFSET (:page*:size)) beer_temp LEFT JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> adminSearchBeerSortBySoldDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);



    //search by query for user
    @Query(value = "SELECT * FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search) AND (product.status IS NULL OR product.status != 'HIDE') LIMIT :size OFFSET (:page*:size)")
    Flux<Product> searchBeer(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT beer_s.product_second_id, MAX(product_unit.price) AS price FROM (SELECT product.product_second_id FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search) AND (product.status IS NULL OR product.status != 'HIDE')) beer_s LEFT JOIN product_unit ON beer_s.product_second_id = product_unit.product_second_id GROUP BY beer_s.product_second_id ORDER BY price ASC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> searchBeerSortByPriceASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT beer_s.product_second_id, MAX(product_unit.price) AS price FROM (SELECT product.product_second_id FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search) AND (product.status IS NULL OR product.status != 'HIDE')) beer_s LEFT JOIN product_unit ON beer_s.product_second_id = product_unit.product_second_id GROUP BY beer_s.product_second_id ORDER BY price DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> searchBeerSortByPriceDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search) AND (product.status IS NULL OR product.status != 'HIDE') ORDER BY name ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> searchBeerSortByNameASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search) AND (product.status IS NULL OR product.status != 'HIDE') ORDER BY name DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> searchBeerSortByNameDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search) AND (product.status IS NULL OR product.status != 'HIDE') ORDER BY createat ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> searchBeerSortByCreateASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search) AND (product.status IS NULL OR product.status != 'HIDE') ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> searchBeerSortByCreateDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT beer_s.product_second_id, CASE WHEN SUM(product_unit_order.number_unit) IS NULL THEN 0 ELSE SUM(product_unit_order.number_unit) END AS sold from (SELECT product.product_second_id FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id WHERE search_token.tokens @@ to_tsquery(:search) AND (product.status IS NULL OR product.status != 'HIDE')) beer_s LEFT JOIN product_unit_order ON beer_s.product_second_id = product_unit_order.product_second_id GROUP BY beer_s.product_second_id ORDER BY sold DESC LIMIT :size OFFSET (:page*:size)) beer_temp LEFT JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> searchBeerSortBySoldDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);


    //SEARCH LIKE foradmin
    //select * FROM product where meta_search ~ '(?=.*go)(?=.*ha)'
    @Query(value = "SELECT * FROM product WHERE product.meta_search LIKE :search LIMIT :size OFFSET (:page*:size)")
    Flux<Product> adminSearchBeerLike(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT product.product_second_id, MAX(product_unit.price) AS price FROM product LEFT JOIN product_unit ON product.product_second_id = product_unit.product_second_id WHERE product.meta_search LIKE :search GROUP BY product.product_second_id ORDER BY price ASC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> adminSearchBeerLikeSortByPriceASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT product.product_second_id, MAX(product_unit.price) AS price FROM product LEFT JOIN product_unit ON product.product_second_id = product_unit.product_second_id WHERE product.meta_search LIKE :search GROUP BY product.product_second_id ORDER BY price DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> adminSearchBeerLikeSortByPriceDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product WHERE product.meta_search LIKE :search ORDER BY name ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> adminSearchBeerLikeSortByNameASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product WHERE product.meta_search LIKE :search ORDER BY name DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> adminSearchBeerLikeSortByNameDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product WHERE product.meta_search LIKE :search ORDER BY createat ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> adminSearchBeerLikeSortCreateASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product WHERE product.meta_search LIKE :search ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> adminSearchBeerLikeSortCreateDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT product.product_second_id, CASE WHEN SUM(product_unit_order.number_unit) IS NULL THEN 0 ELSE SUM(product_unit_order.number_unit) END AS sold FROM product LEFT JOIN product_unit_order ON product.product_second_id = product_unit_order.product_second_id WHERE product.meta_search LIKE :search GROUP BY product.product_second_id ORDER BY sold DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> adminSearchBeerLikeSortBySoldDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);


    //SEARCH LIKE
    //select * FROM product where meta_search ~ '(?=.*go)(?=.*ha)'
    @Query(value = "SELECT * FROM product WHERE product.meta_search LIKE :search AND (product.status IS NULL OR product.status != 'HIDE') LIMIT :size OFFSET (:page*:size)")
    Flux<Product> searchBeerLike(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT product.product_second_id, MAX(product_unit.price) AS price FROM product LEFT JOIN product_unit ON product.product_second_id = product_unit.product_second_id WHERE product.meta_search LIKE :search AND (product.status IS NULL OR product.status != 'HIDE') GROUP BY product.product_second_id ORDER BY price ASC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> searchBeerLikeSortByPriceASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT product.product_second_id, MAX(product_unit.price) AS price FROM product LEFT JOIN product_unit ON product.product_second_id = product_unit.product_second_id WHERE product.meta_search LIKE :search AND (product.status IS NULL OR product.status != 'HIDE') GROUP BY product.product_second_id ORDER BY price DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> searchBeerLikeSortByPriceDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product WHERE product.meta_search LIKE :search AND (product.status IS NULL OR product.status != 'HIDE') ORDER BY name ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> searchBeerLikeSortByNameASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product WHERE product.meta_search LIKE :search AND (product.status IS NULL OR product.status != 'HIDE') ORDER BY name DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> searchBeerLikeSortByNameDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product WHERE product.meta_search LIKE :search AND (product.status IS NULL OR product.status != 'HIDE') ORDER BY createat ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> searchBeerLikeSortCreateASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product WHERE product.meta_search LIKE :search AND (product.status IS NULL OR product.status != 'HIDE') ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Product> searchBeerLikeSortCreateDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM (SELECT product.product_second_id, CASE WHEN SUM(product_unit_order.number_unit) IS NULL THEN 0 ELSE SUM(product_unit_order.number_unit) END AS sold FROM product LEFT JOIN product_unit_order ON product.product_second_id = product_unit_order.product_second_id WHERE product.meta_search LIKE :search AND (product.status IS NULL OR product.status != 'HIDE') GROUP BY product.product_second_id ORDER BY sold DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN product ON product.product_second_id = beer_temp.product_second_id")
    Flux<Product> searchBeerLikeSortBySoldDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);


    //get recommend word
    //SELECT SUBSTRING(product.meta_search, '(?<=(nhap ngoai))(\w*)') FROM product;
}
