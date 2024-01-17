package com.example.heroku.model.repository;

import com.example.heroku.model.DeleteRequest;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DeleteRequestRepository extends ReactiveCrudRepository<DeleteRequest, Long> {

    //search all
    @Query(value = "SELECT * FROM delete_request ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<DeleteRequest> findByIdNotNull(@Param("page")int page, @Param("size")int size);

    @Query(value = "DELETE FROM delete_request WHERE delete_request.user_id = :user_id")
    Mono<DeleteRequest> deleteByID(@Param("user_id")String user_id);


}
