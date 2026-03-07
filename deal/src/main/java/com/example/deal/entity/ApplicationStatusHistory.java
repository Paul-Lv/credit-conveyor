package com.example.deal.entity;

import com.example.deal.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusHistory {
    private ApplicationStatus status;
    private LocalDateTime time;
    private String changeType;
}