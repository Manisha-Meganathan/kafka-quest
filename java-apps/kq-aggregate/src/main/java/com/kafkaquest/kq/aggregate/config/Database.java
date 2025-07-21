package com.kafkaquest.kq.aggregate.config;

public enum Database {

    DB_CONNECTION("db_connection"),
    DB_USER("db_user"),
    DB_PASSWORD("db_password");

    private final String name;

    Database(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
