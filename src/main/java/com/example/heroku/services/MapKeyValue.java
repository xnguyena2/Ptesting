package com.example.heroku.services;

import com.example.heroku.model.repository.MapKeyValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MapKeyValue {

    @Autowired
    MapKeyValueRepository mapKeyValueRepository;


    public Mono<com.example.heroku.model.MapKeyValue> insert(com.example.heroku.model.MapKeyValue mapKeyValue) {
        mapKeyValue.AutoFill();
        return mapKeyValueRepository.insert(mapKeyValue.getGroup_id(), mapKeyValue.getId_o(), mapKeyValue.getValue_o(), mapKeyValue.getCreateat()).then(Mono.just(mapKeyValue));
    }

    public Mono<com.example.heroku.model.MapKeyValue> getByID(String groupID, String id) {
        return this.mapKeyValueRepository.get(groupID, id);
    }

    public Mono<com.example.heroku.model.MapKeyValue> deleteByID(String groupID, String id) {
        return mapKeyValueRepository.deleteByID(groupID, id);
    }
}
