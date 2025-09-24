package com.tecmilenio.carlos.ms.auth.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(name = "username", unique = true, length = 50)
    private String username;
    
    @NotBlank
    @Email
    @Column(name = "email", unique = true, length = 100)
    private String email;
    
    @NotBlank
    @Column(name = "password", length = 255)
    private String password;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20)
    private Role role;
    
    @Column(name = "employee_id")
    private Long employeeId;
    
    @Column(name = "company_id")
    private Long companyId;
    
    // Constructors
    public User() {}
    
    public User(String username, String email, String password, Role role, Long employeeId, Long companyId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.employeeId = employeeId;
        this.companyId = companyId;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public Long getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
    
    public Long getCompanyId() {
        return companyId;
    }
    
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
