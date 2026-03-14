package com.example.conveyor.service;

import com.example.conveyor.dto.CreditDTO;
import com.example.dto.LoanApplicationRequestDTO;
import com.example.dto.LoanOfferDTO;
import com.example.dto.ScoringDataDTO;

import java.util.List;

public interface ConveyorService {
    List<LoanOfferDTO> generateOffers(LoanApplicationRequestDTO request);

    CreditDTO calculateCredit(ScoringDataDTO scoringData);
}