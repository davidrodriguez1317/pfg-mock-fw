package com.dro.pfgmockfw.model.nomad;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
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

