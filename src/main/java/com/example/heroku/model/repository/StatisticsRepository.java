package com.example.heroku.model.repository;

import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.statistics.StatisticsTotalOrder;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface StatisticsRepository extends ReactiveCrudRepository<StatisticsTotalOrder, String> {

    @Query(value = "SELECT SUM(real_price) as real_price, SUM(total_price) as total_price FROM package_order WHERE DATE_PART('day', createat - NOW()) <= :date AND status = :status")
    Mono<StatisticsTotalOrder> getTotal(@Param("date")int date, @Param("status") PackageOrder.Status status);
}
