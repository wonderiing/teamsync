package com.tecmilenio.ms.companies.ms_companies.common.exceptions;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(String message) {
        super(message);
    }
}
