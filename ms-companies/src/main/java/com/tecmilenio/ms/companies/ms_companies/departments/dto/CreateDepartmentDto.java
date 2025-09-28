package com.tecmilenio.ms.companies.ms_companies.departments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public class CreateDepartmentDto {

        @NotBlank(message = "fullName cannot be blank")
        private String fullName;

        @NotNull(message = "company id cannot be null")
        private Long companyId;

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
