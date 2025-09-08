package com.tecmilenio.ms.employees.ms_employees.clients;

import com.tecmilenio.ms.employees.ms_employees.dto.CompanyDto;
import com.tecmilenio.ms.employees.ms_employees.dto.DepartmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-companies")
public interface CompanyFeignClient {

    @GetMapping("/api/v1/companies/{id}")
    CompanyDto findCompanyById(@PathVariable Long id);

    @GetMapping("/api/v1/departments/{id}")
    DepartmentDto findDepartmentById(@PathVariable Long id);

}
