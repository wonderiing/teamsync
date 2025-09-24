package com.tecmilenio.carlos.ms.auth.entities;

public enum Role {
    EMPLOYEE("EMPLOYEE"),
    HR("HR"),
    ADMIN("ADMIN");
    
    private final String value;
    
    Role(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
