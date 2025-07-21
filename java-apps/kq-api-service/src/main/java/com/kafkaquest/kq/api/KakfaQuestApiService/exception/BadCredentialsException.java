package com.kafkaquest.kq.api.KafkaQuestApiService.exception;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException() {
        super();
    }

    public BadCredentialsException(String message) {
        super(message);
    }
}
