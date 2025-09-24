package com.tecmilenio.carlos.ms.auth.dto;

public class LoginResponse {
    
    private String token;
    private String type = "Bearer";
    private String username;
    private String role;
    private Long employeeId;
    private Long companyId;
    
    // Constructors
    public LoginResponse() {}
    
    public LoginResponse(String token, String username, String role, Long employeeId, Long companyId) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.employeeId = employeeId;
        this.companyId = companyId;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
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
