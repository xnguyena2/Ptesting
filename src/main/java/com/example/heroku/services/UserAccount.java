package com.example.heroku.services;

import com.example.heroku.model.Users;
import com.example.heroku.model.repository.ResultWithCountRepository;
import com.example.heroku.model.repository.UserRepository;
import com.example.heroku.request.beer.SearchQuery;
import com.example.heroku.request.beer.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class UserAccount {

    @Autowired
    ResultWithCountRepository resultWithCountRepository;

    @Autowired
    private UserRepository userRepository;

    public Mono<SearchResult<Users>> getAll(SearchQuery query) {
        final SearchQuery.Filter filter = query.GetFilter();
        final int page = query.getPage();
        final int size = query.getSize();
        return this.resultWithCountRepository.userCountAll()
                .map(resultWithCount -> {
                    SearchResult<Users> result = new SearchResult<>();
                    result.setCount(resultWithCount.getCount());
                    return result;
                })
                .flatMap(searchResult ->
                        Flux.just(searchResult).flatMap(rs ->
                                        this.userRepository.findByIdNotNull(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createat")))
                                )
                                .map(users -> {
                                    users.setPassword(null);
                                    return users;
                                })
                                .map(searchResult::Add)
                                .then(Mono.just(searchResult))
                );
    }
}
