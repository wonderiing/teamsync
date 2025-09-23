package com.tecmilenio.carlos.ms.requests.entities;

public enum RequestType {
    CERTIFICATE("certificate", "Constancia"),
    REIMBURSEMENT("reimbursement", "Reembolso"),
    INCIDENT("incident", "Incidencia"),
    OTHER("other", "Otro"),
    INFORMATION("information", "Informacion"),
    VACATIONS("vacations", "Vacaciones");

    private final String value;
    private final String displayName;

    RequestType(String value, String displayName) {
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
