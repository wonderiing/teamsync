package com.tecmilenio.ms.employees.ms_employees.services;

import com.tecmilenio.ms.employees.ms_employees.clients.CompanyFeignClient;
import com.tecmilenio.ms.employees.ms_employees.dto.CompanyDto;
import com.tecmilenio.ms.employees.ms_employees.dto.DepartmentDto;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService{
    private final CompanyFeignClient client;

    public CompanyServiceImpl(CompanyFeignClient client) {
        this.client = client;
    }

    @Override
    public CompanyDto findCompanyById(Long id) {
        return client.findCompanyById(id);
    }

    @Override
    public DepartmentDto findDepartmentById(Long id) {
        return client.findDepartmentById(id);
    }
}
