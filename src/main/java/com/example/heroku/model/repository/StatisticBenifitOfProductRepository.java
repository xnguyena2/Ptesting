package com.example.heroku.model.repository;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.statistics.BenifitByProduct;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.sql.Timestamp;

public interface StatisticBenifitOfProductRepository extends ReactiveCrudRepository<BenifitByProduct, Long> {
    @Query(value = "SELECT product_second_id, product_unit_second_id, COALESCE(SUM(price*number_unit*(1-discount_percent/100) - discount_amount), 0) AS revenue, COALESCE(SUM(number_unit), 0) AS number_unit FROM user_package WHERE user_package.group_id = :group_id AND user_package.status = :status AND (user_package.createat AT TIME ZONE '+07' BETWEEN :fromtime AND :totime) GROUP BY product_second_id, product_unit_second_id")
    Flux<BenifitByProduct> getTotalStatictis(@Param("group_id") String groupID, @Param("fromtime") Timestamp from, @Param("totime") Timestamp to, @Param("status") UserPackageDetail.Status status);
}
