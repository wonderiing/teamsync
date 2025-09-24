package com.tecmilenio.carlos.ms.auth.dto;

public class UserInfo {
    
    private String username;
    private String role;
    private Long employeeId;
    private Long companyId;
    
    // Constructors
    public UserInfo() {}
    
    public UserInfo(String username, String role, Long employeeId, Long companyId) {
        this.username = username;
        this.role = role;
        this.employeeId = employeeId;
        this.companyId = companyId;
    }
    
    // Getters and Setters
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
