package com.tecmilenio.ms.companies.ms_companies.departments.dto;

import com.tecmilenio.ms.companies.ms_companies.companies.entities.Company;

public class DepartmentDto {

    private Long id;
    private String fullName;
    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
