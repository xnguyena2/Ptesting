package com.example.heroku.model.repository;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.statistics.BenifitByBuyer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;


public interface StatisticBenifitOfStaffRepository extends ReactiveCrudRepository<BenifitByBuyer, Long> {
    @Query(value = "SELECT ss.*, users_info.user_fullname AS name, users_info.phone AS phone FROM (SELECT COUNT(*) AS COUNT, COALESCE(SUM(payment), 0) AS revenue, COALESCE(SUM(profit), 0) AS profit, staff_id AS id FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.status = :status AND (user_package_detail.createat BETWEEN :fromtime AND :totime) GROUP BY staff_id LIMIT :size OFFSET (:page * :size)) AS ss LEFT JOIN users_info ON ss.id = users_info.username")
    Flux<BenifitByBuyer> getTotalStatictis(@Param("group_id") String groupID, @Param("fromtime") LocalDateTime from, @Param("totime") LocalDateTime to, @Param("status") UserPackageDetail.Status status, @Param("page")int page, @Param("size")int size);
}
