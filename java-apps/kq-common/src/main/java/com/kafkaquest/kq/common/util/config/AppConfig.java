package com.kafkaquest.kq.common.util.config;

import com.kafkaquest.kq.common.util.exceptions.ConfigurationPropertyNotFoundException;
import com.kafkaquest.kq.common.util.exceptions.InvalidPathException;
import com.kafkaquest.kq.common.util.exceptions.PropertiesFileLoadingException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 *  A class that helps to maps the external configurations used in the kafka stream application
 *  Works as the central point for all the external configurations used throughout the application
 */

@Getter
@ToString
@EqualsAndHashCode
@Slf4j(topic = "[AppConfig]")
public abstract class AppConfig <T extends Enum<T> & KafkaPropertyKey> {

    private final String configPath;

    private final Environment environment;

    private final Class<T> kafkaPropertyKeyEnum;

    private boolean isPropertiesLoaded;

    private Map<String, String> allPropertiesCache;

    public AppConfig(Environment environment, Class<T> kafkaPropertyKeyEnum, String configPath) {
        if (!configPath.startsWith("/")) {
            throw new InvalidPathException(
                "Leading froward slash not found in config path: %s".formatted(configPath)
            );
        }
        this.configPath = configPath;
        this.environment = environment;
        this.kafkaPropertyKeyEnum = kafkaPropertyKeyEnum;
        this.isPropertiesLoaded = false;
    }

    @Nonnull
    public Properties getKafkaProperties() {
        Map<String, String> properties = this.getAllProperties();

        log.info("Started adding kafka properties to application in {} environment", this.environment);
        Properties kafkaProperties = new Properties();
        for (T key : kafkaPropertyKeyEnum.getEnumConstants()) {
            String propertyKey = key.getKafkaPropertyKey();
            String propertyValue = properties.get(key.getKafkaPropertyKey());

            if (Objects.isNull(propertyValue)) {
                throw new ConfigurationPropertyNotFoundException(
                    "Configuration property: %s does not exist in the environment".formatted(key.name())
                );
            }
            kafkaProperties.put(propertyKey, propertyValue);
            log.info("Added {} property with value {} to application.", propertyKey, propertyValue);
        }
        log.info("Completed adding kafka properties to application in {} environment", this.environment);
        return kafkaProperties;
    }

    @Nullable
    public String getByPropertyKey(String key) {
        return this.getAllProperties().get(key);
    }

    @Nonnull
    public Map<String, String> getAllProperties() {
        this.loadPropertiesByEnv();
        return this.allPropertiesCache;
    }

    private void loadPropertiesByEnv() {
        if (!this.isPropertiesLoaded) {
            if (this.environment == Environment.DOCKER) {
                this.allPropertiesCache = getPropertiesFromDockerEnv();
            } else {
                this.allPropertiesCache = getPropertiesFromPropertyFile();
            }
            this.isPropertiesLoaded = true;
        }
    }

    /**
     * Method to get all the properties from the Docker environment.
     * Note: Assumes Kafka properties in the docker env variables are prefixed
     * with KAFKA_ and named in capital snake case, where underscore(_) is used for
     * group separation rather than a dot(.). And following method will format those keys.
     * This is to follow docker environment variable naming practices.
     * Other constants passed through are only converted to lowercase
     */
    @Nonnull
    private Map<String, String> getPropertiesFromDockerEnv() {
        final var envVars = System.getenv();
        final var kafkaProps = new HashMap<String, String>();

        for (Map.Entry<String, String> entry : envVars.entrySet()) {
            String key = entry.getKey();
            if (entry.getKey().startsWith("KAFKA")) {
                // transforming kafka properties to correct key format
                key = key
                    .replaceFirst("^KAFKA_(.*)", "$1")
                    .replaceAll("[_]", ".");
            }
            // converting other constants to lower snake case
            key = key.toLowerCase();
            kafkaProps.put(key, entry.getValue());
        }
        // Constant keys are in lower snake case at this point.
        // This is to keep the consistency with .properties files
        // where the practice is to keep properties in lower snake case
        return kafkaProps;
    }

    @Nonnull
    private Map<String, String> getPropertiesFromPropertyFile() {
        final Properties propertiesFromFile = new Properties();
        try (InputStream input = getClass().getResourceAsStream(this.configPath)) {
            if (input != null) {
                propertiesFromFile.load(input);
                log.info("Properties file loaded successfully.");
                return getPropertiesAsMap(propertiesFromFile);
            } else {
                throw new PropertiesFileLoadingException(
                    "Properties file not found: %s".formatted(this.configPath)
                );
            }
        } catch (IOException ex) {
            log.error("Error loading properties file: {}", ex.getMessage(), ex);
            throw new PropertiesFileLoadingException(
                "Exception occurred while loading properties file: %s".formatted(this.configPath)
            );
        }
    }

    @Nonnull
    private Map<String, String> getPropertiesAsMap(Properties properties) {
        return properties
                .entrySet()
                .stream()
                .collect(
                    Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        entry -> entry.getValue().toString()
                    )
                );
    }
}
