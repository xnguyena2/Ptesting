package com.example.heroku.model.repository;

import com.example.heroku.model.PackageOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PackageOrderRepository  extends ReactiveCrudRepository<PackageOrder, String> {
}
