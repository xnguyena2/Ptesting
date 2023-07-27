package com.example.heroku.request.permission;

import com.example.heroku.request.beer.SearchQuery;
import reactor.core.publisher.Flux;

public interface DoQueryFlux<T> {
    Flux<T> doQueryFlux(SearchQuery query);
}
