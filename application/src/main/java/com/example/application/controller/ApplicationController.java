package com.example.application.controller;


import com.example.application.dto.LoanApplicationRequestDTO;
import com.example.application.dto.LoanOfferDTO;
import com.example.application.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
@Tag(name = "Заявка", description = "API для работы с заявками")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    @Operation(summary = "Создание заявки",
            description = "Прескоринг + отправка в deal")
    public ResponseEntity<List<LoanOfferDTO>> createApplication(@RequestBody LoanApplicationRequestDTO request) {

        List<LoanOfferDTO> offers = applicationService.prescoreAndSend(request);
        return ResponseEntity.ok(offers);
    }

    @PutMapping("/offer")
    @Operation(summary = "Выбор предложения",
            description = "Отправляет выбранное предложение в deal")
    public ResponseEntity<Void> selectOffer(@RequestBody LoanOfferDTO offer) {
        applicationService.selectOffer(offer);
        return ResponseEntity.ok().build();
    }
}
