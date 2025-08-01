package com.kafkaquest.kq.common.util.exceptions;

public class PropertiesFileLoadingException extends RuntimeException {
    public PropertiesFileLoadingException() {
    }

    public PropertiesFileLoadingException(String message) {
        super(message);
    }
}
