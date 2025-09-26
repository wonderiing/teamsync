package com.tecmilenio.ms.employees.ms_employees.repository;

import com.tecmilenio.ms.employees.ms_employees.entities.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee testEmployee;
    private Employee testEmployee2;

    // Arrange
    @BeforeEach
    void setUp() {
        testEmployee = Employee.builder()
                .companyId(2L)
                .departmentId(2L)
                .fullName("Carlos Rodriguez")
                .email("email@gmail.com")
                .telephone("1231421")
                .position("Alguna")
                .status("active")
                .build();

        testEmployee2 = Employee.builder()
                .companyId(3L)
                .departmentId(3L)
                .fullName("Pepe")
                .email("pepe@gmail.com")
                .telephone("123142142")
                .position("Alguna")
                .status("active")
                .build();

    }

    @Test
    @DisplayName("Should save employee succesfully")
    public void employeeRepository_SaveAll_ReturnSavedEmployee() {


        // Act
        Employee savedEmployee = employeeRepository.save(testEmployee);

        //Assert
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(testEmployee.getEmail()).isEqualTo("email@gmail.com");
        Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0L);
    }

    @Test
    @DisplayName("Should auto generate createdAt property")
    public void employeeRepository_autoGenerateCreatedAt() {

        LocalDateTime beforeSave = LocalDateTime.now();

        Employee savedEmployee = employeeRepository.save(testEmployee);

        Assertions.assertThat(savedEmployee.getCreatedAt()).isAfter(beforeSave);
        Assertions.assertThat(savedEmployee.getCreatedAt()).isNotNull();

    }

    @Test
    @DisplayName("Should find employee by id")
    public void employeeRepository_findById() {

        Employee savedEmployee = employeeRepository.save(testEmployee);

        Employee foundEmployee = employeeRepository.findById(savedEmployee.getId()).orElse(null);

        Assertions.assertThat(foundEmployee).isNotNull();
        Assertions.assertThat(foundEmployee.getEmail()).isEqualTo("email@gmail.com");
        Assertions.assertThat(foundEmployee.getId()).isEqualTo(savedEmployee.getId());
    }

    @Test
    @DisplayName("Should delete employee by id")
    public void employeeRepository_Delete() {


        Employee savedEmployee = employeeRepository.save(testEmployee);

        employeeRepository.deleteById(savedEmployee.getId());

        Employee foundEmployee = employeeRepository.findById(savedEmployee.getId()).orElse(null);

        Assertions.assertThat(foundEmployee).isNull();

    }

    @Test
    @DisplayName("Should update employee")
    public void employeeRepository_updateEmployee() {

        Employee savedEmployee = employeeRepository.save(testEmployee);

        savedEmployee.setEmail("nuevo@gmail.com");
        savedEmployee.setPosition("Nueva");

        employeeRepository.save(savedEmployee);

        Assertions.assertThat(savedEmployee.getEmail()).isEqualTo("nuevo@gmail.com");
        Assertions.assertThat(savedEmployee.getPosition()).isEqualTo("Nueva");

    }


    @Test
    @DisplayName("Should find all employees")
    public void employeeRepository_findAll() {

        Employee firstSavedEmployee = employeeRepository.save(testEmployee);
        Employee secondSavedEmployee = employeeRepository.save(testEmployee2);


        List<Employee> employeeList = employeeRepository.findAll();

        Assertions.assertThat(employeeList.size()).isGreaterThanOrEqualTo(2);
        Assertions.assertThat(employeeList)
                .extracting(Employee::getEmail)
                .contains(firstSavedEmployee.getEmail(), secondSavedEmployee.getEmail());

    }



}
