package com.example.deal.feign;

import com.example.deal.dto.CreditDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.dto.ScoringDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "conveyor", url = "${conveyor.url}")
public interface ConveyorClient {

    @PostMapping("/conveyor/offers")
    List<LoanOfferDTO> getOffers(@RequestBody LoanApplicationRequestDTO request);

    @PostMapping("/conveyor/calculation")
    CreditDTO calculateCredit(@RequestBody ScoringDataDTO scoringData);

}
