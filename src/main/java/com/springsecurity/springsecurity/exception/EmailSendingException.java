package com.springsecurity.springsecurity.exception;

public class EmailSendingException extends RuntimeException{
        public EmailSendingException(String message) {
            super(message);
        }
}
