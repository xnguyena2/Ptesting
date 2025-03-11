package com.example.heroku.model.repository;

import com.example.heroku.model.statistics.DebtOfBuyer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface StatisticDebtOfBuyerRepository extends ReactiveCrudRepository<DebtOfBuyer, Long> {

    @Query(value = "SELECT buyer.*, debt.in_come AS in_come, debt.out_come AS out_come FROM (SELECT device_id, SUM(CASE WHEN transaction_type = 'INCOME' THEN amount ELSE 0 END) AS in_come, SUM(CASE WHEN transaction_type = 'OUTCOME' THEN amount ELSE 0 END) AS out_come FROM debt_transaction WHERE group_id = :group_id GROUP BY device_id) AS debt LEFT JOIN (SELECT * FROM buyer WHERE group_id = :group_id) AS buyer ON debt.device_id = buyer.device_id")
    Flux<DebtOfBuyer> getIncomeOutComeAllBuyer(@Param("group_id") String groupID);
}
