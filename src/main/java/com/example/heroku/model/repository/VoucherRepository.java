package com.example.heroku.model.repository;

import com.example.heroku.model.Voucher;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface VoucherRepository extends ReactiveCrudRepository<Voucher, String> {
}
