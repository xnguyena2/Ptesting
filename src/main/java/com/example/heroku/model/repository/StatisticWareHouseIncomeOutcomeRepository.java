package com.example.heroku.model.repository;

import com.example.heroku.model.ProductImport;
import com.example.heroku.model.statistics.WareHouseIncomeOutCome;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


public interface StatisticWareHouseIncomeOutcomeRepository extends ReactiveCrudRepository<WareHouseIncomeOutCome, Long> {
    @Query(value = "SELECT COALESCE(SUM(CASE WHEN (group_import.type = 'EXPORT' OR group_import.type = 'SELLING') AND (local_time < :totime) THEN total_price * (1-discount_percent/100) - discount_amount + additional_fee END), 0) AS export_price_inside, COALESCE(SUM(CASE WHEN (group_import.type = 'EXPORT' OR group_import.type = 'SELLING') AND (local_time > :totime) THEN total_price * (1-discount_percent/100) - discount_amount + additional_fee END), 0) AS export_price_outside, COALESCE(SUM(CASE WHEN (group_import.type = 'IMPORT') AND (local_time < :totime) THEN total_price * (1-discount_percent/100) - discount_amount + additional_fee END), 0) AS import_price_inside, COALESCE(SUM(CASE WHEN (group_import.type = 'IMPORT') AND (local_time > :totime) THEN total_price * (1-discount_percent/100) - discount_amount + additional_fee END), 0) AS import_price_outside, COALESCE(SUM(CASE WHEN (group_import.type = 'EXPORT' OR group_import.type = 'SELLING') AND (local_time < :totime) THEN total_amount END), 0) AS export_amount_inside, COALESCE(SUM(CASE WHEN (group_import.type = 'EXPORT' OR group_import.type = 'SELLING') AND (local_time > :totime) THEN total_amount END), 0) AS export_amount_outside, COALESCE(SUM(CASE WHEN (group_import.type = 'IMPORT') AND (local_time < :totime) THEN total_amount END), 0) AS import_amount_inside, COALESCE(SUM(CASE WHEN (group_import.type = 'IMPORT') AND (local_time > :totime) THEN total_amount END), 0) AS import_amount_outside FROM (SELECT group_import.*, (group_import.createat) AS local_time FROM group_import WHERE group_import.group_id = :group_id AND group_import.status IS DISTINCT FROM :not_status AND (group_import.createat > :fromtime) ) AS group_import")
    Mono<WareHouseIncomeOutCome> getTotalStatictis(@Param("group_id") String groupID, @Param("fromtime") LocalDateTime from, @Param("totime") LocalDateTime to, @Param("not_status") ProductImport.Status not_status);
}
