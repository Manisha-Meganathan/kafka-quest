package com.kafkaquest.kq.api.KafkaQuestApiService.exception;

public class EmailDeliveryException extends RuntimeException {
    public EmailDeliveryException() {
        super();
    }

    public EmailDeliveryException(String message) {
        super(message);
    }

    public EmailDeliveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
