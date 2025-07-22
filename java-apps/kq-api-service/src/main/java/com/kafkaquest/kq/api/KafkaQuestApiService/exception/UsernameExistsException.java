package com.kafkaquest.kq.api.KafkaQuestApiService.exception;

public class UsernameExistsException extends RuntimeException {
    public UsernameExistsException() {
        super();
    }

    public UsernameExistsException(String message) {
        super(message);
    }
}
