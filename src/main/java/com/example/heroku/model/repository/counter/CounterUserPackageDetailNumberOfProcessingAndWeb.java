package com.example.heroku.model.repository.counter;

import com.example.heroku.model.UserPackageDetail;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CounterUserPackageDetailNumberOfProcessingAndWeb extends ReactiveCrudRepository<com.example.heroku.response.CounterUserPackageDetailNumberOfProcessingAndWeb, Long> {
    @Query(value = "SELECT COUNT(CASE WHEN status = :processing THEN 1 ELSE null END) AS processing, COUNT(CASE WHEN status = :web THEN 1 ELSE null END) AS web FROM user_package_detail WHERE group_id = :group_id")
    Mono<com.example.heroku.response.CounterUserPackageDetailNumberOfProcessingAndWeb> counterPackage(@Param("group_id")String groupID, @Param("processing") UserPackageDetail.Status processing, @Param("web") UserPackageDetail.Status or_status);

}
