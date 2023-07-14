package com.dro.pfgmockfw.model.nomad;

import com.dro.pfgmockfw.exception.EnumDoesNotExistException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JobStatusTypeTest {

    @Test
    void fromString_validValue_returnsCorrectJobStatusType() {
        //given
        String value = "running";

        //when
        JobStatusType status = JobStatusType.fromString(value);

        //then
        assertEquals(JobStatusType.RUNNING, status);
    }

    @Test
    void fromString_invalidValue_EnumDoesNotExistException() {
        //given
        String value = "invalid";

        //when //then
        assertThrows(EnumDoesNotExistException.class, () -> JobStatusType.fromString(value));
    }
}