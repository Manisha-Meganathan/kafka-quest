package com.kafkaquest.kq.common.util.serializer;

import com.kafkaquest.kq.common.util.models.kafkakeys.ProjectionKey;
import com.kafkaquest.kq.common.util.serializer.generic.GenericKafkaDeserializer;

public class ProjectionKeyDeserializer extends GenericKafkaDeserializer<ProjectionKey> {
    public ProjectionKeyDeserializer(Class<ProjectionKey> classValue) {
        super(classValue);
    }

    public ProjectionKeyDeserializer() {
        super(ProjectionKey.class);
    }
}
