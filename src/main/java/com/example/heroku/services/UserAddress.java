package com.example.heroku.services;

import com.example.heroku.model.repository.UserAddressRepository;
import com.example.heroku.response.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.ok;

@Component
public class UserAddress {

    @Autowired
    UserAddressRepository userAddressRepository;

    public Mono<Object> CreateAddress(com.example.heroku.model.UserAddress address) {
        address.setStatus(com.example.heroku.model.UserAddress.Status.FAIL);
        return userAddressRepository.findByDeviceID(address.getDevice_id(), address.getRegion(), address.getDistrict(), address.getWard())
                .filter(userAddress -> userAddress.getHouse_number().equals(address.getHouse_number()))
                .switchIfEmpty(
                        userAddressRepository.save(address.changeStatus(com.example.heroku.model.UserAddress.Status.SUCCESS).AutoFill())
                )
                .then(Mono.just(address))
                .map(useraddress -> {
                    if (useraddress.getStatus() == com.example.heroku.model.UserAddress.Status.SUCCESS)
                        return Mono.just(ok(Format.builder().response("good").build()));
                    else
                        return Mono.just(org.springframework.http.ResponseEntity.badRequest());
                });
    }

    public Flux<com.example.heroku.model.UserAddress> GetUserAddress(String deviceID) {
        return userAddressRepository.findallByDeviceID(deviceID);
    }

    public Mono<com.example.heroku.model.UserAddress> DeleteAddress(int id) {
        return userAddressRepository.deleteAddress(id);
    }

    public Mono<com.example.heroku.model.UserAddress> UpdateAddress(com.example.heroku.model.UserAddress address) {
        return userAddressRepository.deleteAddress(Integer.parseInt(address.getId()))
                .then(Mono.just(address))
                .flatMap(address1 -> {
                    address.setId(null);
                    return userAddressRepository.save(address);
                });
    }
}
