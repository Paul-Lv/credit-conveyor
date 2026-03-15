package com.example.application.service;

import com.example.application.feign.DealClient;
import com.example.dto.LoanApplicationRequestDTO;
import com.example.dto.LoanOfferDTO;
import com.example.validation.PrescoringValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final DealClient dealClient;

    public List<LoanOfferDTO> prescoreAndSend(LoanApplicationRequestDTO request) {
        log.info("Starting prescoring for request: {}", request);

        // Прескоринг через общий валидатор
        PrescoringValidator.validate(request);
        log.info("Prescoring passed successfully");

        List<LoanOfferDTO> offers = dealClient.createApplication(request);
        log.info("Received {} offers from deal", offers.size());

        return offers;
    }

    public void selectOffer(LoanOfferDTO offer) {
        log.info("Selecting offer: {}", offer);
        dealClient.applyOffer(offer);
        log.info("Offer selected successfully");
    }
}