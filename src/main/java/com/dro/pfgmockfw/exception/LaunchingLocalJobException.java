package com.dro.pfgmockfw.exception;

public class LaunchingLocalJobException extends RuntimeException{
    public LaunchingLocalJobException(String message, Exception ex) {
        super(message, ex);
    }
}
