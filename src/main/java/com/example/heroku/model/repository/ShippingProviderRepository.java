package com.example.heroku.model.repository;

import com.example.heroku.model.ShippingProvider;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ShippingProviderRepository extends ReactiveCrudRepository<ShippingProvider,String> {
    @Query(value = "DELETE FROM shipping_provider WHERE shipping_provider.provider_id = :id")
    Mono<ShippingProvider> deleteByProviderId(@Param("id")String id);

    @Query(value = "SELECT * FROM shipping_provider WHERE shipping_provider.provider_id = :id")
    Mono<ShippingProvider> findByProviderId(@Param("id")String id);
}
