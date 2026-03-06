package com.example.conveyor.controller;

import com.example.conveyor.dto.LoanApplicationRequestDTO;
import com.example.conveyor.dto.LoanOfferDTO;
import com.example.conveyor.dto.ScoringDataDTO;
import com.example.conveyor.dto.CreditDTO;
import com.example.conveyor.service.ConveyorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conveyor")
@RequiredArgsConstructor
@Tag(name = "Кредитный конвейер", description = "API для расчета условий кредита и скоринга")
public class ConveyorController {

    private final ConveyorService conveyorService;

    @PostMapping("/offers")
    @Operation(summary = "Расчёт возможных условий кредита",
            description = "Принимает заявку, проводит прескоринг и возвращает 4 предложения")
    public ResponseEntity<List<LoanOfferDTO>> getOffers(@RequestBody LoanApplicationRequestDTO request) {
        List<LoanOfferDTO> offers = conveyorService.generateOffers(request);
        return ResponseEntity.ok(offers);
    }

    @PostMapping("/calculation")
    @Operation(summary = "Валидация и скоринг данных",
            description = "Принимает полные данные для скоринга и возвращает рассчитанный кредит")
    public ResponseEntity<CreditDTO> calculateCredit(@RequestBody ScoringDataDTO scoringData) {
        CreditDTO credit = conveyorService.calculateCredit(scoringData);
        return ResponseEntity.ok(credit);
    }
}