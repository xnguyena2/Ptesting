package com.example.heroku.model.repository;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.statistics.BenifitByMonth;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;


public interface StatisticTotalBenifitRepository extends ReactiveCrudRepository<BenifitByMonth, Long> {
    @Query(value = """
            SELECT *
            FROM
              (SELECT COUNT(CASE WHEN COALESCE(device_id, '') = '' THEN 1 END)
              + COUNT (DISTINCT device_id) FILTER (WHERE COALESCE(device_id, '') <> '') AS buyer,
                 COALESCE(SUM(cost), 0) AS cost,
                 COALESCE(SUM(profit), 0) AS profit,
                 COALESCE(SUM(payment), 0) AS revenue,
                 COALESCE(SUM(price), 0) AS price,
                 COALESCE(SUM(ship_price), 0) AS ship_price,
                 COALESCE(SUM(discount_amount + discount_percent / 100 * price), 0) AS discount,
                 COUNT(*) AS count
               FROM user_package_detail
               WHERE user_package_detail.group_id = :group_id
                 AND user_package_detail.status = :status
                 AND (user_package_detail.createat AT TIME ZONE :time_zone BETWEEN :fromtime AND :totime)) user_package_detail
            CROSS JOIN
              (SELECT COALESCE(SUM(payment_transaction.amount), 0) AS payment_order
               FROM payment_transaction
               WHERE payment_transaction.group_id = :group_id
                 AND payment_transaction.action_type = 'PAYMENT_ORDER'
                 AND (payment_transaction.createat AT TIME ZONE :time_zone BETWEEN :fromtime AND :totime)) payment_transaction
            """)
    Mono<BenifitByMonth> getTotalStatictis(@Param("group_id") String groupID, @Param("time_zone") String time_zone,
                                           @Param("fromtime") LocalDateTime from, @Param("totime") LocalDateTime to, @Param("status") UserPackageDetail.Status status);

    @Query(value = """
            SELECT COALESCE(SUM(payment), 0) AS revenue,
                   COALESCE(SUM(discount_amount + discount_percent / 100 * price), 0) AS discount,
                   COUNT(*) AS count,
                   0 AS profit,
                   0 AS cost,
                   0 AS price,
                   0 AS ship_price,
                   0 AS buyer,
                   0 AS payment_order
            FROM user_package_detail
            WHERE user_package_detail.group_id = :group_id
              AND user_package_detail.device_id = :device_id
              AND user_package_detail.status = :status
              AND (user_package_detail.createat AT TIME ZONE :time_zone BETWEEN :fromtime AND :totime)
            """)
    Mono<BenifitByMonth> getTotalStatictisOfBuyer(@Param("group_id") String groupID, @Param("device_id") String device_id, @Param("time_zone") String time_zone,
                                                  @Param("fromtime") LocalDateTime from, @Param("totime") LocalDateTime to, @Param("status") UserPackageDetail.Status status);
}
