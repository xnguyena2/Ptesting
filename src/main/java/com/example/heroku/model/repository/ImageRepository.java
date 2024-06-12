package com.example.heroku.model.repository;

import com.example.heroku.model.Image;
import com.example.heroku.model.UserPackageDetail;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ImageRepository extends ReactiveCrudRepository<Image, Long> {

    @Query(value = "SELECT * FROM image WHERE image.category = :catetory AND image.group_id = :group_id")//, nativeQuery = true)
    Flux<Image> findByCategory(@Param("group_id")String group_id, @Param("catetory")String catetory);

    @Query(value = "SELECT * FROM image WHERE image.group_id = :group_id")//, nativeQuery = true)
    Flux<Image> findByGroupID(@Param("group_id")String group_id);

    @Query(value = "SELECT image.* FROM (SELECT * FROM product WHERE group_id = :group_id) AS product INNER JOIN (SELECT * FROM image WHERE group_id = :group_id) AS image ON image.category = product.product_second_id")//, nativeQuery = true)
    Flux<Image> getAllOfProduct(@Param("group_id")String group_id);

    @Query(value = "SELECT image.* FROM (SELECT DISTINCT user_package.product_second_id AS product_second_id FROM (SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND (status = :status OR status = :or_status) ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)) AS user_package_detail LEFT JOIN (SELECT * FROM user_package WHERE user_package.group_id = :group_id) AS user_package ON user_package_detail.package_second_id = user_package.package_second_id) AS product INNER JOIN (SELECT * FROM image WHERE group_id = :group_id) AS image ON image.category = product.product_second_id")
    Flux<Image> getAllOfProductByPackage(@Param("group_id") String group_id, @Param("status") UserPackageDetail.Status status, @Param("or_status") UserPackageDetail.Status or_status, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT image.* FROM (SELECT DISTINCT user_package.product_second_id AS product_second_id FROM (SELECT * FROM user_package_detail WHERE user_package_detail.group_id = :group_id AND (status = :status OR status = :or_status) AND id < :id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)) AS user_package_detail LEFT JOIN (SELECT * FROM user_package WHERE user_package.group_id = :group_id) AS user_package ON user_package_detail.package_second_id = user_package.package_second_id) AS product INNER JOIN (SELECT * FROM image WHERE group_id = :group_id) AS image ON image.category = product.product_second_id")
    Flux<Image> getAllOfProductByPackageAferID(@Param("group_id") String group_id, @Param("status") UserPackageDetail.Status status, @Param("or_status") UserPackageDetail.Status or_status, @Param("id") int id, @Param("page") int page, @Param("size") int size);

    @Query(value = "DELETE FROM image WHERE image.imgid = :imgid AND image.group_id = :group_id")
    Mono<Image> deleteImage(@Param("group_id")String group_id, @Param("imgid")String imgid);
}
