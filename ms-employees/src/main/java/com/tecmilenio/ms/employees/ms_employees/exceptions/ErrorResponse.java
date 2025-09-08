package com.tecmilenio.ms.employees.ms_employees.exceptions;

import java.time.LocalDateTime;

public class ErrorResponse {

    public LocalDateTime timestamp;
    public String error;
    public int value;

    public ErrorResponse(LocalDateTime timestamp, String error, int value) {
        this.timestamp = timestamp;
        this.error = error;
        this.value = value;
    }
}
