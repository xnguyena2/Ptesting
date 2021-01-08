package com.example.heroku.model.repository;

import com.example.heroku.model.UserPackage;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserPackageRepository extends ReactiveCrudRepository<UserPackage, String> {

    @Query(value = "SELECT user_package.device_id, user_package.beer_id, user_package.beer_unit, SUM(user_package.number_unit) as number_unit, MAX(user_package.createat) as createat FROM user_package WHERE user_package.device_id = :id GROUP BY user_package.device_id, user_package.beer_id, user_package.beer_unit ORDER BY createat DESC LIMIT :size OFFSET (:page*:size)")
    Flux<UserPackage> GetDevicePackage(@Param("id")String id, @Param("page")int page, @Param("size")int size);
}
