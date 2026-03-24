package com.example.audit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditEvent {
    private UUID id;
    private String service;     // APPLICATION, DEAL, CONVEYOR, DOSSIER, GATEWAY
    private String type;        // START, SUCCESS, FAILURE
    private String message;
    private LocalDateTime timestamp;
}