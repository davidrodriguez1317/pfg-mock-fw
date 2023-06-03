package com.dro.pfgmockfw.exception;

public class EnumDoesNotExistException extends RuntimeException{
    public EnumDoesNotExistException(String message) {
        super(message);
    }
}
