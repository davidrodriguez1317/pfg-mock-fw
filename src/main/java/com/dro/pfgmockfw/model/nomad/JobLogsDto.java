package com.dro.pfgmockfw.model.nomad;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class JobLogsDto {
    @JsonProperty("Data")
    private String data;
}

