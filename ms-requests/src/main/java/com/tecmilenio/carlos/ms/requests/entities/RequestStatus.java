package com.tecmilenio.carlos.ms.requests.entities;

public enum RequestStatus {
    RECEIVED("received", "Recibido"),
    IN_REVIEW("in_review", "En Revisión"),
    APPROVED("approved", "Aprobado"),
    REJECTED("rejected", "Rechazado"),
    CLOSED("closed", "Cerrado");

    private final String value;
    private final String displayName;

    RequestStatus(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }
}
