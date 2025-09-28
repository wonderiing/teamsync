package com.tecmilenio.ms.companies.ms_companies.companies.repository;

import com.tecmilenio.ms.companies.ms_companies.companies.entities.Company;
import com.tecmilenio.ms.companies.ms_companies.companies.repositories.CompanyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    private Company testCompany;

    @BeforeEach
    void setUp() {

        testCompany = Company.builder()
                .fullName("New Company")
                .rfc("2315123")
                .address("address")
                .email("ca223871@gmail.com")
                .telephone("231521")
                .build();
    }



    @Test
    @DisplayName("Should save company succesfull")
    public void companyRepository_save_returnCompany() {

        Company result = companyRepository.save(testCompany);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getEmail()).isEqualTo(testCompany.getEmail());
        Assertions.assertThat(result.getId()).isEqualTo(testCompany.getId());

    }

    @Test
    @DisplayName("Should find company by id")
    public void companyRepository_FindById_ReturnCompany() {

        Company savedCompany = companyRepository.save(testCompany);

        Company result = companyRepository.findById(savedCompany.getId()).orElse(null);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(savedCompany.getId());
        Assertions.assertThat(result.getEmail()).isEqualTo(savedCompany.getEmail());

    }


    @Test
    @DisplayName("Should delete company")
    public void companyRepository_Delete_ReturnNull() {

        Company savedCompany = companyRepository.save(testCompany);

        companyRepository.delete(savedCompany);

        Company deletedCompany = companyRepository.findById(savedCompany.getId()).orElse(null);

        Assertions.assertThat(deletedCompany).isEqualTo(null);

    }

    @Test
    @DisplayName("Should find all companys")
    public void companyRepository_FindAll_ReturnAllCompanies() {

        Company savedCompany = companyRepository.save(testCompany);

        List<Company> result = companyRepository.findAll();

        Assertions.assertThat(result.get(0).getEmail()).isEqualTo(savedCompany.getEmail());
        Assertions.assertThat(result.get(0).getId()).isEqualTo(savedCompany.getId());

    }


}
