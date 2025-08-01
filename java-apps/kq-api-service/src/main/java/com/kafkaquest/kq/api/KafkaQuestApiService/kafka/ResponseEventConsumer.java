package com.kafkaquest.kq.api.KafkaQuestApiService.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kafkaquest.kq.api.KafkaQuestApiService.config.kafka.KafkaTopicProperties;
import com.kafkaquest.kq.api.KafkaQuestApiService.service.WebSocketMessagingService;
import com.kafkaquest.kq.common.util.models.events.responsedata.ExceptionalEventResponseData;
import com.kafkaquest.kq.common.util.models.events.responsedata.GameDiscardEventResponseData;
import com.kafkaquest.kq.common.util.models.events.responsedata.GameStartedEventResponseData;
import com.kafkaquest.kq.common.util.models.events.responsedata.PieceAddedEventResponseData;
import com.kafkaquest.kq.common.util.models.events.responsedata.PieceRemovedEventResponseData;
import com.kafkaquest.kq.common.util.models.events.ProjectionEventResponse;
import com.kafkaquest.kq.common.util.models.events.responsedata.ResponseData;
import com.kafkaquest.kq.common.util.models.events.enums.EventType;
import com.kafkaquest.kq.common.util.models.kafkakeys.ProjectionKey;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.kafkaquest.kq.api.KafkaQuestApiService.config.websocket.WebSocketTopics.EVENT_RESPONSE;

@Component
@AllArgsConstructor
@Slf4j(topic = "[ResponseEventConsumer]")
public class ResponseEventConsumer {

    private final KafkaTopicProperties kafkaTopicProperties;

    private final WebSocketMessagingService messagingService;

    @KafkaListener(
        topics = "#{kafkaTopicProperties.getTopics().get('consumer-topic')}",
        groupId = "#{'${spring.kafka.consumer.group-id}'}",
        containerFactory = "kafkaEventListenerContainerFactory"
    )
    public void consumeProjectionEventResponse(ConsumerRecord<ProjectionKey, byte[]> record) {
        final Long playerId = record.key().getPlayerId();
        final EventType eventType = record.key().getEventType();

        if (Objects.isNull(playerId) || Objects.isNull(eventType)) {
            log.warn("Player Id or Event Type Can not be null for Projection Key");
        }

        final byte[] valueAsBytes = record.value();
        final var responseDataClass = getResponseDataType(eventType);
        final ProjectionEventResponse projectionEventResponse = deserialize(valueAsBytes, responseDataClass);

        messagingService.sendMessage(
            record.key().getGameId(),
            EVENT_RESPONSE.getTopic(),
            projectionEventResponse
        );
        log.info("Response Consumed");
    }

    private Class<? extends ResponseData> getResponseDataType(EventType eventType) {
        switch (eventType) {
            case GAME_STARTED_EVENT -> {
                return GameStartedEventResponseData.class;
            }
            case PIECE_ADDED_EVENT -> {
                return PieceAddedEventResponseData.class;
            }
            case PIECE_REMOVED_EVENT -> {
                return PieceRemovedEventResponseData.class;
            }
            case EXCEPTIONAL_EVENT -> {
                return ExceptionalEventResponseData.class;
            }
            case DISCARD_GAME_EVENT -> {
                return GameDiscardEventResponseData.class;
            }
            default -> throw new IllegalArgumentException("Unknown Event");
        }
    }

    private ProjectionEventResponse deserialize(byte[] responseAsBytes, Class<? extends ResponseData> responseDataClass) {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            SimpleModule module = new SimpleModule();
            module.addAbstractTypeMapping(ResponseData.class, responseDataClass);
            objectMapper.registerModule(module);
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(responseAsBytes, ProjectionEventResponse.class);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to deserialize value", ex);
        }
    }

}
