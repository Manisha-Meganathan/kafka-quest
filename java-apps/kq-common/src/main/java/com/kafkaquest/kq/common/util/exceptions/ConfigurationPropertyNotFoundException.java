package com.kafkaquest.kq.common.util.exceptions;

public class ConfigurationPropertyNotFoundException extends RuntimeException {
    public ConfigurationPropertyNotFoundException() {
    }

    public ConfigurationPropertyNotFoundException(String message) {
        super(message);
    }
}
