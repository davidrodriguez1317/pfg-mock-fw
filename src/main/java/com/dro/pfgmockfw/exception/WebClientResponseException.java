package com.dro.pfgmockfw.exception;

public class WebClientResponseException extends RuntimeException{
    public WebClientResponseException(String message) {
        super(message);
    }

    public WebClientResponseException(String message, Throwable ex) {
        super(message, ex);
    }
}
