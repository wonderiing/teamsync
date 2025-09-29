package com.tecmilenio.ms.companies.ms_companies.companies.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecmilenio.ms.companies.ms_companies.companies.controllers.CompanyController;
import com.tecmilenio.ms.companies.ms_companies.companies.dto.CompanyDto;
import com.tecmilenio.ms.companies.ms_companies.companies.dto.CreateCompanyDto;
import com.tecmilenio.ms.companies.ms_companies.companies.services.CompanyService;
import com.tecmilenio.ms.companies.ms_companies.companies.services.CompanyServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.hasSize;



@WebMvcTest(CompanyController.class)
@DisplayName("Company controller tests")
@Import(CompanyControllerTest.TestConfig.class)
public class CompanyControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyServiceImpl companyService;

    private CompanyDto companyDto;
    private CreateCompanyDto createCompanyDto;



    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public CompanyServiceImpl companyService() {
            return Mockito.mock(CompanyServiceImpl.class);
        }
    }

    @BeforeEach
    void setUp() {
        companyDto = CompanyDto.builder()
                .id(1L)
                .status("active")
                .fullName("New Company")
                .rfc("2315123")
                .address("address")
                .email("ca223871@gmail.com")
                .telephone("231521")
                .build();

        createCompanyDto = CreateCompanyDto.builder()
                .fullName("New Company")
                .rfc("2315123")
                .address("address")
                .email("ca223871@gmail.com")
                .telephone("231521")
                .build();
    }


    @Test
    @DisplayName("Get /api/v1/companies should get all companies")
    public void companiesController_getAllCompanies_returnCompanies() throws Exception{

        List<CompanyDto> companies = List.of(companyDto);

        ArgumentCaptor<Pageable> argument = ArgumentCaptor.forClass(Pageable.class);

        when(companyService.findAll(argument.capture())).thenReturn(companies);

        mockMvc.perform(get("/api/v1/companies").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email").value(companyDto.getEmail()));

        Pageable pageable = argument.getValue();

        Assertions.assertThat(pageable.getPageSize()).isEqualTo(10);
        Assertions.assertThat(pageable.getPageNumber()).isEqualTo(0);

        verify(companyService).findAll(argument.capture());

    }

    @Test
    @DisplayName("Get /api/v1/companies/id should get company by id")
    public void companyController_getCompanyById_returnCompany() throws Exception {

        when(companyService.findOneById(companyDto.getId())).thenReturn(companyDto);

        mockMvc.perform(get("/api/v1/companies/{id}", companyDto.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(companyDto.getEmail()))
                .andExpect(jsonPath("$.id").value(companyDto.getId()));


        verify(companyService).findOneById(companyDto.getId());
    }


    @Test
    @DisplayName("Post /api/v1/companies should create successfully a company")
    public void companyController_postCompany_returnCompany() throws Exception {

        when(companyService.create(any(CreateCompanyDto.class))).thenReturn(companyDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String createCompanyString = objectMapper.writeValueAsString(createCompanyDto);


        mockMvc.perform(post("/api/v1/companies")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createCompanyString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(companyDto.getId()))
                .andExpect(jsonPath("$.email").value(companyDto.getEmail()));

        verify(companyService).create(ArgumentMatchers.any(CreateCompanyDto.class));
    }

    @Test
    @DisplayName("Put /api/v1/companies/id should update company successfully")
    public void companyController_putCompany_returnCompany() throws Exception {

        when(companyService.update(eq(companyDto.getId()), any(CreateCompanyDto.class))).thenReturn(companyDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String updateCompanyString = objectMapper.writeValueAsString(createCompanyDto);

        mockMvc.perform(put("/api/v1/companies/{id}", companyDto.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateCompanyString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(companyDto.getEmail()));


        verify(companyService).update(eq(companyDto.getId()), ArgumentMatchers.any(CreateCompanyDto.class));
    }

    @Test
    @DisplayName("Delete /api/v1/companies/id should soft delete company")
    public void companyController_deleteCompany_returnNoContent() throws Exception{

        mockMvc.perform(delete("/api/v1/companies/{id}", companyDto.getId()))
                .andExpect(status().isNoContent());

    }



}
