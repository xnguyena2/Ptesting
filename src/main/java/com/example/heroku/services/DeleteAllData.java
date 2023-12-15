package com.example.heroku.services;

import com.example.heroku.model.Users;
import com.example.heroku.model.repository.DeleteRepository;
import com.example.heroku.model.repository.StoreManagementRepository;
import com.example.heroku.model.repository.UserRepository;
import com.example.heroku.request.client.UserID;
import org.springframework.beans.factory.annotation.Autowired;
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
        return userRepository.findByUsername(username).flatMap(users -> deleteByGroupID(users.getGroup_id())
                .then(Mono.just(users))
        );
    }

    public Mono<Boolean> deleteByGroupID(String groupID) {
        return imageAPI.JustDeleteImageFromFlickByGroupID(groupID)
                .then(deleteRepository.deleteByGroupId(groupID));
    }
}
