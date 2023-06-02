package com.dro.pfgmockfw.model.nomad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JobStatusTypeTest {

    @Test
    public void fromString_validValue_returnsCorrectJobStatusType() {
        // Arrange
        String value = "running";

        // Act
        JobStatusType status = JobStatusType.fromString(value);

        // Assert
        assertEquals(JobStatusType.RUNNING, status);
    }

    @Test
    public void fromString_invalidValue_throwsIllegalArgumentException() {
        // Arrange
        String value = "invalid";

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            JobStatusType.fromString(value);
        });
    }
}