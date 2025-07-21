package com.kafkaquest.kq.sink.config.appconfig;

import com.kafkaquest.kq.common.util.config.AppConfig;
import com.kafkaquest.kq.common.util.config.Environment;
import com.kafkaquest.kq.common.util.exceptions.ConfigurationPropertyNotFoundException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@Slf4j(topic = "[SinkAppConfig]")
public class SinkAppConfig extends AppConfig<SinkApplicationKafkaPropertyKey> {

    private static final String CONFIG_PATH = "/config.properties";

    private final String sourceTopicRegex;

    private final String dbConnection;

    private final String dbUser;

    private final String dbPassword;

    public SinkAppConfig(Environment environment) {
        super(environment, SinkApplicationKafkaPropertyKey.class, CONFIG_PATH);
        this.sourceTopicRegex = this.getByPropertyKey(Topic.SOURCE_TOPIC_REGEX.getName());
        this.dbConnection = this.getByPropertyKey(Database.DB_CONNECTION.getName());
        this.dbUser = this.getByPropertyKey(Database.DB_USER.getName());
        this.dbPassword = this.getByPropertyKey(Database.DB_PASSWORD.getName());
        validateOtherConfigurations();
    }

    private void validateOtherConfigurations() {
        if (ObjectUtils.anyNull(this.sourceTopicRegex, this.dbConnection, this.dbUser, this.dbPassword)) {
            log.info("Invalid topic configuration property in {} environment", this.getEnvironment());
            throw new ConfigurationPropertyNotFoundException(
                "Some Configuration properties is missing from the environment. Properties: %s=%s, %s=%s, %s=%s, %s=%s"
                .formatted(
                    Topic.SOURCE_TOPIC_REGEX.name(),
                    this.sourceTopicRegex,
                    Database.DB_CONNECTION.getName(),
                    this.dbConnection,
                    Database.DB_USER.getName(),
                    this.dbUser,
                    Database.DB_PASSWORD.getName(),
                    this.dbPassword
                )
            );
        }
    }
}
