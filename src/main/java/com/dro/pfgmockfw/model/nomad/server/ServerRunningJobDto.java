package com.dro.pfgmockfw.model.nomad.server;

import com.dro.pfgmockfw.model.nomad.JobStatusType;
import com.dro.pfgmockfw.serializer.JobStatusDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServerRunningJobDto {
    @JsonProperty("ID")
    private String id;

    @JsonProperty("Status")
    @JsonDeserialize(using = JobStatusDeserializer.class)
    private JobStatusType status;
}
