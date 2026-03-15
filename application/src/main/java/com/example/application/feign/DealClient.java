package com.example.application.feign;

import com.example.dto.LoanApplicationRequestDTO;
import com.example.dto.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "deal", url = "${deal.url}")
public interface DealClient {

    @PostMapping("/deal/application")
    List<LoanOfferDTO> createApplication(@RequestBody LoanApplicationRequestDTO request);

    @PutMapping("/deal/offer")
    void applyOffer(@RequestBody LoanOfferDTO offer);

}
