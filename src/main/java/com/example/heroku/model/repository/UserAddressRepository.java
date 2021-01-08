package com.example.heroku.model.repository;

import com.example.heroku.model.UserAddress;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserAddressRepository extends ReactiveCrudRepository<UserAddress, String> {

    @Query(value = "SELECT * FROM user_address WHERE user_address.device_id = :deviceid AND user_address.region = :region AND user_address.district = :district AND user_address.ward = :ward")
    Flux<UserAddress> findByDeviceID(@Param("deviceid")String deviceID, @Param("region")int region, @Param("district")int district, @Param("ward")int ward);

    @Query(value = "SELECT * FROM user_address WHERE user_address.device_id = :deviceid")
    Flux<UserAddress> findallByDeviceID(@Param("deviceid")String deviceID);

    @Query(value = "DELETE FROM user_address WHERE user_address.id = :id")
    Mono<UserAddress> deleteAddress(@Param("id")int id);
}
