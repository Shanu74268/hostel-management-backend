package com.springsecurity.springsecurity.exception;

import org.aspectj.bridge.IMessage;

public class UserAlreadyVerifiedException extends RuntimeException{
    public UserAlreadyVerifiedException(String message){
        super(message);
    }
}
