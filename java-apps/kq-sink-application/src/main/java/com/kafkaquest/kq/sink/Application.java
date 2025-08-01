package com.kafkaquest.kq.sink;

import com.kafkaquest.kq.common.util.config.Environment;
import com.kafkaquest.kq.common.util.models.events.enums.EventType;
import com.kafkaquest.kq.common.util.models.kafkakeys.AggregateKey;
import com.kafkaquest.kq.sink.config.appconfig.SinkAppConfig;
import com.kafkaquest.kq.sink.config.jdbi.JdbiConfig;
import com.kafkaquest.kq.common.util.serializer.AggregateKeyDeserializer;
import com.kafkaquest.kq.common.util.serializer.AggregateKeySerializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.apache.kafka.streams.kstream.Consumed;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.regex.Pattern;

import static com.kafkaquest.kq.common.util.utils.CommonUtils.deserializeAggregateEventToDomainEvent;

public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private static final EnumSet<EventType> excludedEventsFilter = EnumSet.of(
            EventType.EXCEPTIONAL_EVENT,
            EventType.DISCARD_GAME_EVENT,
            EventType.SAVE_CURRENT_STATE_EVENT
    );

    private static final String INSERT_QUERY =
            "INSERT INTO kq_events.domain_events (game_id, player_id, event_stream_id, event_type, timestamp, event_status, event_data) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static SinkAppConfig appConfig;

    private static Jdbi jdbi;

    public static void configureEnvironment(String... varArgs) {
        final var env = Arrays.asList(varArgs).contains(Environment.DOCKER.getValue())
                ? Environment.DOCKER
                : Environment.LOCAL;

        appConfig = new SinkAppConfig(env);

        Flyway flyway = Flyway.configure()
                .dataSource(
                    appConfig.getDbConnection(),
                    appConfig.getDbUser(),
                    appConfig.getDbPassword()
                )
                .driver("org.postgresql.Driver")
                .locations("classpath:db/migration")
                .defaultSchema("kq_events")
                .schemas("kq_events")
                .load();

        flyway.migrate();

        jdbi = JdbiConfig.createJdbiConnection(appConfig);
    }

    public static Topology getTopology() {

        Serde<AggregateKey> aggregateKeySerde = Serdes.serdeFrom(
            new AggregateKeySerializer(),
            new AggregateKeyDeserializer()
        );

        StreamsBuilder builder = new StreamsBuilder();

        builder
            .stream(
                Pattern.compile(appConfig.getSourceTopicRegex()),
                Consumed.with(aggregateKeySerde, Serdes.ByteArray())
            )
            .filterNot((key, value) -> excludedEventsFilter.contains(key.getEventType()))
            .foreach(Application::saveEvent);

        return builder.build();
    }

    private static void saveEvent(AggregateKey key, byte[] event) {
        final var eventDeserialized = deserializeAggregateEventToDomainEvent(event);
        jdbi.useHandle(handle ->
            handle.execute(
                INSERT_QUERY,
                eventDeserialized.getGameId(),
                eventDeserialized.getPlayerId(),
                eventDeserialized.getEventStreamId(),
                eventDeserialized.getEventType().name(),
                eventDeserialized.getTimestamp(),
                eventDeserialized.getEventStatus().name(),
                eventDeserialized.getEventData()
            )
        );
    }

    public static void main(String[] args) {
        configureEnvironment(args);

        Topology topology = getTopology();

        KafkaStreams kafkaStreams = new KafkaStreams(topology, appConfig.getKafkaProperties());
        kafkaStreams.setUncaughtExceptionHandler((e) -> {
            LOGGER.error(e.getMessage(), e);
            return StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.REPLACE_THREAD;
        });

        kafkaStreams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }
}
