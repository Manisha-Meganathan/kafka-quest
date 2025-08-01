package com.kafkaquest.kq.common.util.exceptions;

public class DeserializationException extends RuntimeException {
    public DeserializationException() {
        super();
    }

    public DeserializationException(String message) {
        super(message);
    }
}
