package com.kafkaquest.kq.api.KESAApiService.exception;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException() {
        super();
    }

    public BadCredentialsException(String message) {
        super(message);
    }
}
