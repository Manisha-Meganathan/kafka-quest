package com.kafkaquest.kq.aggregate.config;

import com.kafkaquest.kq.common.util.exceptions.ConfigurationPropertyNotFoundException;
import com.kafkaquest.kq.common.util.config.AppConfig;
import com.kafkaquest.kq.common.util.config.Environment;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@Slf4j(topic = "[KQAggregateAppConfig]")
public class KQAggregateAppConfig extends AppConfig<AggregateKafkaPropertyKey> {

    private static final String CONFIG_PATH = "/config.properties";

    private final String sourceTopicRegex;

    private final String sinkTopic;

    private final String dbConnection;

    private final String dbUser;

    private final String dbPassword;

    public KQAggregateAppConfig(Environment environment) {
        super(environment, AggregateKafkaPropertyKey.class, CONFIG_PATH);
        this.sourceTopicRegex = this.getByPropertyKey(Topic.SOURCE_TOPIC_REGEX.getName());
        this.sinkTopic = this.getByPropertyKey(Topic.SINK_TOPIC.getName());
        this.dbConnection = this.getByPropertyKey(Database.DB_CONNECTION.getName());
        this.dbUser = this.getByPropertyKey(Database.DB_USER.getName());
        this.dbPassword = this.getByPropertyKey(Database.DB_PASSWORD.getName());
        validateOtherConfigurations();
    }

    private void validateOtherConfigurations() {
        if (ObjectUtils.anyNull(this.sourceTopicRegex, this.sinkTopic, this.dbConnection, this.dbUser, this.dbPassword)) {
            log.info("Invalid configuration property in {} environment", this.getEnvironment());
            throw new ConfigurationPropertyNotFoundException(
                "Configuration property is missing from the environment. properties: %s=%s, %s=%s, %s=%s, %s=%s, %s=%s"
                .formatted(
                    Topic.SOURCE_TOPIC_REGEX.name(),
                    this.sourceTopicRegex,
                    Topic.SINK_TOPIC.name(),
                    this.sinkTopic,
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
