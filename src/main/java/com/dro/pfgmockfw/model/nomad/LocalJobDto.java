package com.dro.pfgmockfw.model.nomad;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Map;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class LocalJobDto {
    @NotBlank
    private String nomadUrl;
    @NotBlank
    private String jarName;
    @NotBlank
    private String name;

    //private Map<String, String> envVariables;
}
