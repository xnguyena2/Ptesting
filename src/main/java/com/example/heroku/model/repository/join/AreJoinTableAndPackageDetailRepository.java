package com.example.heroku.model.repository.join;

import com.example.heroku.model.joinwith.AreJoinTable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface AreJoinTableAndPackageDetailRepository extends ReactiveCrudRepository<AreJoinTable, Long> {
    @Query(value = "SELECT area.*, table_detail.area_id AS child_area_id, table_detail.table_id AS child_table_id, table_detail.package_second_id AS child_package_second_id, table_detail.table_name AS child_table_name, table_detail.detail AS child_detail, table_detail.status AS child_status, user_package_detail.price AS child_price FROM (SELECT * FROM area WHERE group_id = :group_id) AS area LEFT JOIN (SELECT * FROM table_detail WHERE group_id = :group_id) AS table_detail ON area.area_id = table_detail.area_id LEFT JOIN (SELECT * FROM user_package_detail WHERE group_id = :group_id) AS user_package_detail ON user_package_detail.package_second_id = table_detail.package_second_id")
    Flux<AreJoinTable> findByGroupID(@Param("group_id")String groupID);

    @Query(value = "SELECT area.*, table_detail.area_id AS child_area_id, table_detail.table_id AS child_table_id, table_detail.package_second_id AS child_package_second_id, table_detail.table_name AS child_table_name, table_detail.detail AS child_detail, table_detail.status AS child_status, user_package_detail.price AS child_price FROM (SELECT * FROM area WHERE group_id = :group_id AND area_id = :area_id) AS area LEFT JOIN (SELECT * FROM table_detail WHERE group_id = :group_id) AS table_detail ON area.area_id = table_detail.area_id LEFT JOIN (SELECT * FROM user_package_detail WHERE group_id = :group_id) AS user_package_detail ON user_package_detail.package_second_id = table_detail.package_second_id")
    Flux<AreJoinTable> findByID(@Param("group_id")String groupID, @Param("area_id")String areID);

}
