package com.kafkaquest.kq.common.util.config;

/**
 * Defines the environment used in the Application.
 * Enum value is used as command line arguments for deciding of the environment.
 * Currently LOCAL is the default.
 */
public enum Environment {
    LOCAL("local"),
    DOCKER("docker");

    private final String value;

    Environment(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
