package com.example.heroku.model.repository;

import com.example.heroku.model.statistics.BenifitByDate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;


public interface StatisticBenifitPaymentTransactionByDateRepository extends ReactiveCrudRepository<BenifitByDate, Long> {

    @Query(value = """
            SELECT SUM(CASE payment_transaction.transaction_type
                           WHEN 'OUTCOME' THEN payment_transaction.amount
                       END) AS cost,
                   SUM(CASE payment_transaction.transaction_type
                           WHEN 'INCOME' THEN payment_transaction.amount
                       END) AS revenue,
                   (payment_transaction.createat AT TIME ZONE :time_zone)::date AS local_time
            FROM payment_transaction
            WHERE payment_transaction.group_id = :group_id
              AND payment_transaction.package_second_id IS NULL
              AND (payment_transaction.createat BETWEEN :fromtime AND :totime)
            GROUP BY local_time
            """)
    Flux<BenifitByDate> getStatictis(@Param("group_id") String groupID, @Param("time_zone") String time_zone,
                                     @Param("fromtime") LocalDateTime from, @Param("totime") LocalDateTime to);

}
