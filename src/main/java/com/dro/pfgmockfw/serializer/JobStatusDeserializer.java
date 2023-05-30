package com.dro.pfgmockfw.serializer;

import com.dro.pfgmockfw.model.nomad.JobStatus;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class JobStatusDeserializer extends StdDeserializer<JobStatus> {

    public JobStatusDeserializer() {
        super(JobStatus.class);
    }

    @Override
    public JobStatus deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String value = parser.readValueAs(String.class);
        return JobStatus.fromString(value);
    }
}