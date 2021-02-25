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

    //@Query(value = "SELECT * FROM beer ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> findByIdNotNull(Pageable pageable);

    Flux<Beer> findByCategory(Beer.Category category, Pageable pageable);

    @Query(value = "SELECT * FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search) LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeer(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer_s.beer_second_id, MAX(beer_unit.price) AS price FROM (SELECT beer.beer_second_id FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search)) beer_s LEFT JOIN beer_unit ON beer_s.beer_second_id = beer_unit.beer GROUP BY beer_s.beer_second_id ORDER BY price ASC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> searchBeerSortByPriceASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer_s.beer_second_id, MAX(beer_unit.price) AS price FROM (SELECT beer.beer_second_id FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search)) beer_s LEFT JOIN beer_unit ON beer_s.beer_second_id = beer_unit.beer GROUP BY beer_s.beer_second_id ORDER BY price DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> searchBeerSortByPriceDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search) ORDER BY name ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeerSortByNameASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search) ORDER BY name DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeerSortByNameDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search)  ORDER BY createat ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeerSortByCreateASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search)  ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeerSortByCreateDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer_s.beer_second_id, CASE WHEN SUM(beer_unit_order.number_unit) IS NULL THEN 0 ELSE SUM(beer_unit_order.number_unit) END AS sold from (SELECT beer.beer_second_id FROM beer INNER JOIN search_token ON search_token.beer_second_id = beer.beer_second_id WHERE search_token.tokens @@ to_tsquery(:search)) beer_s LEFT JOIN beer_unit_order ON beer_s.beer_second_id = beer_unit_order.beer_second_id GROUP BY beer_s.beer_second_id ORDER BY sold DESC LIMIT :size OFFSET (:page*:size)) beer_temp LEFT JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> searchBeerSortBySoldDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);


    //SEARCH LIKE
    //select * from beer where meta_search ~ '(?=.*go)(?=.*ha)'
    @Query(value = "SELECT * FROM beer WHERE beer.meta_search LIKE :search LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeerLike(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, MAX(beer_unit.price) AS price FROM beer LEFT JOIN beer_unit ON beer.beer_second_id = beer_unit.beer WHERE beer.meta_search LIKE :search GROUP BY beer.beer_second_id ORDER BY price ASC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> searchBeerLikeSortByPriceASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, MAX(beer_unit.price) AS price FROM beer LEFT JOIN beer_unit ON beer.beer_second_id = beer_unit.beer WHERE beer.meta_search LIKE :search GROUP BY beer.beer_second_id ORDER BY price DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> searchBeerLikeSortByPriceDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer WHERE beer.meta_search LIKE :search ORDER BY name ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeerLikeSortByNameASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer WHERE beer.meta_search LIKE :search ORDER BY name DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeerLikeSortByNameDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer WHERE beer.meta_search LIKE :search ORDER BY createat ASC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeerLikeSortCreateASC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM beer WHERE beer.meta_search LIKE :search ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<Beer> searchBeerLikeSortCreateDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT beer.* FROM (SELECT beer.beer_second_id, CASE WHEN SUM(beer_unit_order.number_unit) IS NULL THEN 0 ELSE SUM(beer_unit_order.number_unit) END AS sold FROM beer LEFT JOIN beer_unit_order ON beer.beer_second_id = beer_unit_order.beer_second_id WHERE beer.meta_search LIKE :search GROUP BY beer.beer_second_id ORDER BY sold DESC LIMIT :size OFFSET (:page*:size)) beer_temp INNER JOIN beer ON beer.beer_second_id = beer_temp.beer_second_id")
    Flux<Beer> searchBeerLikeSortBySoldDESC(@Param("search")String search, @Param("page")int page, @Param("size")int size);

    //get recommend word
    //SELECT SUBSTRING(beer.meta_search, '(?<=(nhap ngoai))(\w*)') FROM beer;
}
