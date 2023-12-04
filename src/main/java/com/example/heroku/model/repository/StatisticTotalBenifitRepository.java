package com.example.heroku.model.repository;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.statistics.BenifitByMonth;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface StatisticTotalBenifitRepository extends ReactiveCrudRepository<BenifitByMonth, Long> {
    @Query(value = "SELECT COALESCE(SUM(cost), 0) AS cost, COALESCE(SUM(profit), 0) AS profit, COALESCE(SUM(payment), 0) AS revenue, COUNT(*) AS count FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.status = :status AND (user_package_detail.createat AT TIME ZONE 'ICT' BETWEEN :fromtime AND :totime)")
    Mono<BenifitByMonth> getTotalStatictis(@Param("group_id") String groupID, @Param("fromtime") Timestamp from, @Param("totime") Timestamp to, @Param("status") UserPackageDetail.Status status);
}
