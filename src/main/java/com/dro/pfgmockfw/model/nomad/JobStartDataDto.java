package com.dro.pfgmockfw.model.nomad;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class JobStartDataDto {
    @NotBlank
    private String dockerUrl;

    @NotBlank
    private String nomadUrl;

    @NotBlank
    private String appName;

    @NotBlank
    private String appVersion;

}

