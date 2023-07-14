package com.dro.pfgmockfw.serializer;

import com.dro.pfgmockfw.exception.EnumDoesNotExistException;
import com.dro.pfgmockfw.model.nomad.JobStatusType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobStatusDeserializerTest {

    @InjectMocks
    private JobStatusDeserializer deserializer;

    @Mock
    private JsonParser mockParser;

    @Mock
    private DeserializationContext mockContext;


    @Test
    void testDeserialize() throws IOException {
        //given
        String value = "COMPLETE";
        when(mockParser.readValueAs(String.class)).thenReturn(value);

        //when
        JobStatusType result = deserializer.deserialize(mockParser, mockContext);

        //then
        assertEquals(JobStatusType.COMPLETE, result);
    }

    @Test
    void testDeserialize_InvalidJobStatusType() throws IOException {
        //given
        String value = "INVALID_STATUS";
        when(mockParser.readValueAs(String.class)).thenReturn(value);

        // when //then
        assertThrows(EnumDoesNotExistException.class, () -> {
            deserializer.deserialize(mockParser, mockContext);
        });
    }
}