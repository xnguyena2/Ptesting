package com.example.heroku.model.repository;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.statistics.CountOrderByDate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface StatisticCountOrdersRepository extends ReactiveCrudRepository<CountOrderByDate, Long> {

    @Query(value = "SELECT COUNT(CASE WHEN \"status\" = :cancel_status THEN 1 END) AS count_cancel, COUNT(status) FILTER ( WHERE \"status\" = :return_status) AS count_return, SUM(CASE WHEN \"status\" = :cancel_status THEN user_package_detail.payment ELSE 0 END) as revenue_cancel, SUM(CASE WHEN \"status\" = :return_status THEN user_package_detail.payment ELSE 0 END) as revenue_return FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND (user_package_detail.status = :cancel_status OR user_package_detail.status = :return_status) AND (user_package_detail.createat AT TIME ZONE '+07' BETWEEN :fromtime AND :totime)")
    Mono<CountOrderByDate> getStatictis(@Param("group_id") String groupID, @Param("fromtime") Timestamp from, @Param("totime") Timestamp to, @Param("cancel_status") UserPackageDetail.Status cancel_status, @Param("return_status") UserPackageDetail.Status return_status);

}
