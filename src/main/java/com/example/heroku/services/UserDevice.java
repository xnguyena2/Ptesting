package com.example.heroku.services;

import com.example.heroku.model.repository.UserDeviceRepository;
import com.example.heroku.response.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@Component
public class UserDevice {
    @Autowired
    private UserDeviceRepository userDeviceRepository;

    public Mono<Object> CreateUserDevice(@Valid @ModelAttribute com.example.heroku.model.UserDevice info) {
        return Mono.just(info)
                .map(userDevice -> {
                    if(userDevice.getDevice_id() == "" || userDevice.getDevice_id() == null)
                        return null;
                    return userDevice;
                })
                .flatMap(userDevice ->
                        userDeviceRepository.findByDeviceId(userDevice.getDevice_id())
                                .switchIfEmpty(userDeviceRepository.save(userDevice.AutoFill()))
                )
                .map(userDevice ->
                    ok(Format.builder().response(userDevice.getDevice_id()).build())
                );
    }
}
