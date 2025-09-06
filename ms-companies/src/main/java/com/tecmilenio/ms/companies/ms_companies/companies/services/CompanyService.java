package com.tecmilenio.ms.companies.ms_companies.companies.services;

import com.tecmilenio.ms.companies.ms_companies.companies.dto.CompanyDto;
import com.tecmilenio.ms.companies.ms_companies.companies.dto.CreateCompanyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyService {

    List<CompanyDto> findAll(Pageable pageable);
    CompanyDto findOneById(Long id);
    CompanyDto create(CreateCompanyDto createCompanyDto);
    CompanyDto update(Long id, CreateCompanyDto createCompanyDto);
    void delete(Long id);
}
