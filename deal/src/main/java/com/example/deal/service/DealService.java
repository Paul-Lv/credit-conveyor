package com.example.deal.service;

import com.example.deal.dto.*;
import com.example.deal.entity.*;
import com.example.deal.enums.ApplicationStatus;
import com.example.deal.feign.ConveyorClient;
import com.example.deal.repository.ApplicationRepository;
import com.example.deal.repository.ClientRepository;
import com.example.deal.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealService {

    private final ClientRepository clientRepository;
    private final ApplicationRepository applicationRepository;
    private final CreditRepository creditRepository;
    private final ConveyorClient conveyorClient;

    @Transactional
    public List<LoanOfferDTO> createApplication(LoanApplicationRequestDTO request) {
        log.info("Creating application for request: {}", request);

        // Создаем и сохраняем клиента
        Client client = Client.fromLoanApplicationRequest(request);
        client = clientRepository.save(client);
        log.info("Client saved with id: {}", client.getId());

        // Создаем заявку
        Application application = new Application();
        application.setClient(client);
        application.setStatus(ApplicationStatus.PREAPPROVAL);

        // Инициализируем историю статусов
        List<ApplicationStatusHistory> statusHistory = new ArrayList<>();
        statusHistory.add(new ApplicationStatusHistory(
                ApplicationStatus.PREAPPROVAL,
                LocalDateTime.now(),
                "Application created"
        ));
        application.setStatusHistory(statusHistory);

        application = applicationRepository.save(application);
        final UUID applicationId = application.getId();
        log.info("Application saved with id: {}", applicationId);

        // Получаем предложения от conveyor
        List<LoanOfferDTO> offers = conveyorClient.getOffers(request);

        // Устанавливаем applicationId в каждом оффере
        offers.forEach(offer -> offer.setApplicationId(applicationId.toString()));

        return offers;
    }

    @Transactional
    public void applyOffer(LoanOfferDTO offer) {
        log.info("Applying offer: {}", offer);

        Application application = applicationRepository.findById(UUID.fromString(offer.getApplicationId()))
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setAppliedOffer(offer);
        application.setStatus(ApplicationStatus.APPROVED);

        // Добавляем запись в историю статусов
        List<ApplicationStatusHistory> statusHistory = application.getStatusHistory();
        if (statusHistory == null) {
            statusHistory = new ArrayList<>();
        }
        statusHistory.add(new ApplicationStatusHistory(
                ApplicationStatus.APPROVED,
                LocalDateTime.now(),
                "Offer selected: " + offer
        ));
        application.setStatusHistory(statusHistory);

        applicationRepository.save(application);
        log.info("Offer applied successfully");
    }

    @Transactional
    public void calculateCredit(UUID applicationId, FinishRegistrationRequestDTO request) {
        log.info("Calculating credit for application: {} with request: {}", applicationId, request);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        Client client = application.getClient();

        // Обновляем данные клиента
        client.setGender(request.getGender() != null ? request.getGender().name() : null);
        client.setMaritalStatus(request.getMaritalStatus() != null ? request.getMaritalStatus().name() : null);
        client.setDependentAmount(request.getDependentAmount());
        client.setPassportIssueDate(request.getPassportIssueDate());
        client.setPassportIssueBranch(request.getPassportIssueBranch());
        client.setEmployment(request.getEmployment());
        client.setAccount(request.getAccount());

        clientRepository.save(client);

        // Создаем ScoringDataDTO для отправки в conveyor
        ScoringDataDTO scoringData = new ScoringDataDTO();
        scoringData.setAmount(application.getAppliedOffer().getRequestedAmount());
        scoringData.setTerm(application.getAppliedOffer().getTerm());
        scoringData.setFirstName(client.getFirstName());
        scoringData.setLastName(client.getLastName());
        scoringData.setMiddleName(client.getMiddleName());
        scoringData.setGender(request.getGender());
        scoringData.setBirthdate(client.getBirthDate());
        scoringData.setPassportSeries(client.getPassportSeries());
        scoringData.setPassportNumber(client.getPassportNumber());
        scoringData.setPassportIssueDate(request.getPassportIssueDate());
        scoringData.setPassportIssueBranch(request.getPassportIssueBranch());
        scoringData.setMaritalStatus(request.getMaritalStatus());
        scoringData.setDependentAmount(request.getDependentAmount());
        scoringData.setEmployment(request.getEmployment());
        scoringData.setAccount(request.getAccount());
        scoringData.setIsInsuranceEnabled(application.getAppliedOffer().getIsInsuranceEnabled());
        scoringData.setIsSalaryClient(application.getAppliedOffer().getIsSalaryClient());

        // Получаем расчет от conveyor
        CreditDTO creditDTO = conveyorClient.calculateCredit(scoringData);

        // Создаем и сохраняем кредит
        Credit credit = new Credit();
        credit.setAmount(creditDTO.getAmount());
        credit.setTerm(creditDTO.getTerm());
        credit.setMonthlyPayment(creditDTO.getMonthlyPayment());
        credit.setRate(creditDTO.getRate());
        credit.setPsk(creditDTO.getPsk());
        credit.setPaymentSchedule(creditDTO.getPaymentSchedule());
        credit.setInsuranceEnabled(creditDTO.getIsInsuranceEnabled());
        credit.setSalaryClient(creditDTO.getIsSalaryClient());

        credit = creditRepository.save(credit);
        log.info("Credit saved with id: {}", credit.getId());

        // Обновляем заявку
        application.setCredit(credit);
        application.setStatus(ApplicationStatus.CLIENT_DOCUMENT_REQUESTED);

        List<ApplicationStatusHistory> statusHistory = application.getStatusHistory();
        statusHistory.add(new ApplicationStatusHistory(
                ApplicationStatus.CLIENT_DOCUMENT_REQUESTED,
                LocalDateTime.now(),
                "Credit calculated"
        ));

        applicationRepository.save(application);
        log.info("Credit calculation completed");
    }
}