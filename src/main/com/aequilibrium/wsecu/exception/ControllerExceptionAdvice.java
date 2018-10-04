package com.aequilibrium.wsecu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    /**
     * Example of a simple exception handler message converter using a partial static builder
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public SimpleJsonError handleEntityNotFoundException(EntityNotFoundException ex) {
        return SimpleJsonError.fromException(ex).withStatus(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SimpleJsonError handleEntityValidationException(EntityValidationException ex) {
        return SimpleJsonError.fromException(ex).withStatus(HttpStatus.BAD_REQUEST);
    }
}
