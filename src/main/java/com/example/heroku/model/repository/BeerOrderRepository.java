package com.example.heroku.model.repository;

import com.example.heroku.model.BeerOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BeerOrderRepository extends ReactiveCrudRepository<BeerOrder, String> {

}
