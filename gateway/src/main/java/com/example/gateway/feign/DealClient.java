package com.example.gateway.feign;

import com.example.dto.FinishRegistrationRequestDTO;
import com.example.dto.LoanApplicationRequestDTO;
import com.example.dto.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "deal-client", url = "${services.deal.url}")
public interface DealClient {

    // Для пользователей (те же, что в application, но прямым доступом)
    @PostMapping("/deal/application")
    List<LoanOfferDTO> createApplication(@RequestBody LoanApplicationRequestDTO request);

    @PutMapping("/deal/offer")
    void applyOffer(@RequestBody LoanOfferDTO offer);

    @PutMapping("/deal/calculate/{applicationId}")
    void calculateCredit(@PathVariable UUID applicationId, @RequestBody FinishRegistrationRequestDTO request);

    // Admin endpoints (добавим позже)
    @GetMapping("/deal/admin/application/{applicationId}")
    Object getApplication(@PathVariable UUID applicationId);

    @GetMapping("/deal/admin/application")
    List<Object> getAllApplications();
}