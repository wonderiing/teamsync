package com.tecmilenio.ms.companies.ms_companies.common.errors;

import java.time.LocalDateTime;

public class ErrorResponse {

    private LocalDateTime timestamp;
    private String error;
    private int status;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ErrorResponse(LocalDateTime timestamp, String error, int status) {
        this.timestamp = timestamp;
        this.error = error;
        this.status = status;
    }


}
