package com.example.deal.mapper;

import com.example.deal.dto.CreditDTO;
import com.example.deal.entity.Credit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CreditMapper {

    @Mapping(target = "insuranceEnabled", source = "isInsuranceEnabled")
    @Mapping(target = "salaryClient", source = "isSalaryClient")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creditStatus", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    Credit toEntity(CreditDTO dto);
}