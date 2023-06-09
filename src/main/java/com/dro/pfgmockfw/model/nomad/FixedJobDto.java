package com.dro.pfgmockfw.model.nomad;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class FixedJobDto {
    @NotBlank
    private String nomadUrl;
    @NotBlank
    private String appName;
    @NotBlank
    private String appVersion;
    @NotBlank
    private String fileName;
}