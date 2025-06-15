package com.example.heroku.model.repository;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.statistics.BenifitByBuyer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;


public interface StatisticBenifitOfBuyerRepository extends ReactiveCrudRepository<BenifitByBuyer, Long> {
    @Query(value = "SELECT ss.count, ss.revenue, ss.profit, buyer.reciver_fullname AS name, buyer.phone_number AS id FROM (SELECT COUNT(*) AS COUNT, COALESCE(SUM(payment), 0) AS revenue, COALESCE(SUM(profit), 0) AS profit, device_id AS device_id FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.status = :status AND (user_package_detail.createat AT TIME ZONE '+07' BETWEEN :fromtime AND :totime) GROUP BY device_id LIMIT :size OFFSET (:page * :size)) AS ss LEFT JOIN (SELECT * FROM buyer WHERE buyer.group_id = :group_id) AS buyer ON ss.device_id = buyer.device_id")
    Flux<BenifitByBuyer> getTotalStatictis(@Param("group_id") String groupID, @Param("fromtime") LocalDateTime from, @Param("totime") LocalDateTime to, @Param("status") UserPackageDetail.Status status, @Param("page")int page, @Param("size")int size);
}
