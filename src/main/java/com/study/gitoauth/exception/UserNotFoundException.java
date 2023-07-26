package com.study.gitoauth.exception;

public class UserNotFoundException extends IllegalArgumentException{

    public UserNotFoundException() {
        super("USER NOT FOUND");
    }
}
