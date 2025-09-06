package com.tecmilenio.ms.companies.ms_companies.companies.controllers;

import com.tecmilenio.ms.companies.ms_companies.companies.dto.CompanyDto;
import com.tecmilenio.ms.companies.ms_companies.companies.dto.CreateCompanyDto;
import com.tecmilenio.ms.companies.ms_companies.companies.services.CompanyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.Path;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
@Validated
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }


    @GetMapping
    public ResponseEntity<List<CompanyDto>> getAllCompanies(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<CompanyDto> companies = companyService.findAll(pageable);
        return ResponseEntity.ok(companies);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable @Min(1) Long id) {

        CompanyDto company = companyService.findOneById(id);
        return ResponseEntity.ok(company);
    }

    @PostMapping
    public ResponseEntity<CompanyDto> createCompany(@RequestBody @Valid CreateCompanyDto createCompanyDto) {
        CompanyDto createdCompany = companyService.create(createCompanyDto);


        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdCompany.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdCompany);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CompanyDto> updateCompany(@RequestBody @Valid CreateCompanyDto createCompanyDto, @PathVariable @Min(1) Long id) {
        CompanyDto updatedCompany = companyService.update(id, createCompanyDto);

        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteCompany(@PathVariable Long id) {
        companyService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
