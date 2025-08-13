package com.example.heroku.model.repository;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.statistics.BenifitByDateHour;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;


public interface StatisticBenifitByHourRepository extends ReactiveCrudRepository<BenifitByDateHour, Long> {

    @Query(value = """
            SELECT COUNT(CASE
                             WHEN "device_id" = '' THEN 1
                         END) + COUNT (DISTINCT device_id) FILTER (
                                                                   WHERE "device_id" <> '') AS buyer,
                                      SUM(user_package_detail.cost) AS cost,
                                      COUNT(*) AS count,
                                      SUM(user_package_detail.payment) AS revenue,
                                      SUM(user_package_detail.price) AS price,
                                      SUM(user_package_detail.profit) AS profit,
                                      COALESCE(SUM(ship_price), 0) AS ship_price,
                                      COALESCE(SUM(discount_promotional), 0) AS discount_promotional,
                                      COALESCE(SUM(discount_by_point), 0) AS discount_by_point,
                                      COALESCE(SUM(additional_fee), 0) AS additional_fee,
                                      COALESCE(SUM(discount_amount + discount_percent / 100 * price), 0) AS discount,
                                      date_trunc('hour', user_package_detail.createat AT TIME ZONE :time_zone) AS local_time
            FROM user_package_detail
            WHERE user_package_detail.group_id = :group_id
              AND user_package_detail.status = :status
              AND (user_package_detail.createat AT TIME ZONE :time_zone BETWEEN :fromtime AND :totime)
            GROUP BY local_time
            """)
    Flux<BenifitByDateHour> getStatictisByHour(@Param("group_id") String groupID, @Param("time_zone") String time_zone,
                                               @Param("fromtime") LocalDateTime from, @Param("totime") LocalDateTime to, @Param("status") UserPackageDetail.Status status);
}
