package com.example.heroku.model.repository;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.statistics.BenifitByOrder;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;


public interface StatisticBenifitOfOrderRepository extends ReactiveCrudRepository<BenifitByOrder, Long> {

    @Query(value = "SELECT package_second_id, payment AS revenue, profit, price, ship_price, createat FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.status = :status AND (user_package_detail.createat AT TIME ZONE '+07' BETWEEN :fromtime AND :totime) LIMIT :size OFFSET (:page * :size)")
    Flux<BenifitByOrder> getStatictis(@Param("group_id") String groupID, @Param("fromtime") LocalDateTime from, @Param("totime") LocalDateTime to, @Param("status") UserPackageDetail.Status status, @Param("page")int page, @Param("size")int size);

}
