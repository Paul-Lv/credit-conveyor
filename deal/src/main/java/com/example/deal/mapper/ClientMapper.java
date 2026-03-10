package com.example.deal.mapper;

import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {

    @Mapping(target = "birthDate", source = "birthdate")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "maritalStatus", ignore = true)
    @Mapping(target = "dependentAmount", ignore = true)
    @Mapping(target = "passportIssueDate", ignore = true)
    @Mapping(target = "passportIssueBranch", ignore = true)
    @Mapping(target = "employment", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Client toEntity(LoanApplicationRequestDTO request);

    @Mapping(target = "birthDate", source = "loanRequest.birthdate")
    @Mapping(target = "firstName", source = "loanRequest.firstName")
    @Mapping(target = "lastName", source = "loanRequest.lastName")
    @Mapping(target = "middleName", source = "loanRequest.middleName")
    @Mapping(target = "email", source = "loanRequest.email")
    @Mapping(target = "passportSeries", source = "loanRequest.passportSeries")
    @Mapping(target = "passportNumber", source = "loanRequest.passportNumber")
    @Mapping(target = "gender", source = "request.gender", conditionExpression = "java(request.getGender() != null)")
    @Mapping(target = "maritalStatus", source = "request.maritalStatus", conditionExpression = "java(request.getMaritalStatus() != null)")
    @Mapping(target = "dependentAmount", source = "request.dependentAmount")
    @Mapping(target = "passportIssueDate", source = "request.passportIssueDate")
    @Mapping(target = "passportIssueBranch", source = "request.passportIssueBranch")
    @Mapping(target = "employment", source = "request.employment")
    @Mapping(target = "account", source = "request.account")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Client toEntity(FinishRegistrationRequestDTO request, LoanApplicationRequestDTO loanRequest);
}

/*
package com.example.deal.mapper;


import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {

    @Mapping(target = "birthDate", source = "birthdate")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "maritalStatus", ignore = true)
    @Mapping(target = "dependentAmount", ignore = true)
    @Mapping(target = "passportIssueDate", ignore = true)
    @Mapping(target = "passportIssueBranch", ignore = true)
    @Mapping(target = "employment", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Client toEntity(LoanApplicationRequestDTO request);

    @Mapping(target = "gender", expression = "java(request.getGender() != null ? request.getGender().name() : null)")
    @Mapping(target = "maritalStatus", expression = "java(request.getMaritalStatus() != null ? request.getMaritalStatus().name() : null)")
    @Mapping(target = "birthDate", source = "birthdate")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Client toEntity(FinishRegistrationRequestDTO request, LoanApplicationRequestDTO loanRequest);
}*/