package com.aequilibrium.wsecu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

public class SimpleJsonError {

    private final String message;
    private HttpStatus status;

    private SimpleJsonError(Exception ex) {
        this.message = ex.getMessage();
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public static SimpleJsonError fromException(Exception ex) {
        Assert.notNull(ex, "Supplied exception cannot be null");

        return new SimpleJsonError(ex);
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status.value();
    }

    public SimpleJsonError withStatus(HttpStatus status) {
        this.status = status;

        return this;
    }
}
