package com.kafkaquest.kq.sink.config.jdbi;

import com.kafkaquest.kq.sink.config.appconfig.SinkAppConfig;
import org.jdbi.v3.core.Jdbi;

public class JdbiConfig {

    public static Jdbi createJdbiConnection(SinkAppConfig appConfig) {
        return Jdbi.create(
            appConfig.getDbConnection(),
            appConfig.getDbUser(),
            appConfig.getDbPassword()
        );
    }
}
