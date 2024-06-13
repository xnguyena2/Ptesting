package com.example.heroku.client.payment;

public interface FilterAction<T> {
    T action(T value);
}