package com.tecmilenio.carlos.ms.auth.dto;

public class RegisterResponse {
    
    private String message;
    private String username;
    private String role;
    private Long employeeId;
    private Long companyId;
    
    // Constructors
    public RegisterResponse() {}
    
    public RegisterResponse(String message, String username, String role, Long employeeId, Long companyId) {
        this.message = message;
        this.username = username;
        this.role = role;
        this.employeeId = employeeId;
        this.companyId = companyId;
    }
    
    // Getters and Setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
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
