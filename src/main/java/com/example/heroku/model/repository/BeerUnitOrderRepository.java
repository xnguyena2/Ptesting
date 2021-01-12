package com.example.heroku.model.repository;

import com.example.heroku.model.BeerUnitOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BeerUnitOrderRepository extends ReactiveCrudRepository<BeerUnitOrder, String> {
}
