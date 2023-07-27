package com.example.heroku.services;

import com.example.heroku.model.repository.UserDeviceRepository;
import com.example.heroku.response.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@Component
public class UserDevice {
    @Autowired
    private UserDeviceRepository userDeviceRepository;

    public Mono<ResponseEntity<Format>> CreateUserDevice(@Valid @ModelAttribute com.example.heroku.model.UserDevice info) {
        return Mono.just(info)
                .map(com.example.heroku.model.UserDevice::AutoFill)
                .flatMap(userDevice ->
                        userDeviceRepository.saveDevice(userDevice.getGroup_id(), userDevice.getDevice_id(), userDevice.getUser_first_name(),
                                        userDevice.getUser_last_name(), userDevice.getStatus(), userDevice.getCreateat())
                                .then(Mono.just(userDevice))
                )
                .map(userDevice ->
                        ok(Format.builder().response(userDevice.getDevice_id()).build())
                );
    }

    public Mono<com.example.heroku.model.UserDevice> getDevice(String groupid, String id) {
        return userDeviceRepository.findByDeviceId(groupid, id);
    }

    public Mono<com.example.heroku.model.UserDevice> deleteDevice(String groupid, String id) {
        return userDeviceRepository.deleteByDeviceId(groupid, id);
    }

    public Flux<com.example.heroku.model.UserDevice> getAllDevice(String groupid, int page, int size) {
        return userDeviceRepository.findAllByGroupID(groupid, page, size);
    }
}
