package com.example.heroku.error;

public class EmptyException extends RuntimeException {
    public EmptyException() {
        super("Not found in table!.");
    }
}
