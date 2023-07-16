package com.dro.pfgmockfw.model.nomad;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Map;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class JobStartDto {
    @NotBlank
    private String platformUrl;

    @NotBlank
    private String orchestratorUrl;

    @NotBlank
    private String appName;

    @NotBlank
    private String appVersion;

    private Map<String, String> envs;

}

