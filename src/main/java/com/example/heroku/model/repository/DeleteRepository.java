package com.example.heroku.model.repository;

import com.example.heroku.model.Users;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface DeleteRepository extends ReactiveCrudRepository<Users, Long> {

    @Query(value = "select delete_all_data_belong_user(:group_id)")
    Mono<Boolean> deleteByGroupId(@Param("group_id")String group_id);

}
