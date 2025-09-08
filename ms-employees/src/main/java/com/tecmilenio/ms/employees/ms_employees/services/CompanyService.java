package com.tecmilenio.ms.employees.ms_employees.services;

import com.tecmilenio.ms.employees.ms_employees.dto.CompanyDto;
import com.tecmilenio.ms.employees.ms_employees.dto.DepartmentDto;

public interface CompanyService {

    CompanyDto findCompanyById(Long id);
    DepartmentDto findDepartmentById(Long id);
}
