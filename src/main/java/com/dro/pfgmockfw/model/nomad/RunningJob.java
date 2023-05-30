package com.dro.pfgmockfw.model.nomad;

import com.dro.pfgmockfw.serializer.JobStatusDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RunningJob {
    private String id;

    @JsonDeserialize(using = JobStatusDeserializer.class)
    private JobStatus status;
}
