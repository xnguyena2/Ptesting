package com.example.heroku.model.repository;

import com.example.heroku.model.BeerViewCount;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BeerViewCountRepository extends ReactiveCrudRepository<BeerViewCount, String> {
}
