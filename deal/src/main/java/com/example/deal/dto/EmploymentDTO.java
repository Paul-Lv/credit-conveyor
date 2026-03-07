package com.example.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о занятости")
public class EmploymentDTO {

    @Schema(description = "Статус занятости", example = "EMPLOYED")
    private EmploymentStatus status;

    @Schema(description = "ИНН работодателя", example = "770123456789")
    private String employerInn;

    @Schema(description = "Зарплата", example = "150000")
    private BigDecimal salary;

    @Schema(description = "Должность", example = "MID_MANAGER")
    private Position position;

    @Schema(description = "Общий стаж работы (мес)", example = "60")
    private Integer workExperienceTotal;

    @Schema(description = "Текущий стаж работы (мес)", example = "24")
    private Integer workExperienceCurrent;

    public enum EmploymentStatus {
        UNEMPLOYED, SELF_EMPLOYED, BUSINESS_OWNER, EMPLOYED
    }

    public enum Position {
        WORKER, MID_MANAGER, TOP_MANAGER
    }
}