package com.example.heroku.services;

import com.example.heroku.model.UserPackageDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserPackageDetailCounterServices {
    @Autowired
    com.example.heroku.model.repository.counter.CounterUserPackageDetailNumberOfProcessingAndWeb api;

    public Mono<com.example.heroku.response.CounterUserPackageDetailNumberOfProcessingAndWeb> count(String groupID) {
        return api.counterPackage(groupID, UserPackageDetail.Status.CREATE, UserPackageDetail.Status.WEB_SUBMIT);
    }
}
