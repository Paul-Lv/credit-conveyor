package com.example.audit.producer;

import com.example.audit.dto.AuditEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditProducer {

    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;

    @Value("${audit.kafka.topic:audit}")
    private String auditTopic;

    public void sendAuditEvent(AuditEvent event) {
//todo
//        log.debug("Sending audit event: {}", event);

        kafkaTemplate.send(auditTopic, event);
    }
}