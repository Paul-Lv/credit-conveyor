package com.example.validation;

import com.example.dto.LoanApplicationRequestDTO;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class PrescoringValidator {

    public static void validate(LoanApplicationRequestDTO request) {
        validateAmount(request);
        validateTerm(request);
        validateName(request.getFirstName(), "Имя");
        validateName(request.getLastName(), "Фамилия");
        validateMiddleName(request.getMiddleName());
        validateEmail(request.getEmail());
        validateBirthDate(request.getBirthdate());
        validatePassport(request);
    }

    private static void validateAmount(LoanApplicationRequestDTO request) {
        if (request.getAmount() == null ||
                request.getAmount().compareTo(ValidationConstants.MIN_AMOUNT) < 0) {
            throw new IllegalArgumentException("Сумма кредита должна быть не менее " +
                    ValidationConstants.MIN_AMOUNT);
        }
    }

    private static void validateTerm(LoanApplicationRequestDTO request) {
        if (request.getTerm() == null || request.getTerm() < ValidationConstants.MIN_TERM) {
            throw new IllegalArgumentException("Срок кредита должен быть не менее " +
                    ValidationConstants.MIN_TERM + " месяцев");
        }
    }

    private static void validateName(String name, String fieldName) {
        if (!Pattern.matches(ValidationConstants.NAME_PATTERN, name)) {
            throw new IllegalArgumentException(fieldName +
                    " должно содержать от 2 до 30 латинских букв");
        }
    }

    private static void validateMiddleName(String middleName) {
        if (middleName != null && !middleName.isEmpty() &&
                !Pattern.matches(ValidationConstants.NAME_PATTERN, middleName)) {
            throw new IllegalArgumentException("Отчество должно содержать от 2 до 30 латинских букв");
        }
    }

    private static void validateEmail(String email) {
        if (!Pattern.matches(ValidationConstants.EMAIL_PATTERN, email)) {
            throw new IllegalArgumentException("Некорректный формат email");
        }
    }

    private static void validateBirthDate(LocalDate birthdate) {
        if (birthdate == null) {
            throw new IllegalArgumentException("Дата рождения не указана");
        }
        int age = Period.between(birthdate, LocalDate.now()).getYears();
        if (age < ValidationConstants.ADULT_AGE) {
            throw new IllegalArgumentException("Возраст должен быть не менее " +
                    ValidationConstants.ADULT_AGE + " лет");
        }
    }

    private static void validatePassport(LoanApplicationRequestDTO request) {
        if (!Pattern.matches(ValidationConstants.PASSPORT_PATTERN, request.getPassportSeries())) {
            throw new IllegalArgumentException("Серия паспорта должна содержать 4 цифры");
        }
        if (!Pattern.matches(ValidationConstants.PASSPORT_NUMBER_PATTERN,
                request.getPassportNumber())) {
            throw new IllegalArgumentException("Номер паспорта должен содержать 6 цифр");
        }
    }
}