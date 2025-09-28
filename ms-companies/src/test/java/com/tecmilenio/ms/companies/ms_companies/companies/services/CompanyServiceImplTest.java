package com.tecmilenio.ms.companies.ms_companies.companies.services;

import com.tecmilenio.ms.companies.ms_companies.companies.dto.CompanyDto;
import com.tecmilenio.ms.companies.ms_companies.companies.dto.CreateCompanyDto;
import com.tecmilenio.ms.companies.ms_companies.companies.entities.Company;
import com.tecmilenio.ms.companies.ms_companies.companies.mappers.CompanyMapper;
import com.tecmilenio.ms.companies.ms_companies.companies.repositories.CompanyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Company Service Impl Tests")
public class CompanyServiceImplTest {


    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private CompanyServiceImpl companyService;


    private Company testCompany;
    private Company savedCompany;
    private CompanyDto companyDto;
    private CreateCompanyDto createCompanyDto;


    @BeforeEach
    void setUp() {

        testCompany = Company.builder()
                .id(1L)
                .status("active")
                .createdAt(LocalDateTime.now())
                .fullName("New Company")
                .rfc("2315123")
                .address("address")
                .email("ca223871@gmail.com")
                .telephone("231521")
                .build();

        companyDto = CompanyDto.builder()
                .id(1L)
                .status("active")
                .fullName("New Company")
                .rfc("2315123")
                .address("address")
                .email("ca223871@gmail.com")
                .telephone("231521")
                .build();

        savedCompany =  Company.builder()
                .id(1L)
                .status("active")
                .createdAt(LocalDateTime.now())
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
    @DisplayName("Service should save company succesfull")
    public void companyService_saveCompany_returnCompany() {

        when(companyMapper.toEntity(any(CreateCompanyDto.class))).thenReturn(testCompany);
        when(companyMapper.toDto(any(Company.class))).thenReturn(companyDto);
        when(companyRepository.save(any(Company.class))).thenReturn(savedCompany);


        CompanyDto result = companyService.create(createCompanyDto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getEmail()).isEqualTo(createCompanyDto.getEmail());
        Assertions.assertThat(result.getId()).isEqualTo(1L);


        verify(companyMapper).toDto(any(Company.class));
        verify(companyRepository).save(any(Company.class));

    }


    @Test
    @DisplayName("Service should return all companies paginateed")
    public void companyService_GetAll_returnAllCompaniesPaginated(){

        Pageable pageable = PageRequest.of(0,10);
        List<Company> companies = List.of(testCompany);

        Page<Company> companyPage = new PageImpl<>(companies, pageable, companies.size());

        when(companyRepository.findAll(pageable)).thenReturn(companyPage);
        when(companyMapper.toDto(any(Company.class))).thenReturn(companyDto);

        List<CompanyDto> result = companyService.findAll(pageable);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get(0).getEmail()).isEqualTo(testCompany.getEmail());

        verify(companyRepository).findAll(pageable);
        verify(companyMapper).toDto(any(Company.class));


    }

    @Test
    @DisplayName("Service should find company by id")
    public void companyService_FindCompanyById_returnCompany(){

        when(companyRepository.findById(1L)).thenReturn(Optional.of(testCompany));
        when(companyMapper.toDto(any(Company.class))).thenReturn(companyDto);

        CompanyDto result = companyService.findOneById(1L);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getEmail()).isEqualTo(companyDto.getEmail());


        verify(companyRepository).findById(1L);
        verify(companyMapper).toDto(any(Company.class));
    }

    @Test
    @DisplayName("Service should soft delete the company")
    public void companyService_SoftDelete_returnVoid() {

        when(companyRepository.findById(1L)).thenReturn(Optional.of(testCompany));

        companyService.delete(testCompany.getId());

        Assertions.assertThat(testCompany.getStatus()).isEqualTo("inactive");


        verify(companyRepository).findById(1L);
        verify(companyRepository).save(testCompany);
    }


}
