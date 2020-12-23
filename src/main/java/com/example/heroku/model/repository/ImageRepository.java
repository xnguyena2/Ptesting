package com.example.heroku.model.repository;

import com.example.heroku.model.Image;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.stream.Stream;

public interface ImageRepository extends ReactiveCrudRepository<Image, String> {
    Mono<Image> findById(String id);

    @Query(value = "SELECT i.Id FROM image i WHERE i.Category = :carousel")//, nativeQuery = true)
    Mono<String> getImgID(@Param("carousel") String carousel);
}
