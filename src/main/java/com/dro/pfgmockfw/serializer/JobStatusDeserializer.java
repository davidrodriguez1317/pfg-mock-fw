package com.dro.pfgmockfw.serializer;

import com.dro.pfgmockfw.model.nomad.JobStatusType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class JobStatusDeserializer extends StdDeserializer<JobStatusType> {

    public JobStatusDeserializer() {
        super(JobStatusType.class);
    }

    @Override
    public JobStatusType deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String value = parser.readValueAs(String.class);
        return JobStatusType.fromString(value);
    }
}