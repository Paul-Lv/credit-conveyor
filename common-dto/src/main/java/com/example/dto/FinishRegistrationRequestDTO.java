package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на завершение регистрации")
public class FinishRegistrationRequestDTO {

    @Schema(description = "Пол", example = "MALE")
    private ScoringDataDTO.Gender gender;

    @Schema(description = "Семейное положение", example = "MARRIED")
    private ScoringDataDTO.MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев", example = "1")
    private Integer dependentAmount;

    @Schema(description = "Дата выдачи паспорта", example = "2015-05-15")
    private LocalDate passportIssueDate;

    @Schema(description = "Кем выдан паспорт", example = "ОВД Ленинского района г. Москва")
    private String passportIssueBranch;

    @Schema(description = "Информация о занятости")
    private EmploymentDTO employment;

    @Schema(description = "Номер счета", example = "40817810099910004321")
    private String account;
}