package com.example.audit.service;

import com.example.audit.dto.AuditEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String REDIS_KEY_PREFIX = "audit:";

    @KafkaListener(topics = "audit", groupId = "audit-group")
    public void consumeAuditEvent(AuditEvent event) {
        try {
            String key = REDIS_KEY_PREFIX + event.getId().toString();
            redisTemplate.opsForValue().set(key, event);
            log.info("Saved audit event: {}", event.getId());
        } catch (Exception e) {
            log.error("Error saving audit event", e);
        }
    }

    public AuditEvent findById(UUID id) {
        return (AuditEvent) redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + id.toString());
    }

    public List<AuditEvent> findAll() {
        Set<String> keys = redisTemplate.keys(REDIS_KEY_PREFIX + "*");
        List<AuditEvent> events = new ArrayList<>();
        if (keys != null) {
            for (String key : keys) {
                AuditEvent event = (AuditEvent) redisTemplate.opsForValue().get(key);
                if (event != null) {
                    events.add(event);
                }
            }
        }
        return events;
    }
}