package com.varkaikin.passwordwallet.exceptions;

public class LoginExistsException extends RuntimeException {
    public LoginExistsException(String s) {
        super(s);

    }
}
