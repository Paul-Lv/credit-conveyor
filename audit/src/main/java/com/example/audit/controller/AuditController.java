package com.example.audit.controller;

import com.example.audit.dto.AuditEvent;
import com.example.audit.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
@Tag(name = "Аудит", description = "API для работы с событиями аудита")
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить событие по ID")
    public ResponseEntity<AuditEvent> getById(@PathVariable UUID id) {
        AuditEvent event = auditService.findById(id);
        return event != null ? ResponseEntity.ok(event) : ResponseEntity.notFound().build();
    }

    @GetMapping
    @Operation(summary = "Получить все события")
    public ResponseEntity<List<AuditEvent>> getAll() {
        return ResponseEntity.ok(auditService.findAll());
    }
}