package com.example.heroku.model.repository;

import com.example.heroku.model.Image;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ImageRepository extends ReactiveCrudRepository<Image, String> {
    Mono<Image> findById(String id);

    @Query(value = "SELECT i.Id FROM image i WHERE i.Category = :catetory")//, nativeQuery = true)
    Flux<Image> findByCategory(@Param("catetory")String catetory);
}
