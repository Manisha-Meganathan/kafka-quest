package com.kafkaquest.kq.aggregate.exceptions;

public class JigsawPiecesNotFoundException extends RuntimeException {
    public JigsawPiecesNotFoundException() {
    }

    public JigsawPiecesNotFoundException(String message) {
        super(message);
    }
}
