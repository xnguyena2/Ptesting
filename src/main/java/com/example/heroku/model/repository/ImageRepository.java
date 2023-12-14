package com.example.heroku.model.repository;

import com.example.heroku.model.Image;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ImageRepository extends ReactiveCrudRepository<Image, Long> {

    @Query(value = "SELECT * FROM image WHERE image.category = :catetory AND image.group_id = :group_id")//, nativeQuery = true)
    Flux<Image> findByCategory(@Param("group_id")String group_id, @Param("catetory")String catetory);

    @Query(value = "SELECT * FROM image WHERE image.group_id = :group_id")//, nativeQuery = true)
    Flux<Image> findByGroupID(@Param("group_id")String group_id);

    @Query(value = "DELETE FROM image WHERE image.imgid = :imgid AND image.group_id = :group_id")
    Mono<Image> deleteImage(@Param("group_id")String group_id, @Param("imgid")String imgid);
}
