package com.tecmilenio.ms.companies.ms_companies.departments.services;

import com.tecmilenio.ms.companies.ms_companies.departments.dto.CreateDepartmentDto;
import com.tecmilenio.ms.companies.ms_companies.departments.dto.DepartmentDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {

    List<DepartmentDto> findAll(Pageable pageable);
    DepartmentDto createDepartment(CreateDepartmentDto createDepartmentDto);
    void deleteDepartment(Long id);
    List<DepartmentDto> findByCompanyId(Long id);
    DepartmentDto findById(Long id);

}
