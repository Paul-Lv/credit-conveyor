package com.example.deal.entity;

import com.example.dto.EmploymentDTO;
import com.example.dto.LoanApplicationRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "gender")
    private String gender;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "dependent_amount")
    private Integer dependentAmount;

    @Column(name = "passport_series", nullable = false)
    private String passportSeries;

    @Column(name = "passport_number", nullable = false)
    private String passportNumber;

    @Column(name = "passport_issue_date")
    private LocalDate passportIssueDate;

    @Column(name = "passport_issue_branch")
    private String passportIssueBranch;

    @Column(name = "employment", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private EmploymentDTO employment;

    @Column(name = "account")
    private String account;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = LocalDateTime.now();
    }

    public static Client fromLoanApplicationRequest(LoanApplicationRequestDTO request) {
        Client client = new Client();
        client.setLastName(request.getLastName());
        client.setFirstName(request.getFirstName());
        client.setMiddleName(request.getMiddleName());
        client.setBirthDate(request.getBirthdate());
        client.setEmail(request.getEmail());
        client.setPassportSeries(request.getPassportSeries());
        client.setPassportNumber(request.getPassportNumber());
        return client;
    }
}