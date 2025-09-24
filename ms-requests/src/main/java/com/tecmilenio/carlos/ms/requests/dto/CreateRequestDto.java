package com.tecmilenio.carlos.ms.requests.dto;

import com.tecmilenio.carlos.ms.requests.entities.RequestType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateRequestDto {

    // idEmployee se extrae del token JWT, no se requiere en el body
    private Long idEmployee;

    @NotNull(message = "El tipo de solicitud es obligatorio")
    private RequestType requestType;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no puede exceder los 100 caracteres")
    private String title;

    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    private String description;

    // Constructors
    public CreateRequestDto() {}

    public CreateRequestDto(RequestType requestType, String title, String description) {
        this.requestType = requestType;
        this.title = title;
        this.description = description;
    }

    // Getters and Setters
    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
