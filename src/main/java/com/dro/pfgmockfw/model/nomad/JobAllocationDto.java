package com.dro.pfgmockfw.model.nomad;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.jackson.Jacksonized;


@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
public class JobAllocationDto {

    @NotBlank
    @JsonProperty("ID")
    private String id;

    @NotBlank
    @JsonProperty("JobVersion")
    private String jobVersion;

}

