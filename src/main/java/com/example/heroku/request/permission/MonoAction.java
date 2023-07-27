package com.example.heroku.request.permission;

import reactor.core.publisher.Mono;

public interface MonoAction<T> {
    Mono<T> action();
}
