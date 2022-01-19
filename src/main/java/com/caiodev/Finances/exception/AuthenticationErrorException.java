package com.caiodev.Finances.exception;

public class AuthenticationErrorException extends RuntimeException{

    public AuthenticationErrorException(String msg){
        super(msg);
    }
}
