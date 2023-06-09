package com.dro.pfgmockfw.model.nomad;

import com.dro.pfgmockfw.exception.EnumDoesNotExistException;

public enum JobStatusType {
    PENDING("pending"),
    RUNNING("running"),
    DEAD("dead"),
    FAILED("failed"),
    COMPLETE("complete"),
    LOST("lost");

    private final String value;

    JobStatusType(String value) {
        this.value = value;
    }

    public static JobStatusType fromString(String value) {
        for (JobStatusType status : JobStatusType.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new EnumDoesNotExistException("Invalid JobStatus: " + value);
    }
}
