package com.example.heroku.services;

import com.example.heroku.model.repository.DeviceConfigRepository;
import com.example.heroku.response.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@Component
public class DeviceConfig {
    @Autowired
    DeviceConfigRepository deviceConfigRepository;

    public Mono<Object> UpdateConfig(@Valid @ModelAttribute com.example.heroku.model.DeviceConfig config) {
        return
                this.deviceConfigRepository.deleteAll()
                .then(Mono.just(config)
                        .flatMap(deviceConfig ->
                                this.deviceConfigRepository.save(config)
                        )
                )
                .map(save ->
                        ok(Format.builder().response(save.getId()).build())
                );
    }

    public Mono<com.example.heroku.model.DeviceConfig> GetConfig() {
        return this.deviceConfigRepository.getConfig();
    }
}
