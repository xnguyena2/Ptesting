package com.example.heroku.model.repository;

import com.example.heroku.model.UserPaySoDi;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


public interface UserPaySoDiRepository extends ReactiveCrudRepository<UserPaySoDi, Long> {
    @Query(value = "SELECT * FROM user_pay_sodi WHERE user_pay_sodi.group_id = :group_id")
    Flux<UserPaySoDi> findByGroupId(@Param("group_id")String groupID);

    @Query(value = "INSERT INTO user_pay_sodi(group_id, amount, note, plan, bonus, createat) VALUES (:group_id, :amount, :note, :plan, :bonus, :createat)")
    Mono<UserPaySoDi> saveTransaction(@Param("group_id")String groupID, @Param("amount") float amount, @Param("note") String note, @Param("plan") String plan, @Param("bonus") int bonus, @Param("createat") LocalDateTime createat);

    @Query(value = "DELETE FROM user_pay_sodi WHERE user_pay_sodi.group_id = :group_id AND user_pay_sodi.id = :id")
    Mono<UserPaySoDi> deleteById(@Param("group_id")String groupID, @Param("id")long id);
}
