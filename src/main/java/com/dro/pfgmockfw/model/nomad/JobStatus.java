package com.dro.pfgmockfw.model.nomad;

public enum JobStatus {
    PENDING("pending"),
    RUNNING("running"),
    DEAD("dead"),
    FAILED("failed"),
    COMPLETE("complete"),
    LOST("lost");

    private final String value;

    JobStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static JobStatus fromString(String value) {
        for (JobStatus status : JobStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid JobStatus: " + value);
    }
}
