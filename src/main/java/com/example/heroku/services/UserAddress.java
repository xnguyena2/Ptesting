package com.example.heroku.services;

import com.example.heroku.model.repository.UserAddressRepository;
import com.example.heroku.response.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@Component
public class UserAddress {

    @Autowired
    UserAddressRepository userAddressRepository;

    public Mono<ResponseEntity<Format>> CreateAddress(com.example.heroku.model.UserAddress address) {
        address.setStatus(com.example.heroku.model.UserAddress.Status.FAIL);
        return userAddressRepository.findByDeviceID(address.getGroup_id(), address.getDevice_id(), address.getRegion(), address.getDistrict(), address.getWard())
                .filter(userAddress -> userAddress.getHouse_number().equals(address.getHouse_number()))
                .switchIfEmpty(
                        userAddressRepository.save(address.changeStatus(com.example.heroku.model.UserAddress.Status.SUCCESS).AutoFill())
                )
                .then(Mono.just(address))
                .flatMap(useraddress -> {
                    if (useraddress.getStatus() == com.example.heroku.model.UserAddress.Status.SUCCESS)
                        return Mono.just(ok(Format.builder().response("good").build()));
                    else
                        return Mono.just(badRequest().body(Format.builder().response("user package empty").build()));
                });
    }

    public Flux<com.example.heroku.model.UserAddress> GetUserAddress(String groupid, String deviceID) {
        return userAddressRepository.findallByDeviceID(groupid, deviceID);
    }

    public Mono<com.example.heroku.model.UserAddress> DeleteAddress(String groupid, String id) {
        return userAddressRepository.deleteAddress(groupid, id);
    }

    public Mono<com.example.heroku.model.UserAddress> UpdateAddress(com.example.heroku.model.UserAddress address) {
        return userAddressRepository.deleteAddress(address.getGroup_id(), address.getAddress_id())
                .then(userAddressRepository.save(address.Clean()));
    }
}
