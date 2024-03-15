package com.example.heroku.model.repository;

import com.example.heroku.model.PaymentTransation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface PaymentTransactionRepository extends ReactiveCrudRepository<PaymentTransation, Long> {

    @Query(value = "INSERT INTO payment_transaction( group_id, transaction_second_id, device_id, package_second_id, transaction_type, amount, category, money_source, note, status, createat ) VALUES ( :group_id, :transaction_second_id, :device_id, :package_second_id, :transaction_type, :amount, :category, :money_source, :note, :status, :createat ) ON CONFLICT (group_id, transaction_second_id) DO UPDATE SET device_id = :device_id, package_second_id = :package_second_id, transaction_type = :transaction_type, amount = :amount, category = :category, money_source = :money_source, note = :note, status = :status, createat = :createat")
    Mono<PaymentTransation> saveTransaction(@Param("group_id") String group_id, @Param("transaction_second_id") String transaction_second_id,
                              @Param("device_id") String device_id, @Param("package_second_id") String package_second_id, @Param("transaction_type") PaymentTransation.TType transaction_type,
                              @Param("amount") float amount, @Param("category") String category,
                              @Param("money_source") String money_source, @Param("note") String note,
                              @Param("status") PaymentTransation.Status status, @Param("createat") Timestamp createat);

    @Query(value = "DELETE FROM payment_transaction WHERE payment_transaction.group_id = :group_id AND payment_transaction.transaction_second_id = :transaction_second_id")
    Mono<PaymentTransation> deleteTransaction(@Param("group_id") String group_id, @Param("transaction_second_id") String transaction_second_id);

    @Query(value = "DELETE FROM payment_transaction WHERE payment_transaction.group_id = :group_id AND payment_transaction.package_second_id = :package_second_id AND payment_transaction.package_second_id IS NOT NULL")
    Mono<PaymentTransation> deleteTransactionOfPackge(@Param("group_id") String group_id, @Param("package_second_id") String package_second_id);

    @Query(value = "SELECT * FROM payment_transaction WHERE payment_transaction.group_id = :group_id AND payment_transaction.package_second_id = :package_second_id")
    Flux<PaymentTransation> getTransactionByPackageID(@Param("group_id") String group_id, @Param("package_second_id") String package_second_id);

    @Query(value = "SELECT DISTINCT ON (category) category FROM payment_transaction WHERE payment_transaction.group_id = :group_id")
    Flux<PaymentTransation> getAllCategory(@Param("group_id") String group_id);

    @Query(value = "SELECT * FROM payment_transaction WHERE payment_transaction.group_id = :group_id AND (payment_transaction.createat AT TIME ZONE '+07' BETWEEN :fromtime AND :totime)")
    Flux<PaymentTransation> getStatictis(@Param("group_id") String groupID, @Param("fromtime") Timestamp from, @Param("totime") Timestamp to);
}
