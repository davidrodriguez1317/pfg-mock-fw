package com.dro.pfgmockfw.model.nomad;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class JobStopDto {

    @NotBlank
    private String nomadUrl;

    @NotBlank
    private String jobId;

}

