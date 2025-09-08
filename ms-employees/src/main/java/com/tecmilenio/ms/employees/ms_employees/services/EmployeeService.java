package com.tecmilenio.ms.employees.ms_employees.services;

import com.tecmilenio.ms.employees.ms_employees.dto.CompanyDto;
import com.tecmilenio.ms.employees.ms_employees.dto.CreateEmployeeDto;
import com.tecmilenio.ms.employees.ms_employees.dto.DepartmentDto;
import com.tecmilenio.ms.employees.ms_employees.dto.EmployeeDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDto> findAll(Pageable pageable);
    EmployeeDto findEmployeeById(Long id);
    List<EmployeeDto> findEmployeesByCompanyId(Long companyId, Pageable pageable);
    void deleteEmployee(Long id);
    EmployeeDto createEmployee(CreateEmployeeDto createEmployeeDto);
    EmployeeDto updateEmployee(Long id, CreateEmployeeDto createEmployeeDto);
    CompanyDto validateCompany(Long companyId);
    DepartmentDto validateDepartment(Long departmentId);
}
