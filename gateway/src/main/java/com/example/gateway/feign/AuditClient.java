package com.example.gateway.feign;

import com.example.audit.dto.AuditEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "audit-client", url = "${services.audit.url:http://localhost:8085}")
public interface AuditClient {

    @GetMapping("/audit/{id}")
    AuditEvent getAuditById(@PathVariable("id") UUID id);

    @GetMapping("/audit")
    List<AuditEvent> getAllAuditEvents();
}