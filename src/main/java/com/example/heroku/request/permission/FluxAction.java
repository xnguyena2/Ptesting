package com.example.heroku.request.permission;

import reactor.core.publisher.Flux;

public interface FluxAction<T> {
    Flux<T> action();
}
