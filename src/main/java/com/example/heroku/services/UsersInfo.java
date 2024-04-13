package com.example.heroku.services;

import com.example.heroku.model.repository.UsersInfoRepository;
import com.example.heroku.request.beer.SearchQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Component
public class UsersInfo {
    @Autowired
    private UsersInfoRepository usersInfoRepository;

    public Mono<com.example.heroku.model.UsersInfo> createUserInfo(@Valid @ModelAttribute com.example.heroku.model.UsersInfo info) {
        return
                usersInfoRepository.deleteByUserName(info.getUsername())
                        .then(usersInfoRepository.save(info));
    }

    public Flux<com.example.heroku.model.UsersInfo> getAllUsersInfo(SearchQuery query) {
        return usersInfoRepository.findByIdNotNull(query.getGroup_id(), PageRequest.of(query.getPage(), query.getSize(), Sort.by(Sort.Direction.DESC, "createat")));
    }

    public Mono<com.example.heroku.model.UsersInfo> findByUsername(String username) {
        return usersInfoRepository.findByUsername(username);
    }

    public Mono<com.example.heroku.model.UsersInfo> deleteByUsername(String username) {
        return usersInfoRepository.deleteByUserName(username);
    }
}
