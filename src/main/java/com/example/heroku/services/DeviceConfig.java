package com.example.heroku.services;

import com.example.heroku.model.repository.DeviceConfigRepository;
import com.example.heroku.response.Format;
import com.example.heroku.model.entity.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@Component
public class DeviceConfig {
    @Autowired
    DeviceConfigRepository deviceConfigRepository;

    public Mono<ResponseEntity<Format>> UpdateConfig(@Valid @ModelAttribute com.example.heroku.model.DeviceConfig config) {
        return
                this.deviceConfigRepository.deleteByGroupID(config.getGroup_id())
                .then(Mono.just(config)
                        .map(BaseEntity::AutoFill)
                        .flatMap(deviceConfig ->
                                this.deviceConfigRepository.save(config)
                        )
                )
                .map(save ->
                        ok(Format.builder().build().setResponse(save.getId()))
                );
    }

    public Mono<com.example.heroku.model.DeviceConfig> GetConfig(String groupID) {
        return this.deviceConfigRepository.getConfig(groupID);
    }
}
