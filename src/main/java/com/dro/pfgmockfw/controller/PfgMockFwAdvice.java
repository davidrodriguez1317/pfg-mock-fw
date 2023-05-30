package com.dro.pfgmockfw.controller;

import com.dro.pfgmockfw.exception.WebClientResponseException;
import com.dro.pfgmockfw.exception.WebClientTechnicalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientRequestException;

@ControllerAdvice
@Slf4j
public class PfgMockFwAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        String error = "Validation failed: " + ex.getBindingResult().getAllErrors();
        log.error(error, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        String error = "The param '" + ex.getName() + "' has an invalid type. Expected one: " +
                ex.getRequiredType().getSimpleName() + ".";
        log.error(error, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(WebClientTechnicalException.class)
    public ResponseEntity<String> handleWebClientTechnicalException(
            WebClientTechnicalException ex) {
        String error = "There was a server error. Message: ".concat(ex.getMessage());
        log.error(error, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(
            WebClientResponseException ex) {
        String error = "There was a client error calling the server. Message: ".concat(ex.getMessage());
        log.error(error, ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<String> handleWebClientRequestException(
            WebClientRequestException ex) {
        String error = "Docker/Nomad is not available. Message: ".concat(ex.getMessage());
        log.error(error, ex);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        String error = "An error occurred in the server";
        log.error(error, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
