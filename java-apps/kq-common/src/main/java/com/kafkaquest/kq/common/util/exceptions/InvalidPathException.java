package com.kafkaquest.kq.common.util.exceptions;

public class InvalidPathException extends RuntimeException {
    public InvalidPathException() {
    }

    public InvalidPathException(String message) {
        super(message);
    }
}
