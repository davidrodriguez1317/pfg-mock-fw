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
    private String name;
    @NotBlank
    private String version;
    @NotBlank
    private String fileName;
}
