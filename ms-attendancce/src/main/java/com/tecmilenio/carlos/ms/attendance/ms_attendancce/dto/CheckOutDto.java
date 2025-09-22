package com.tecmilenio.carlos.ms.attendance.ms_attendancce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CheckOutDto {

    @NotNull(message = "El ID del empleado es obligatorio")
    @Positive(message = "El ID del empleado debe ser positivo")
    private Long idEmployee;

    private String notes;

    // Constructors
    public CheckOutDto() {}

    public CheckOutDto(Long idEmployee, String notes) {
        this.idEmployee = idEmployee;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
