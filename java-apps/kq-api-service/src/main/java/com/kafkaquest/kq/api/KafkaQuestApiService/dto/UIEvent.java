package com.kafkaquest.kq.api.KafkaQuestApiService.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents an Event emitted from the front-end
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UIEvent {

    private EventInfo eventInfo;

    private JsonNode eventData;
}
