package com.tecmilenio.ms.employees.ms_employees.mapper;

import com.tecmilenio.ms.employees.ms_employees.dto.CreateEmployeeDto;
import com.tecmilenio.ms.employees.ms_employees.dto.EmployeeDto;
import com.tecmilenio.ms.employees.ms_employees.entities.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeDto toDto(Employee employee);

    Employee toEntity(CreateEmployeeDto createEmployeeDto);

}
