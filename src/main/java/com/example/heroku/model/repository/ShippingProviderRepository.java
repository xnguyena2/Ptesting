package com.example.heroku.model.repository;

import com.example.heroku.model.ShippingProvider;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ShippingProviderRepository extends ReactiveCrudRepository<ShippingProvider, Long> {
    @Query(value = "DELETE FROM shipping_provider WHERE shipping_provider.group_id = :group_id AND shipping_provider.provider_id = :id")
    Mono<ShippingProvider> deleteByProviderId(@Param("group_id")String group_id, @Param("id")String id);

    @Query(value = "SELECT * FROM shipping_provider WHERE shipping_provider.group_id = :group_id AND shipping_provider.provider_id = :id")
    Mono<ShippingProvider> findByProviderId(@Param("group_id")String group_id, @Param("id")String id);
}
