package com.example.deal.controller;

import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.service.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.filter.OrderedFormContentFilter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
@Tag(name = "Сделка", description = "API для работы с заявками")
public class DealController {

    private final DealService dealService;

    private final OrderedFormContentFilter orderedFormContentFilter;

    @PostMapping("/application")
    @Operation(summary = "Создание заявки",
            description = "Принимает заявку, создает клиента и заявку в БД, отправляет запрос в conveyor")
    public ResponseEntity<List<LoanOfferDTO>> createApplication(@RequestBody LoanApplicationRequestDTO request) {

        List<LoanOfferDTO> offers = dealService.createApplication(request);

        return ResponseEntity.ok(offers);
    }


    @PutMapping("/offer")
    @Operation(summary = "Выбор предложения",
            description = "Сохраняет выбранное предложение и обновляет статус заявки")
    public ResponseEntity<Void> applyOffer(@RequestBody LoanOfferDTO offer) {

        dealService.applyOffer(offer);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/calculate/{applicationId}")
    @Operation(summary = "Расчет кредита",
            description = "Завершает регистрацию и отправляет данные на скоринг")
    public ResponseEntity<Void> calculateCredit(
            @PathVariable UUID applicationId,
            @RequestBody FinishRegistrationRequestDTO request) {

        dealService.calculateCredit(applicationId, request);
        return ResponseEntity.ok().build();
    }


}
