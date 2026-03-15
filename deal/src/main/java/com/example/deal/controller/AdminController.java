package com.example.deal.controller;

import com.example.deal.entity.Application;
import com.example.deal.repository.ApplicationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deal/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Админские API для управления заявками")
public class AdminController {

    private final ApplicationRepository applicationRepository;

    @GetMapping("/application/{applicationId}")
    @Operation(summary = "Получить заявку по ID")
    public ResponseEntity<Application> getApplication(@PathVariable UUID applicationId) {
        return applicationRepository.findById(applicationId)
                .map(body -> ResponseEntity.ok(body))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/application")
    @Operation(summary = "Получить все заявки")
    public ResponseEntity<List<Application>> getAllApplications() {
        List<Application> applications = applicationRepository.findAll();
        return ResponseEntity.ok(applications);
    }
}