package com.tecmilenio.ms.employees.ms_employees.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecmilenio.ms.employees.ms_employees.controllers.EmployeeController;
import com.tecmilenio.ms.employees.ms_employees.dto.CreateEmployeeDto;
import com.tecmilenio.ms.employees.ms_employees.dto.EmployeeDto;
import com.tecmilenio.ms.employees.ms_employees.services.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;



@Slf4j
@WebMvcTest(EmployeeController.class)
@DisplayName("Employee Controller Tests")
@Import(EmployeeControllerTest.TestConfig.class)
public class EmployeeControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeServiceImpl employeeService;



    private EmployeeDto testEmployee;
    private CreateEmployeeDto createEmployeeDto;



    // Mockeamos EmployeeService
    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public EmployeeServiceImpl employeeService() {
            return Mockito.mock(EmployeeServiceImpl.class);
        }
    }

    @BeforeEach
    void setUp() {

        testEmployee = EmployeeDto.builder()
                .id(1L)
                .companyId(3L)
                .departmentId(3L)
                .fullName("Namee")
                .email("email@email.com")
                .telephone("321412")
                .position("Nueva")
                .status("active")
                .build();

        createEmployeeDto = CreateEmployeeDto.builder()
                .companyId(3L)
                .departmentId(3L)
                .fullName("Namee")
                .email("email@email.com")
                .position("Nueva")
                .telephone("321412")
                .build();

    }


    @Test
    @DisplayName("Get /api/v1/employees should return all employees")
    public void employeeController_getAllEmployees_ReturnListOfEmployees() throws Exception {

        List<EmployeeDto> employeeList = List.of(testEmployee);


        //Captura el argumento que se le pase al employeeService osea pageable
        ArgumentCaptor<Pageable> cap = ArgumentCaptor.forClass(Pageable.class);
        when(employeeService.findAll(cap.capture())).thenReturn(employeeList);

        mockMvc.perform(
                get("/api/v1/employees").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // $ es la raiz del Json - Espera un Json con 1 objeto
                .andExpect(jsonPath("$", hasSize(1)))
                //$[0].fullName, es el fullName del primer objeto del json
                .andExpect(jsonPath("$[0].fullName").value("Namee"));


        Pageable pageable = cap.getValue();

        //Verifica que los valores de pageable, sean los mismo que los default
        Assertions.assertThat(pageable.getPageNumber()).isEqualTo(0);
        Assertions.assertThat(pageable.getPageSize()).isEqualTo(10);
        verify(employeeService).findAll(ArgumentMatchers.any(Pageable.class));

    }

    @Test
    @DisplayName("Get by Id /api/v1/employees/id should return employee")
    public void employeeController_getEmployeeById_returnEmployee() throws Exception {

        Long testEmployeeId = testEmployee.getId();
        when(employeeService.findEmployeeById(testEmployeeId)).thenReturn(testEmployee);

        mockMvc.perform(get("/api/v1/employees/{id}", testEmployeeId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(testEmployee.getEmail()))
                .andExpect(jsonPath("$.fullName").value("Namee"));


        verify(employeeService).findEmployeeById(testEmployeeId);

    }

    @Test
    @DisplayName("Post /api/v1/employees should create and return employee")
    public void employeeController_createEmployee_returnEmployee() throws Exception {

        when(employeeService.createEmployee(
                ArgumentMatchers.any(CreateEmployeeDto.class)))
                .thenReturn(testEmployee);

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(createEmployeeDto);

        mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value(testEmployee.getFullName()))
                .andExpect(jsonPath("$.telephone").value(testEmployee.getTelephone()));


        verify(employeeService).createEmployee(ArgumentMatchers.any(CreateEmployeeDto.class));

    }


    @Test
    @DisplayName("Put /api/v1/employees/id should update and return employee")
    public void employeeController_updateEmployee_returnEmployee() throws Exception {

        // eq - le dice al metodo, espera especificamente este valor
        when(employeeService.updateEmployee(eq(testEmployee.getId()),
                ArgumentMatchers.any(CreateEmployeeDto.class)))
                .thenReturn(testEmployee);

        ObjectMapper objectMapper = new ObjectMapper();

        String employeeJson = objectMapper.writeValueAsString(createEmployeeDto);

        mockMvc.perform(put("/api/v1/employees/{id}",testEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value(testEmployee.getFullName()))
                .andExpect(jsonPath("$.email").value(testEmployee.getEmail()));

        verify(employeeService).updateEmployee(
                eq(testEmployee.getId()),
                ArgumentMatchers.any(CreateEmployeeDto.class));


    }

    @Test
    @DisplayName("Delete /api/v1/employees/id should delete employees")
    public void employeeController_deleteEmployee_returnNoContent() throws Exception {

        mockMvc.perform(delete("/api/v1/employees/{id}", testEmployee.getId()))
                .andExpect(status().isNoContent());

    }
}
