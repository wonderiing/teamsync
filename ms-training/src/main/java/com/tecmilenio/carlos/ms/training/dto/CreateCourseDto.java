package com.tecmilenio.carlos.ms.training.dto;

import jakarta.validation.constraints.*;

public class CreateCourseDto {

    @NotNull(message = "El ID de la empresa es obligatorio")
    @Positive(message = "El ID de la empresa debe ser positivo")
    private Long idCompany;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no puede exceder los 100 caracteres")
    private String title;

    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    private String description;

    @Min(value = 1, message = "La duración debe ser al menos 1 minuto")
    @Max(value = 600, message = "La duración no puede exceder las 600 minutos")
    private Integer durationMinutes;

    @NotBlank(message = "La URL del tutorial es obligatoria")
    @Size(max = 500, message = "La URL no puede exceder los 500 caracteres")
    private String tutorialUrl;

    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 50, message = "La categoría no puede exceder los 50 caracteres")
    private String category;

    // Constructors
    public CreateCourseDto() {}

    public CreateCourseDto(Long idCompany, String title, String description, Integer durationMinutes, String tutorialUrl, String category) {
        this.idCompany = idCompany;
        this.title = title;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.tutorialUrl = tutorialUrl;
        this.category = category;
    }

    // Getters and Setters
    public Long getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(Long idCompany) {
        this.idCompany = idCompany;
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

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getTutorialUrl() {
        return tutorialUrl;
    }

    public void setTutorialUrl(String tutorialUrl) {
        this.tutorialUrl = tutorialUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
