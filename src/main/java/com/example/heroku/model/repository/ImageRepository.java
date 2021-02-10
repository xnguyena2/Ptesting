package com.example.heroku.model.repository;

import com.example.heroku.model.Image;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ImageRepository extends ReactiveCrudRepository<Image, String> {
    @Query(value = "SELECT image.id, image.imgid, image.category, image.createat FROM image WHERE image.id = :id")
    Mono<Image> findById(@Param("id")int id);

    @Query(value = "SELECT i.imgid FROM image i WHERE i.Category = :catetory")//, nativeQuery = true)
    Flux<Image> findByCategory(@Param("catetory")String catetory);

    @Query(value = "DELETE FROM image WHERE image.imgid = :id")
    Mono<Image> deleteById(@Param("id")int id);
}
