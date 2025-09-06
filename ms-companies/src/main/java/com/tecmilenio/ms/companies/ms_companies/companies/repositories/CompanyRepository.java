package com.tecmilenio.ms.companies.ms_companies.companies.repositories;

import com.tecmilenio.ms.companies.ms_companies.companies.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
