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
public class ServerJobStopDto {
    @JsonProperty("EvalID")
    private String evalId;

}
