package com.example.heroku.services;

import com.example.heroku.model.repository.DeleteRequestRepository;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.status.ActiveStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DeleteRequest {

    @Autowired
    DeleteRequestRepository deleteRequestRepository;


    public Mono<com.example.heroku.model.DeleteRequest> createRequest(String userID) {
        com.example.heroku.model.DeleteRequest request = com.example.heroku.model.DeleteRequest.builder()
                .user_id(userID)
                .status(ActiveStatus.ACTIVE)
                .build();
        request.AutoFill("group_id");
        return deleteRequestRepository.save(request).then(Mono.just(request));
    }

    public Flux<com.example.heroku.model.DeleteRequest> getAllRequest(SearchQuery query) {
        return this.deleteRequestRepository.findByIdNotNull(query.getPage(), query.getSize());
    }

    public Mono<com.example.heroku.model.DeleteRequest> deleteByID(String id) {
        return this.deleteRequestRepository.deleteByID(id);
    }
}
