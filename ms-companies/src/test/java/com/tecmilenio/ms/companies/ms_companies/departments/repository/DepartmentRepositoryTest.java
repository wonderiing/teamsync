package com.tecmilenio.ms.companies.ms_companies.departments.repository;

import com.tecmilenio.ms.companies.ms_companies.companies.entities.Company;
import com.tecmilenio.ms.companies.ms_companies.companies.repositories.CompanyRepository;
import com.tecmilenio.ms.companies.ms_companies.departments.entities.Department;
import com.tecmilenio.ms.companies.ms_companies.departments.repositories.DepartmentRepository;
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
public class DepartmentRepositoryTest {


    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private Department testDepartment;
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


        testDepartment = Department.builder()
                .fullName("Departamento")
                .company(testCompany)
                .build();
    }


    @Test
    @DisplayName("Should save department succesfully")
    public void departmentRepository_saveDepartment_returnDepartment() {

        companyRepository.save(testCompany);
        Department result = departmentRepository.save(testDepartment);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getFullName()).isEqualTo(testDepartment.getFullName());
    }

    @Test
    @DisplayName("Should find department by id")
    public void departmentRepository_findDepartmentById_returnDepartment() {

        Department savedDepartment = departmentRepository.save(testDepartment);

        Department result = departmentRepository.findById(savedDepartment.getId()).orElse(null);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(savedDepartment.getId());
        Assertions.assertThat(result.getFullName()).isEqualTo(savedDepartment.getFullName());
    }

    @Test
    @DisplayName("Should find all departments")
    public void departmentRepository_findAllDepartments_returnAllDepartments(){

        companyRepository.save(testCompany);
        Department savedDepartment = departmentRepository.save(testDepartment);

        List<Department> result = departmentRepository.findAll();

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get(0).getFullName()).isEqualTo(savedDepartment.getFullName());
    }

    @Test
    @DisplayName("Should delete department")
    public void setDepartmentRepository_deleteDepartment_returnVoid() {

        companyRepository.save(testCompany);

        Department savedDepartment = departmentRepository.save(testDepartment);


        departmentRepository.delete(savedDepartment);

        Department result = departmentRepository.findById(savedDepartment.getId()).orElse(null);
        Assertions.assertThat(result).isEqualTo(null);
    }



}
