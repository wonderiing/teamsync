package com.tecmilenio.ms.employees.ms_employees.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public class CreateEmployeeDto {


    @NotNull(message = "comapany id cannot be null")
    private Long companyId;

    @NotNull(message = "department id cannot be null")
    private Long departmentId;

    @NotBlank(message = "fullName cannot be blank")
    @Size(min = 5)
    private String fullName;

    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "telephone cannot be blank")
    private String telephone;

    @NotBlank(message = "position cannot be blank")
    private String position;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
