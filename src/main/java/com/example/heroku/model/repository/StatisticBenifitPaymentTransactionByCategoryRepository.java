package com.example.heroku.model.repository;

import com.example.heroku.model.statistics.BenifitByPaymentTransaction;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.sql.Timestamp;

public interface StatisticBenifitPaymentTransactionByCategoryRepository extends ReactiveCrudRepository<BenifitByPaymentTransaction, Long> {

    @Query(value = "SELECT SUM(CASE payment_transaction.transaction_type WHEN 'OUTCOME' THEN payment_transaction.amount END ) AS cost, SUM(CASE payment_transaction.transaction_type WHEN 'INCOME' THEN payment_transaction.amount END ) AS revenue, payment_transaction.category FROM payment_transaction WHERE payment_transaction.group_id = :group_id AND payment_transaction.package_second_id IS NULL AND (payment_transaction.createat AT TIME ZONE '+07' BETWEEN :fromtime AND :totime) GROUP BY category")
    Flux<BenifitByPaymentTransaction> getStatictis(@Param("group_id") String groupID, @Param("fromtime") Timestamp from, @Param("totime") Timestamp to);
}
