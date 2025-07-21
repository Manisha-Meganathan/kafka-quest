package com.kafkaquest.kq.aggregate.services;

import com.kafkaquest.kq.aggregate.config.KesaAggregateAppConfig;
import org.jdbi.v3.core.Jdbi;

public class JdbiConnection {

    private static volatile Jdbi jdbiInstance = null;

    private JdbiConnection() {
    }

    public static void initialize(KesaAggregateAppConfig appConfig) {
        if (jdbiInstance == null) {
            synchronized (JdbiConnection.class) {
                if (jdbiInstance == null) {
                    jdbiInstance = Jdbi.create(
                        appConfig.getDbConnection(),
                        appConfig.getDbUser(),
                        appConfig.getDbPassword()
                    );
                }
            }
        }
    }

    public static Jdbi getInstance() {
        if (jdbiInstance == null) {
            synchronized (JdbiConnection.class) {
                if (jdbiInstance == null) {
                    throw new IllegalStateException("JDBI Connection is not initialized");
                }
            }
        }
        return jdbiInstance;
    }
}
