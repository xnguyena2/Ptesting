package com.example.heroku.request.permission;

import com.example.heroku.request.beer.SearchQuery;
import reactor.core.publisher.Mono;

public interface DoQueryMono<T> {
    Mono<T> doQueryMono(SearchQuery query);
}
