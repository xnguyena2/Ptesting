package com.example.heroku.model.repository;

import com.example.heroku.model.PaymentTransation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface PaymentTransactionRepository extends ReactiveCrudRepository<PaymentTransation, String> {
}
