package com.tecmilenio.ms.companies.ms_companies.companies.services;

import com.tecmilenio.ms.companies.ms_companies.common.exceptions.CompanyNotFound;
import com.tecmilenio.ms.companies.ms_companies.companies.dto.CompanyDto;
import com.tecmilenio.ms.companies.ms_companies.companies.dto.CreateCompanyDto;
import com.tecmilenio.ms.companies.ms_companies.companies.entities.Company;
import com.tecmilenio.ms.companies.ms_companies.companies.mappers.CompanyMapper;
import com.tecmilenio.ms.companies.ms_companies.companies.repositories.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository repository;
    private final CompanyMapper mapper;


    public CompanyServiceImpl(CompanyRepository repository, CompanyMapper mapper) {
        this.mapper = mapper;
        this.repository = repository;
    }


    @Override
    public List<CompanyDto> findAll(Pageable pageable) {
        Page<Company> companies = repository.findAll(pageable);
        return companies.stream().map(c -> mapper.toDto(c)).toList();
    }

    @Override
    public CompanyDto findOneById(Long id) {

        Company company = repository.findById(id).orElseThrow(() -> new CompanyNotFound("Company with id " + id + " was not found"));
        return mapper.toDto(company);

    }

    @Override
    public CompanyDto create(CreateCompanyDto createCompanyDto) {

        Company company = mapper.toEntity(createCompanyDto);

        Company savedCompany = repository.save(company);
        return mapper.toDto(savedCompany);
    }

    @Override
    public CompanyDto update(Long id, CreateCompanyDto createCompanyDto) {

        Company oldCompany = repository.findById(id).orElseThrow(() -> new CompanyNotFound("Company with id " + id + " was not found"));

        oldCompany.setAddress(createCompanyDto.getAddress());
        oldCompany.setEmail(createCompanyDto.getEmail());
        oldCompany.setTelephone(createCompanyDto.getTelephone());

        Company updatedCompany = repository.save(oldCompany);
        return mapper.toDto(updatedCompany);
    }

    @Override
    public void delete(Long id) {

        Company company = repository.findById(id).orElseThrow(() -> new CompanyNotFound("Company with id " + id + " was not found"));

        company.setStatus("inactive");
        repository.save(company);
    }
}
