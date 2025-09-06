package com.tecmilenio.ms.companies.ms_companies.departments.mapper;

import com.tecmilenio.ms.companies.ms_companies.companies.dto.CompanyDto;
import com.tecmilenio.ms.companies.ms_companies.departments.dto.DepartmentDto;
import com.tecmilenio.ms.companies.ms_companies.departments.entities.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(target = "companyId", source = "company.id")
    DepartmentDto toDto(Department department);


}
