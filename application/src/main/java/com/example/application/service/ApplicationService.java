package com.example.application.service;

import com.example.dto.LoanApplicationRequestDTO;
import com.example.dto.LoanOfferDTO;
import com.example.application.feign.DealClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final DealClient dealClient;

    private static final BigDecimal MIN_AMOUNT = new BigDecimal("10000");
    private static final Integer MIN_TERM = 6;
    private static final Integer ADULT_AGE = 18;
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]{2,50}@[A-Za-z0-9.-]{2,20}$";
    private static final String NAME_PATTERN = "^[A-Za-z]{2,30}$";
    private static final String PASSPORT_PATTERN = "^\\d{4}$";

    public List<LoanOfferDTO> prescoreAndSend(LoanApplicationRequestDTO request) {
        log.info("Starting prescoring for request: {}", request);

        // Прескоринг
        prescoringValidation(request);
        log.info("Prescoring passed successfully");

        // Отправляем в deal
        List<LoanOfferDTO> offers = dealClient.createApplication(request);
        log.info("Received {} offers from deal", offers.size());

        return offers;
    }

    public void selectOffer(LoanOfferDTO offer) {
        log.info("Selecting offer: {}", offer);
        dealClient.applyOffer(offer);
        log.info("Offer selected successfully");
    }

    private void prescoringValidation(LoanApplicationRequestDTO request) {
        // Проверка суммы
        if (request.getAmount() == null || request.getAmount().compareTo(MIN_AMOUNT) < 0) {
            throw new IllegalArgumentException("Сумма кредита должна быть не менее " + MIN_AMOUNT);
        }

        // Проверка срока
        if (request.getTerm() == null || request.getTerm() < MIN_TERM) {
            throw new IllegalArgumentException("Срок кредита должен быть не менее " + MIN_TERM + " месяцев");
        }

        // Проверка имени
        if (!Pattern.matches(NAME_PATTERN, request.getFirstName())) {
            throw new IllegalArgumentException("Имя должно содержать от 2 до 30 латинских букв");
        }

        // Проверка фамилии
        if (!Pattern.matches(NAME_PATTERN, request.getLastName())) {
            throw new IllegalArgumentException("Фамилия должна содержать от 2 до 30 латинских букв");
        }

        // Проверка отчества (если указано)
        if (request.getMiddleName() != null && !request.getMiddleName().isEmpty()
                && !Pattern.matches(NAME_PATTERN, request.getMiddleName())) {
            throw new IllegalArgumentException("Отчество должно содержать от 2 до 30 латинских букв");
        }

        // Проверка email
        if (!Pattern.matches(EMAIL_PATTERN, request.getEmail())) {
            throw new IllegalArgumentException("Некорректный формат email");
        }

        // Проверка возраста
        if (request.getBirthdate() == null) {
            throw new IllegalArgumentException("Дата рождения не указана");
        }
        int age = Period.between(request.getBirthdate(), LocalDate.now()).getYears();
        if (age < ADULT_AGE) {
            throw new IllegalArgumentException("Возраст должен быть не менее " + ADULT_AGE + " лет");
        }

        // Проверка паспорта
        if (!Pattern.matches(PASSPORT_PATTERN, request.getPassportSeries())) {
            throw new IllegalArgumentException("Серия паспорта должна содержать 4 цифры");
        }
        if (!Pattern.matches("^\\d{6}$", request.getPassportNumber())) {
            throw new IllegalArgumentException("Номер паспорта должен содержать 6 цифр");
        }
    }
}