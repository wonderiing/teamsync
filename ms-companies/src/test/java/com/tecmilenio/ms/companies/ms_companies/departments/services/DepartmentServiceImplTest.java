package com.tecmilenio.ms.companies.ms_companies.departments.services;

import com.tecmilenio.ms.companies.ms_companies.companies.entities.Company;
import com.tecmilenio.ms.companies.ms_companies.companies.repositories.CompanyRepository;
import com.tecmilenio.ms.companies.ms_companies.departments.dto.CreateDepartmentDto;
import com.tecmilenio.ms.companies.ms_companies.departments.dto.DepartmentDto;
import com.tecmilenio.ms.companies.ms_companies.departments.entities.Department;
import com.tecmilenio.ms.companies.ms_companies.departments.mapper.DepartmentMapper;
import com.tecmilenio.ms.companies.ms_companies.departments.repositories.DepartmentRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Department Service tests")
public class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;


    public Department department;
    public CreateDepartmentDto createDepartmentDto;
    public DepartmentDto departmentDto;
    public Company testCompany;


    @BeforeEach
    void setUp() {
        testCompany = Company.builder()
                .id(1L)
                .fullName("New Company")
                .rfc("2315123")
                .address("address")
                .email("ca223871@gmail.com")
                .telephone("231521")
                .createdAt(LocalDateTime.now())
                .build();


        department = Department.builder()
                .id(1L)
                .fullName("Departamento")
                .company(testCompany)
                .createdAt(LocalDateTime.now())
                .build();

        departmentDto = DepartmentDto.builder().
                id(1L)
                .fullName("Departamento")
                .companyId(1L)
                .build();

        createDepartmentDto = CreateDepartmentDto.builder()
                .fullName("Departamento")
                .companyId(1L)
                .build();
    }

    @Test
    @DisplayName("Should return all departments paginated")
    public void departmentService_findAll_returnAllDepartments(){

        Pageable pageable = PageRequest.of(0,10);
        List<Department> departmentList = List.of(department);
        Page<Department> page = new PageImpl<>(departmentList, pageable, departmentList.size());

        when(departmentRepository.findAll(pageable)).thenReturn(page);
        when(departmentMapper.toDto(any(Department.class))).thenReturn(departmentDto);


        List<DepartmentDto> result = departmentService.findAll(pageable);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get(0).getFullName()).isEqualTo(departmentDto.getFullName());


        verify(departmentRepository).findAll(pageable);
        verify(departmentMapper).toDto(any(Department.class));
    }

    @Test
    @DisplayName("Should create a department")
    public void departmentService_createDepartment_returnDepartment() {

        when(companyRepository.findById(1L)).thenReturn(Optional.of(testCompany));
        when(departmentMapper.toDto(any(Department.class))).thenReturn(departmentDto);
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        DepartmentDto result = departmentService.createDepartment(createDepartmentDto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getFullName()).isEqualTo(createDepartmentDto.getFullName());
        Assertions.assertThat(result.getId()).isEqualTo(1L);

        verify(companyRepository).findById(1L);
        verify(departmentMapper).toDto(any(Department.class));
        verify(departmentRepository).save(any(Department.class));

    }

    @Test
    @DisplayName("Should find department by id")
    public void departmentService_findById_returnDepartment() {


        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));
        when(departmentMapper.toDto(any(Department.class))).thenReturn(departmentDto);

        DepartmentDto result = departmentService.findById(department.getId());

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(department.getId());
        Assertions.assertThat(result.getFullName()).isEqualTo(department.getFullName());


        verify(departmentRepository).findById(department.getId());
        verify(departmentMapper).toDto(any(Department.class));

    }

    @Test
    @DisplayName("Should find company by id")
    public void departmentService_findAllByCompanyId_returnAllDepartments() {

        when(companyRepository.existsById(department.getCompany().getId())).thenReturn(true);
        when(departmentMapper.toDto(any(Department.class))).thenReturn(departmentDto);

        List<Department> departments = List.of(department);
        when(departmentRepository.findByCompanyId(department.getCompany().getId())).thenReturn(departments);

        List<DepartmentDto> result = departmentService.findByCompanyId(department.getCompany().getId());

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get(0).getId()).isEqualTo(department.getId());

        verify(companyRepository).existsById(department.getCompany().getId());
        verify(departmentMapper).toDto(any(Department.class));
        verify(departmentRepository).findByCompanyId(department.getCompany().getId());

    }

    @Test
    @DisplayName("Should delete department")
    public void departmentService_deleteDepartment_returnVoid() {

        when(departmentRepository.findById(department.getId())).thenReturn(Optional.of(department));

        departmentService.deleteDepartment(department.getId());

        // Verificamos que el servicio haya llamado al delete del repository
        verify(departmentRepository).findById(department.getId());
        verify(departmentRepository).delete(department);
    }

}
