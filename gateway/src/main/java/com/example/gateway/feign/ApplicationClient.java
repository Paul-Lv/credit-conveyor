package com.example.gateway.feign;

import com.example.gateway.dto.LoanApplicationRequestDTO;
import com.example.gateway.dto.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "application-client", url = "${services.application.url}")
public interface ApplicationClient {

    @PostMapping("/application")
    List<LoanOfferDTO> createApplication(@RequestBody LoanApplicationRequestDTO request);

    @PutMapping("/application/offer")
    void selectOffer(@RequestBody LoanOfferDTO offer);
}