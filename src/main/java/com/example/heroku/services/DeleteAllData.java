package com.example.heroku.services;

import com.example.heroku.model.Users;
import com.example.heroku.model.repository.DeleteRepository;
import com.example.heroku.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DeleteAllData {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Image imageAPI;

    @Autowired
    private DeleteRepository deleteRepository;

    public Mono<Users> seftMarkDelete(String username) {
        return userRepository.findByUsername(username)
                .filter(users -> users.getGroup_id().endsWith("_" + users.getUsername()))
                .switchIfEmpty(Mono.error(new AccessDeniedException("403 returned1")))
                .flatMap(users -> deleteByGroupID(users.getGroup_id())
                        .then(Mono.just(users))
                );
    }

    Mono<Boolean> deleteByGroupID(String groupID) {
        return imageAPI.JustDeleteImageFromFlickByGroupID(groupID)
                .then(deleteRepository.deleteByGroupId(groupID));
    }
}
