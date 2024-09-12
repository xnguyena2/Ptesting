package com.example.heroku.model.repository;

import com.example.heroku.model.DebtTransation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface DebtTransactionRepository extends ReactiveCrudRepository<DebtTransation, Long> {

    @Query(value = "INSERT INTO debt_transaction( group_id, transaction_second_id, device_id, action_id, action_type, transaction_type, amount, category, money_source, note, status, createat ) VALUES ( :group_id, :transaction_second_id, :device_id, :action_id, :action_type, :transaction_type, :amount, :category, :money_source, :note, :status, :createat ) ON CONFLICT (group_id, transaction_second_id) DO UPDATE SET device_id = :device_id, action_id = :action_id, action_type = :action_type, transaction_type = :transaction_type, amount = :amount, category = :category, money_source = :money_source, note = :note, status = :status, createat = :createat")
    Mono<DebtTransation> saveTransaction(@Param("group_id") String group_id, @Param("transaction_second_id") String transaction_second_id,
                                            @Param("device_id") String device_id,
                                            @Param("action_id") String action_id, @Param("action_type") DebtTransation.ActionType action_type,
                                            @Param("transaction_type") DebtTransation.TType transaction_type,
                                            @Param("amount") float amount, @Param("category") String category,
                                            @Param("money_source") String money_source, @Param("note") String note,
                                            @Param("status") DebtTransation.Status status, @Param("createat") Timestamp createat);

    @Query(value = "DELETE FROM debt_transaction WHERE debt_transaction.group_id = :group_id AND debt_transaction.transaction_second_id = :transaction_second_id")
    Mono<DebtTransation> deleteTransaction(@Param("group_id") String group_id, @Param("transaction_second_id") String transaction_second_id);

    @Query(value = "DELETE FROM debt_transaction WHERE debt_transaction.group_id = :group_id AND debt_transaction.action_id = :action_id AND debt_transaction.action_id IS NOT NULL")
    Mono<DebtTransation> deleteTransactionOfPackge(@Param("group_id") String group_id, @Param("action_id") String action_id);

    @Query(value = "SELECT * FROM debt_transaction WHERE debt_transaction.group_id = :group_id AND debt_transaction.action_id = :action_id")
    Flux<DebtTransation> getTransactionByPackageID(@Param("group_id") String group_id, @Param("action_id") String action_id);

    @Query(value = "SELECT DISTINCT ON (category) category FROM debt_transaction WHERE debt_transaction.group_id = :group_id")
    Flux<DebtTransation> getAllCategory(@Param("group_id") String group_id);

    @Query(value = "SELECT * FROM debt_transaction WHERE debt_transaction.group_id = :group_id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<DebtTransation> getAllDebt(@Param("group_id") String group_id, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM debt_transaction WHERE debt_transaction.group_id = :group_id AND id < :id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<DebtTransation> getAllDebtAfterID(@Param("group_id") String group_id, @Param("id") long id, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM debt_transaction WHERE debt_transaction.group_id = :group_id AND device_id = :device_id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<DebtTransation> getAllDebtOfBuyer(@Param("group_id") String group_id, @Param("device_id") String device_id, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM debt_transaction WHERE debt_transaction.group_id = :group_id AND device_id = :device_id AND id < :id ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<DebtTransation> getAllDebtAfterIDOfBuyer(@Param("group_id") String group_id, @Param("device_id") String device_id, @Param("id") long id, @Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT * FROM debt_transaction WHERE debt_transaction.group_id = :group_id AND (debt_transaction.createat AT TIME ZONE '+07' BETWEEN :fromtime AND :totime)")
    Flux<DebtTransation> getStatictis(@Param("group_id") String groupID, @Param("fromtime") Timestamp from, @Param("totime") Timestamp to);

}
