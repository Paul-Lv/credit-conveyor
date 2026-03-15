package com.example.deal.service;

import com.example.deal.dto.CreditDTO;
import com.example.deal.dto.EmailMessage;
import com.example.deal.entity.Application;
import com.example.deal.entity.ApplicationStatusHistory;
import com.example.deal.entity.Client;
import com.example.deal.entity.Credit;
import com.example.deal.enums.ApplicationStatus;
import com.example.deal.feign.ConveyorClient;
import com.example.deal.mapper.ClientMapper;
import com.example.deal.mapper.CreditMapper;
import com.example.deal.repository.ApplicationRepository;
import com.example.deal.repository.ClientRepository;
import com.example.deal.repository.CreditRepository;
import com.example.dto.FinishRegistrationRequestDTO;
import com.example.dto.LoanApplicationRequestDTO;
import com.example.dto.LoanOfferDTO;
import com.example.dto.ScoringDataDTO;
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

    private final ClientMapper clientMapper;
    private final CreditMapper creditMapper;
    private final ClientRepository clientRepository;
    private final ApplicationRepository applicationRepository;
    private final CreditRepository creditRepository;
    private final ConveyorClient conveyorClient;

    private final KafkaProducerService kafkaProducerService;

    @Transactional
    public List<LoanOfferDTO> createApplication(LoanApplicationRequestDTO request) {
        log.info("Creating application for request: {}", request);

        // Создаем и сохраняем клиента
        Client client = clientMapper.toEntity(request);
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

        // После обновления статуса
        EmailMessage emailMessage = new EmailMessage(
                application.getClient().getEmail(),
                "Завершите регистрацию",
                "Ваша заявка предварительно одобрена. Пожалуйста, завершите регистрацию.",
                "finish-registration"
        );
        kafkaProducerService.sendMessage("finish-registration", emailMessage);
    }

    @Transactional
    public void calculateCredit(UUID applicationId, FinishRegistrationRequestDTO request) {
        log.info("Calculating credit for application: {} with request: {}", applicationId, request);

        // 1. Получаем заявку из БД
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        Client client = application.getClient();

        // 2. Обновляем данные клиента через маппер
        // Создаём базовый запрос из данных, которые уже есть в клиенте
        LoanApplicationRequestDTO loanRequest = new LoanApplicationRequestDTO();
        loanRequest.setAmount(application.getAppliedOffer().getRequestedAmount());
        loanRequest.setTerm(application.getAppliedOffer().getTerm());
        loanRequest.setFirstName(client.getFirstName());
        loanRequest.setLastName(client.getLastName());
        loanRequest.setMiddleName(client.getMiddleName());
        loanRequest.setEmail(client.getEmail());
        loanRequest.setBirthdate(client.getBirthDate());
        loanRequest.setPassportSeries(client.getPassportSeries());
        loanRequest.setPassportNumber(client.getPassportNumber());

        // Маппер объединяет данные из FinishRegistrationRequestDTO и LoanApplicationRequestDTO
        Client updatedClient = clientMapper.toEntity(request, loanRequest);
        // Важно: сохраняем ID и дату создания от старого клиента
        updatedClient.setId(client.getId());
        updatedClient.setCreationDate(client.getCreationDate());

        // Сохраняем обновлённого клиента
        client = clientRepository.save(updatedClient);
        log.info("Client updated with id: {}", client.getId());

        // 3. Создаём ScoringDataDTO для отправки в conveyor
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

        // 4. Получаем расчёт от conveyor
        CreditDTO creditDTO = conveyorClient.calculateCredit(scoringData);
        log.info("Received credit calculation from conveyor");

        // 5. Создаём кредит через маппер (ВОТ ЗДЕСЬ ТОЖЕ ИЗМЕНЕНИЯ)
        Credit credit = creditMapper.toEntity(creditDTO);
        credit = creditRepository.save(credit);
        log.info("Credit saved with id: {}", credit.getId());

        // 6. Обновляем заявку
        application.setCredit(credit);
        application.setStatus(ApplicationStatus.CLIENT_DOCUMENT_REQUESTED);

        List<ApplicationStatusHistory> statusHistory = application.getStatusHistory();
        if (statusHistory == null) {
            statusHistory = new ArrayList<>();
        }
        statusHistory.add(new ApplicationStatusHistory(
                ApplicationStatus.CLIENT_DOCUMENT_REQUESTED,
                LocalDateTime.now(),
                "Credit calculated"
        ));
        application.setStatusHistory(statusHistory);

        applicationRepository.save(application);
        log.info("Credit calculation completed");

        // 7. Отправляем событие в Kafka (для Level 4)
        EmailMessage emailMessage = new EmailMessage(
                client.getEmail(),
                "Создание документов",
                "Ваши документы готовы. Пожалуйста, запросите их отправку.",
                "create-documents"
        );
        kafkaProducerService.sendMessage("create-documents", emailMessage);
    }
}