package com.example.heroku.model.repository;

import com.example.heroku.model.Beer;
import com.example.heroku.model.BeerUnit;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerUnitRepository extends ReactiveCrudRepository<BeerUnit, String> {

    @Query(value = "DELETE FROM beer_unit WHERE beer_unit.beer = :id")
    Mono<BeerUnit> deleteByBeerId(@Param("id")String id);

    @Query(value = "SELECT * FROM beer_unit WHERE beer_unit.beer = :id")
    Flux<BeerUnit> findByBeerID(@Param("id")String id);

    @Query(value = "SELECT * FROM beer_unit WHERE beer_unit.beer_unit_second_id = :id")
    Mono<BeerUnit> findByBeerUnitID(@Param("id")String id);
}
