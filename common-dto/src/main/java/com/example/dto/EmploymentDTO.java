package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentDTO {
    private EmploymentStatus status;
    private String employerInn;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;

    public enum EmploymentStatus {
        UNEMPLOYED, SELF_EMPLOYED, BUSINESS_OWNER, EMPLOYED
    }

    public enum Position {
        WORKER, MID_MANAGER, TOP_MANAGER
    }
}