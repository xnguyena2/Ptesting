package com.example.heroku.model.repository;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.statistics.BenifitByDate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.sql.Timestamp;

public interface StatisticBenifitRepository  extends ReactiveCrudRepository<BenifitByDate, Long> {

    @Query(value = "SELECT COUNT(CASE WHEN \"device_id\" = '' THEN 1 END) + COUNT (DISTINCT device_id) FILTER (WHERE \"device_id\" <> '') AS buyer, SUM(user_package_detail.cost) AS cost, COUNT(*) AS count, SUM(user_package_detail.payment) AS revenue, SUM(user_package_detail.price) AS price, SUM(user_package_detail.profit) AS profit, (user_package_detail.createat AT TIME ZONE '+07')::date AS local_time FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.status = :status AND (user_package_detail.createat AT TIME ZONE '+07' BETWEEN :fromtime AND :totime) GROUP BY local_time")
    Flux<BenifitByDate> getStatictis(@Param("group_id") String groupID, @Param("fromtime") Timestamp from, @Param("totime") Timestamp to, @Param("status") UserPackageDetail.Status status);

}
