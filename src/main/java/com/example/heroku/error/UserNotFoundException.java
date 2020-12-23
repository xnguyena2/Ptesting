package com.example.heroku.error;

public class UserNotFoundException  extends RuntimeException {

    public UserNotFoundException(String id) {
        super("User:" + id + " is not found.");
    }

}
