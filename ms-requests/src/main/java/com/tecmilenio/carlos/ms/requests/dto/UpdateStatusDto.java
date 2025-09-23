package com.tecmilenio.carlos.ms.requests.dto;

import com.tecmilenio.carlos.ms.requests.entities.RequestStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusDto {

    @NotNull(message = "El estado es obligatorio")
    private RequestStatus status;

    private String reason;

    // Constructors
    public UpdateStatusDto() {}

    public UpdateStatusDto(RequestStatus status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    // Getters and Setters
    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
