package com.tecmilenio.ms.companies.ms_companies.companies.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;


@Builder
public class CreateCompanyDto {

    @NotBlank(message = "fullName cannot be blank")
    private String fullName;

    @NotBlank(message = "rfc cannot be blank")
    private String rfc;

    @NotBlank(message = "address cannot be blank")
    private String address;

    @Email(message = "Not valid email")
    private String email;

    @NotBlank(message = "telephone cannot be blank")
    private String telephone;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
