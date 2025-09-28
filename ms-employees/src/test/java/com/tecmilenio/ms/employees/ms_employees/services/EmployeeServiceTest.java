package com.tecmilenio.ms.employees.ms_employees.services;


import com.tecmilenio.ms.employees.ms_employees.dto.CompanyDto;
import com.tecmilenio.ms.employees.ms_employees.dto.CreateEmployeeDto;
import com.tecmilenio.ms.employees.ms_employees.dto.DepartmentDto;
import com.tecmilenio.ms.employees.ms_employees.dto.EmployeeDto;
import com.tecmilenio.ms.employees.ms_employees.entities.Employee;
import com.tecmilenio.ms.employees.ms_employees.mapper.EmployeeMapper;
import com.tecmilenio.ms.employees.ms_employees.repository.EmployeeRepository;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Employee service Tests")
public class EmployeeServiceTest {


    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CompanyService companyService;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    public Employee employee;
    public CreateEmployeeDto createEmployeeDto;
    public Employee savedEmployee;
    public CompanyDto companyDto;
    public DepartmentDto departmentDto;
    public EmployeeDto employeeDto;
    public CreateEmployeeDto updatedEmployee;
    public EmployeeDto upEmployeeDto;

    @BeforeEach
    void setUp() {

        employee = Employee.builder()
                .id(1L)
                .email("ca223871@gmail.com")
                .position("Alguna")
                .status("Active")
                .companyId(3L)
                .departmentId(3L)
                .telephone("2319512")
                .fullName("PEPEPEPE")
                .build();

        employeeDto = EmployeeDto.builder()
                .id(1L)
                .email("ca223871@gmail.com")
                .position("Alguna")
                .status("Active")
                .companyId(3L)
                .departmentId(3L)
                .telephone("2319512")
                .fullName("PEPEPEPE")
                .build();

        upEmployeeDto = EmployeeDto.builder()
                .id(1L)
                .email("ca223871@gmail.com")
                .position("Alguna")
                .status("Active")
                .companyId(3L)
                .departmentId(3L)
                .telephone("2319512")
                .fullName("Nuevo")
                .build();

        createEmployeeDto = CreateEmployeeDto.builder()
                .companyId(3L)
                .departmentId(3L)
                .fullName("PEPEPEPE")
                .email("ca223871@gmail.com")
                .position("Alguna")
                .telephone("2319512")
                .build();

        updatedEmployee = CreateEmployeeDto.builder()
                .companyId(3L)
                .departmentId(3L)
                .fullName("Nuevo")
                .email("ca223871@gmail.com")
                .position("Alguna")
                .telephone("2319512")
                .build();

        savedEmployee =  Employee.builder()
                .id(1L)
                .email("ca223871@gmail.com")
                .position("Alguna")
                .status("Active")
                .companyId(3L)
                .departmentId(3L)
                .telephone("2319512")
                .fullName("PEPEPEPE")
                .build();

        companyDto = CompanyDto.builder()
                .id(3L)
                .fullName("Empresa")
                .rfc("29381")
                .address("Circuito")
                .status("active")
                .email("email@email.com")
                .telephone("29038123")
                .build();

        departmentDto = DepartmentDto.builder()
                .id(3L)
                .fullName("Departamento")
                .companyId(3L)
                .build();

    }


    @Test
    @DisplayName("Service should save and return employee")
    public void employeeService_save_returnSavedEmployee() {

        // Stubbs: Comportamiento esperado de los mocks cuando son ejecutado
        when(companyService.findCompanyById(3L)).thenReturn(companyDto);
        when(companyService.findDepartmentById(3L)).thenReturn(departmentDto);
        when(employeeMapper.toEntity(createEmployeeDto)).thenReturn(employee);
        when(employeeMapper.toDto(any(Employee.class))).thenReturn(employeeDto);
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);


        EmployeeDto result = employeeService.createEmployee(createEmployeeDto);

        Assertions.assertThat(result.getId()).isEqualTo(1L);
        Assertions.assertThat(result.getFullName()).isEqualTo("PEPEPEPE");

        // Verify: verifica que los stubbs realmente hayan sido ejecutados con los parametros definidos
        verify(companyService).findCompanyById(3L);
        verify(companyService).findDepartmentById(3L);

    }

    @Test
    @DisplayName("Service should return all employees")
    public void employeeService_findAll_returnAllEmployees() {


        Pageable pageable = PageRequest.of(0, 10);

        List<Employee> employees = List.of(employee);

        Page<Employee> page = new PageImpl<>(employees, pageable, employees.size());

        when(employeeRepository.findAll(pageable)).thenReturn(page);
        when(employeeMapper.toDto(employee)).thenReturn(employeeDto);

        List<EmployeeDto> result = employeeService.findAll(pageable);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get(0).getEmail()).isEqualTo("ca223871@gmail.com");

        verify(employeeRepository).findAll(pageable);
        verify(employeeMapper).toDto(employee);

    }

    @Test
    @DisplayName("Should return employee by id")
    public void employeeService_findEmployeeById_returnEmployee(){


        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeeMapper.toDto(any(Employee.class))).thenReturn(employeeDto);

        EmployeeDto result = employeeService.findEmployeeById(employee.getId());

        Assertions.assertThat(result.getEmail()).isEqualTo(employee.getEmail());
        Assertions.assertThat(result.getId()).isEqualTo(employee.getId());

        verify(employeeRepository).findById(employee.getId());
    }

    @Test
    @DisplayName("Should set employee status to inactive")
    public void employeeService_softDeleteEmployee_returnNoContent(){

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(employee.getId());

        Assertions.assertThat(employee.getStatus()).isEqualTo("inactive");
        verify(employeeRepository).findById(employee.getId());

    }


    @Test
    @DisplayName("Should update employee")
    public void employeeService_updateEmployee_returnNewEmployee(){

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(companyService.findCompanyById(3L)).thenReturn(companyDto);
        when(companyService.findDepartmentById(3L)).thenReturn(departmentDto);
        when(employeeMapper.toDto(any(Employee.class))).thenReturn(upEmployeeDto);
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));


        EmployeeDto result = employeeService.updateEmployee(employee.getId(), updatedEmployee);

        Assertions.assertThat(result.getFullName()).isEqualTo("Nuevo");
        Assertions.assertThat(result.getId()).isEqualTo(employee.getId());

        verify(employeeRepository).findById(employee.getId());
        verify(companyService).findCompanyById(3L);
        verify(companyService).findDepartmentById(3L);
        verify(employeeRepository).save(any(Employee.class));

    }


}
