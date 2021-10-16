package com.example.heroku.model.repository;

import com.example.heroku.model.Beer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerRepository extends ReactiveCrudRepository<Beer, String> {

    @Query(value = "SELECT * FROM beer WHERE beer.beer_second_id = :id")
    Mono<Beer> findBySecondID(@Param("id")String id);

    @Query(value = "SELECT * FROM beer WHERE beer.beer_second_id = :id AND (beer.status IS NULL OR (beer.status != 'SOLD_OUT' AND beer.status != 'HIDE'))")
    Mono<Beer> findBySecondIDCanOrder(@Param("id")String id);

    @Query(value = "DELETE FROM beer WHERE beer.beer_second_id = :id")
    Mono<Beer> deleteBySecondId(@Param("id")String id);


    //search all
    @Query(value = "SELECT * FROM beer WHERE (beer.status IS NULL OR beer.status != 'HIDE') ORDER BY :property DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> findByIdNotNullDESC(@Param("page")int page, @Param("size")int size, @Param("property")String property);
    //search all
    @Query(value = "SELECT * FROM beer WHERE (beer.status IS NULL OR beer.status != 'HIDE') ORDER BY :property ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> findByIdNotNullASC(@Param("page")int page, @Param("size")int size, @Param("property")String property);

    //search all
    @Query(value = "SELECT * FROM beer WHERE (beer.status IS NULL OR beer.status != 'HIDE') ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> findByIdNotNull(@Param("page")int page, @Param("size")int size);

    //search all
    //@Query(value = "SELECT * FROM beer ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> findByIdNotNull(Pageable pageable);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, MAX(beer_unit.price) AS price FROM beer LEFT JOIN beer_unit ON beer.beer_second_id = beer_unit.beer GROUP BY beer.beer_second_id ORDER BY price ASC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> adminGetAllBeerSortByPriceASC(@Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, MAX(beer_unit.price) AS price FROM beer LEFT JOIN beer_unit ON beer.beer_second_id = beer_unit.beer GROUP BY beer.beer_second_id ORDER BY price DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> adminGetAllBeerSortByPriceDESC(@Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, CASE WHEN SUM(beer_unit_order.number_unit) IS NULL THEN 0 ELSE SUM(beer_unit_order.number_unit) END AS sold FROM beer LEFT JOIN beer_unit_order ON beer.beer_second_id = beer_unit_order.beer_second_id GROUP BY beer.beer_second_id ORDER BY sold DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> adminGetAllBeerSortBySoldDESC(@Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, MAX(beer_unit.price) AS price FROM beer LEFT JOIN beer_unit ON beer.beer_second_id = beer_unit.beer WHERE (beer.status IS NULL OR beer.status != 'HIDE') GROUP BY beer.beer_second_id ORDER BY price ASC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> getAllBeerSortByPriceASC(@Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, MAX(beer_unit.price) AS price FROM beer LEFT JOIN beer_unit ON beer.beer_second_id = beer_unit.beer WHERE (beer.status IS NULL OR beer.status != 'HIDE') GROUP BY beer.beer_second_id ORDER BY price DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> getAllBeerSortByPriceDESC(@Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, CASE WHEN SUM(beer_unit_order.number_unit) IS NULL THEN 0 ELSE SUM(beer_unit_order.number_unit) END AS sold FROM beer LEFT JOIN beer_unit_order ON beer.beer_second_id = beer_unit_order.beer_second_id WHERE (beer.status IS NULL OR beer.status != 'HIDE') GROUP BY beer.beer_second_id ORDER BY sold DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> getAllBeerSortBySoldDESC(@Param("page")int page, @Param("size")int size);

    //search by category
    Flux<Beer> findByCategory(Beer.Category category, Pageable pageable);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, MAX(beer_unit.price) AS price FROM beer LEFT JOIN beer_unit ON beer.beer_second_id = beer_unit.beer WHERE beer.category = :category GROUP BY beer.beer_second_id ORDER BY price ASC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> AdminFindByCategoryBeerSortByPriceASC(@Param("category")Beer.Category category, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, MAX(beer_unit.price) AS price FROM beer LEFT JOIN beer_unit ON beer.beer_second_id = beer_unit.beer WHERE beer.category = :category GROUP BY beer.beer_second_id ORDER BY price DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> AdminFindByCategoryBeerSortByPriceDESC(@Param("category")Beer.Category category, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, CASE WHEN SUM(beer_unit_order.number_unit) IS NULL THEN 0 ELSE SUM(beer_unit_order.number_unit) END AS sold FROM beer LEFT JOIN beer_unit_order ON beer.beer_second_id = beer_unit_order.beer_second_id WHERE beer.category = :category GROUP BY beer.beer_second_id ORDER BY sold DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> AdminFindByCategoryBeerSortBySoldDESC(@Param("category")Beer.Category category, @Param("page")int page, @Param("size")int size);


    //search by category
    @Query(value = "SELECT * FROM beer WHERE (beer.category = :category) AND (beer.status IS NULL OR beer.status != 'HIDE') ORDER BY :property DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> findByCategoryDESC(@Param("category")Beer.Category category, @Param("page")int page, @Param("size")int size, @Param("property")String property);
    //search by category
    @Query(value = "SELECT * FROM beer WHERE (beer.category = :category) AND (beer.status IS NULL OR beer.status != 'HIDE') ORDER BY :property ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> findByCategoryASC(@Param("category")Beer.Category category, @Param("page")int page, @Param("size")int size, @Param("property")String property);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, MAX(beer_unit.price) AS price FROM beer LEFT JOIN beer_unit ON beer.beer_second_id = beer_unit.beer WHERE beer.category = :category AND (beer.status IS NULL OR beer.status != 'HIDE') GROUP BY beer.beer_second_id ORDER BY price ASC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> findByCategoryBeerSortByPriceASC(@Param("category")Beer.Category category, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, MAX(beer_unit.price) AS price FROM beer LEFT JOIN beer_unit ON beer.beer_second_id = beer_unit.beer WHERE beer.category = :category AND (beer.status IS NULL OR beer.status != 'HIDE') GROUP BY beer.beer_second_id ORDER BY price DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> findByCategoryBeerSortByPriceDESC(@Param("category")Beer.Category category, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, CASE WHEN SUM(beer_unit_order.number_unit) IS NULL THEN 0 ELSE SUM(beer_unit_order.number_unit) END AS sold FROM beer LEFT JOIN beer_unit_order ON beer.beer_second_id = beer_unit_order.beer_second_id WHERE beer.category = :category AND (beer.status IS NULL OR beer.status != 'HIDE') GROUP BY beer.beer_second_id ORDER BY sold DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> findByCategoryBeerSortBySoldDESC(@Param("category")Beer.Category category, @Param("page")int page, @Param("size")int size);



    //search by query for admin
    @Query(value = "SELECT * FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search) LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> adminSearchBeer(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer_s.beer_second_id, MAX(beer_unit.price) AS price FROM (SELECT beer.beer_second_id FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search)) beer_s LEFT JOIN beer_unit ON beer_s.beer_second_id = beer_unit.beer GROUP BY beer_s.beer_second_id ORDER BY price ASC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> adminSearchBeerSortByPriceASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer_s.beer_second_id, MAX(beer_unit.price) AS price FROM (SELECT beer.beer_second_id FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search)) beer_s LEFT JOIN beer_unit ON beer_s.beer_second_id = beer_unit.beer GROUP BY beer_s.beer_second_id ORDER BY price DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> adminSearchBeerSortByPriceDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search) ORDER BY name ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> adminSearchBeerSortByNameASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search) ORDER BY name DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> adminSearchBeerSortByNameDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search)  ORDER BY createat ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> adminSearchBeerSortByCreateASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search)  ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> adminSearchBeerSortByCreateDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer_s.beer_second_id, CASE WHEN SUM(beer_unit_order.number_unit) IS NULL THEN 0 ELSE SUM(beer_unit_order.number_unit) END AS sold from (SELECT beer.beer_second_id FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search)) beer_s LEFT JOIN beer_unit_order ON beer_s.beer_second_id = beer_unit_order.beer_second_id GROUP BY beer_s.beer_second_id ORDER BY sold DESC LIMIT :size OFFSET (:page*:size)) beer_temp LEFT JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> adminSearchBeerSortBySoldDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);



    //search by query for user
    @Query(value = "SELECT * FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search) AND (beer.status IS NULL OR beer.status != 'HIDE') LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeer(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer_s.beer_second_id, MAX(beer_unit.price) AS price FROM (SELECT beer.beer_second_id FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search) AND (beer.status IS NULL OR beer.status != 'HIDE')) beer_s LEFT JOIN beer_unit ON beer_s.beer_second_id = beer_unit.beer GROUP BY beer_s.beer_second_id ORDER BY price ASC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> searchBeerSortByPriceASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer_s.beer_second_id, MAX(beer_unit.price) AS price FROM (SELECT beer.beer_second_id FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search) AND (beer.status IS NULL OR beer.status != 'HIDE')) beer_s LEFT JOIN beer_unit ON beer_s.beer_second_id = beer_unit.beer GROUP BY beer_s.beer_second_id ORDER BY price DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> searchBeerSortByPriceDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search) AND (beer.status IS NULL OR beer.status != 'HIDE') ORDER BY name ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeerSortByNameASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search) AND (beer.status IS NULL OR beer.status != 'HIDE') ORDER BY name DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeerSortByNameDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search) AND (beer.status IS NULL OR beer.status != 'HIDE') ORDER BY createat ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeerSortByCreateASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search) AND (beer.status IS NULL OR beer.status != 'HIDE') ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeerSortByCreateDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer_s.beer_second_id, CASE WHEN SUM(beer_unit_order.number_unit) IS NULL THEN 0 ELSE SUM(beer_unit_order.number_unit) END AS sold from (SELECT beer.beer_second_id FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search) AND (beer.status IS NULL OR beer.status != 'HIDE')) beer_s LEFT JOIN beer_unit_order ON beer_s.beer_second_id = beer_unit_order.beer_second_id GROUP BY beer_s.beer_second_id ORDER BY sold DESC LIMIT :size OFFSET (:page*:size)) beer_temp LEFT JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> searchBeerSortBySoldDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);


    //SEARCH LIKE foradmin
    //select * from beer where meta_search ~ '(?=.*go)(?=.*ha)'
    @Query(value = "SELECT * FROM beer WHERE beer.meta_search LIKE :search LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> adminSearchBeerLike(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, MAX(beer_unit.price) AS price FROM beer LEFT JOIN beer_unit ON beer.beer_second_id = beer_unit.beer WHERE beer.meta_search LIKE :search GROUP BY beer.beer_second_id ORDER BY price ASC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> adminSearchBeerLikeSortByPriceASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, MAX(beer_unit.price) AS price FROM beer LEFT JOIN beer_unit ON beer.beer_second_id = beer_unit.beer WHERE beer.meta_search LIKE :search GROUP BY beer.beer_second_id ORDER BY price DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> adminSearchBeerLikeSortByPriceDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer WHERE beer.meta_search LIKE :search ORDER BY name ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> adminSearchBeerLikeSortByNameASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer WHERE beer.meta_search LIKE :search ORDER BY name DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> adminSearchBeerLikeSortByNameDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer WHERE beer.meta_search LIKE :search ORDER BY createat ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> adminSearchBeerLikeSortCreateASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer WHERE beer.meta_search LIKE :search ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> adminSearchBeerLikeSortCreateDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, CASE WHEN SUM(beer_unit_order.number_unit) IS NULL THEN 0 ELSE SUM(beer_unit_order.number_unit) END AS sold FROM beer LEFT JOIN beer_unit_order ON beer.beer_second_id = beer_unit_order.beer_second_id WHERE beer.meta_search LIKE :search GROUP BY beer.beer_second_id ORDER BY sold DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> adminSearchBeerLikeSortBySoldDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);


    //SEARCH LIKE
    //select * from beer where meta_search ~ '(?=.*go)(?=.*ha)'
    @Query(value = "SELECT * FROM beer WHERE beer.meta_search LIKE :search AND (beer.status IS NULL OR beer.status != 'HIDE') LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeerLike(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, MAX(beer_unit.price) AS price FROM beer LEFT JOIN beer_unit ON beer.beer_second_id = beer_unit.beer WHERE beer.meta_search LIKE :search AND (beer.status IS NULL OR beer.status != 'HIDE') GROUP BY beer.beer_second_id ORDER BY price ASC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> searchBeerLikeSortByPriceASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, MAX(beer_unit.price) AS price FROM beer LEFT JOIN beer_unit ON beer.beer_second_id = beer_unit.beer WHERE beer.meta_search LIKE :search AND (beer.status IS NULL OR beer.status != 'HIDE') GROUP BY beer.beer_second_id ORDER BY price DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> searchBeerLikeSortByPriceDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer WHERE beer.meta_search LIKE :search AND (beer.status IS NULL OR beer.status != 'HIDE') ORDER BY name ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeerLikeSortByNameASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer WHERE beer.meta_search LIKE :search AND (beer.status IS NULL OR beer.status != 'HIDE') ORDER BY name DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeerLikeSortByNameDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer WHERE beer.meta_search LIKE :search AND (beer.status IS NULL OR beer.status != 'HIDE') ORDER BY createat ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeerLikeSortCreateASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer WHERE beer.meta_search LIKE :search AND (beer.status IS NULL OR beer.status != 'HIDE') ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeerLikeSortCreateDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, CASE WHEN SUM(beer_unit_order.number_unit) IS NULL THEN 0 ELSE SUM(beer_unit_order.number_unit) END AS sold FROM beer LEFT JOIN beer_unit_order ON beer.beer_second_id = beer_unit_order.beer_second_id WHERE beer.meta_search LIKE :search AND (beer.status IS NULL OR beer.status != 'HIDE') GROUP BY beer.beer_second_id ORDER BY sold DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> searchBeerLikeSortBySoldDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);


    //get recommend word
    //SELECT SUBSTRING(beer.meta_search, '(?<=(nhap ngoai))(\w*)') FROM beer;
}
