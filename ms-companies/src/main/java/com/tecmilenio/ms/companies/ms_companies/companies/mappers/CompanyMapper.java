package com.tecmilenio.ms.companies.ms_companies.companies.mappers;

import com.tecmilenio.ms.companies.ms_companies.companies.dto.CompanyDto;
import com.tecmilenio.ms.companies.ms_companies.companies.dto.CreateCompanyDto;
import com.tecmilenio.ms.companies.ms_companies.companies.entities.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {


    CompanyDto toDto(Company company);
    Company toEntity(CreateCompanyDto companyDto);
}
