package com.example.heroku.model.repository;

import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.model.statistics.BenifitByProduct;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.sql.Timestamp;

public interface StatisticBenifitOfProductRepository extends ReactiveCrudRepository<BenifitByProduct, Long> {
    @Query(value = "SELECT bn.*, product.name AS product_name, product_unit.name AS product_unit_name FROM (SELECT product_second_id, product_unit_second_id, COALESCE(SUM(price*number_unit*(1-discount_percent/100) - discount_amount), 0) AS revenue, COALESCE(SUM((price-buy_price)*number_unit*(1-discount_percent/100) - discount_amount), 0) AS profit, COALESCE(SUM(number_unit), 0) AS number_unit, MAX(user_package.createat) AS createat FROM user_package WHERE user_package.group_id = :group_id AND user_package.status = :status AND (user_package.createat AT TIME ZONE '+07' BETWEEN :fromtime AND :totime) GROUP BY product_second_id, product_unit_second_id LIMIT :size OFFSET (:page * :size)) AS bn LEFT JOIN (SELECT * FROM product WHERE group_id = :group_id) AS product ON bn.product_second_id = product.product_second_id LEFT JOIN (SELECT * FROM product_unit WHERE group_id = :group_id) AS product_unit ON bn.product_second_id = product_unit.product_second_id AND bn.product_unit_second_id = product_unit.product_unit_second_id")
    Flux<BenifitByProduct> getTotalStatictis(@Param("group_id") String groupID, @Param("fromtime") Timestamp from, @Param("totime") Timestamp to, @Param("status") UserPackageDetail.Status status, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT bn.*, product.name AS product_name, product_unit.name AS product_unit_name FROM (SELECT product_second_id, product_unit_second_id, COALESCE(SUM(price*number_unit*(1-discount_percent/100) - discount_amount), 0) AS revenue, COALESCE(SUM((price-buy_price)*number_unit*(1-discount_percent/100) - discount_amount), 0) AS profit, COALESCE(SUM(number_unit), 0) AS number_unit, MAX(user_package.createat) AS createat FROM (SELECT user_package_detail.package_second_id FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND user_package_detail.status = :status AND user_package_detail.device_id = :device_id AND (user_package_detail.createat AT TIME ZONE '+07' BETWEEN :fromtime AND :totime)) AS pd INNER JOIN (SELECT * FROM user_package WHERE group_id = :group_id) AS user_package ON user_package.package_second_id = pd.package_second_id GROUP BY product_second_id, product_unit_second_id ORDER BY number_unit DESC LIMIT :size OFFSET (:page * :size)) AS bn LEFT JOIN (SELECT * FROM product WHERE group_id = :group_id) AS product ON bn.product_second_id = product.product_second_id LEFT JOIN (SELECT * FROM product_unit WHERE group_id = :group_id) AS product_unit ON bn.product_second_id = product_unit.product_second_id AND bn.product_unit_second_id = product_unit.product_unit_second_id")
    Flux<BenifitByProduct> getTotalStatictisOfBuyer(@Param("group_id") String groupID, @Param("device_id") String device_id, @Param("fromtime") Timestamp from, @Param("totime") Timestamp to, @Param("status") UserPackageDetail.Status status, @Param("page")int page, @Param("size")int size);
}
