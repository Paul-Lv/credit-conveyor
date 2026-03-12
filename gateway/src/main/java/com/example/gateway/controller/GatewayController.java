package com.example.gateway.controller;

import com.example.gateway.dto.FinishRegistrationRequestDTO;
import com.example.gateway.dto.LoanApplicationRequestDTO;
import com.example.gateway.dto.LoanOfferDTO;
import com.example.gateway.feign.ApplicationClient;
import com.example.gateway.feign.DealClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Gateway", description = "Единая точка входа в систему")
public class GatewayController {

    private final ApplicationClient applicationClient;
    private final DealClient dealClient;

    @PostMapping("/application")
    @Operation(summary = "Создать заявку")
    public ResponseEntity<List<LoanOfferDTO>> createApplication(@RequestBody LoanApplicationRequestDTO request) {
        log.info("Gateway: createApplication called with {}", request);
        List<LoanOfferDTO> offers = applicationClient.createApplication(request);
        return ResponseEntity.ok(offers);
    }

    @PutMapping("/application/offer")
    @Operation(summary = "Выбрать предложение")
    public ResponseEntity<Void> selectOffer(@RequestBody LoanOfferDTO offer) {
        log.info("Gateway: selectOffer called with {}", offer);
        applicationClient.selectOffer(offer);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/deal/calculate/{applicationId}")
    @Operation(summary = "Завершить регистрацию и рассчитать кредит")
    public ResponseEntity<Void> calculateCredit(
            @PathVariable UUID applicationId,
            @RequestBody FinishRegistrationRequestDTO request) {
        log.info("Gateway: calculateCredit called for application {}", applicationId);
        dealClient.calculateCredit(applicationId, request);
        return ResponseEntity.ok().build();
    }

    // Admin endpoints
    @GetMapping("/admin/application/{applicationId}")
    @Operation(summary = "Получить заявку по ID (админ)")
    public ResponseEntity<Object> getApplication(@PathVariable UUID applicationId) {
        log.info("Gateway: admin getApplication called for {}", applicationId);
        Object application = dealClient.getApplication(applicationId);
        return ResponseEntity.ok(application);
    }

    @GetMapping("/admin/application")
    @Operation(summary = "Получить все заявки (админ)")
    public ResponseEntity<List<Object>> getAllApplications() {
        log.info("Gateway: admin getAllApplications called");
        List<Object> applications = dealClient.getAllApplications();
        return ResponseEntity.ok(applications);
    }
}