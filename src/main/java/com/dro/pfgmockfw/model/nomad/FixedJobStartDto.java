package com.dro.pfgmockfw.model.nomad;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Map;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class FixedJobStartDto {
    @NotBlank
    private String orchestratorUrl;
    @NotBlank
    private String name;
    @NotBlank
    private String version;
    @NotBlank
    private String fileName;

    private Map<String, String> envVariables;
}
