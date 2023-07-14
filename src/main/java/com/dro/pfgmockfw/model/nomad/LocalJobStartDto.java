package com.dro.pfgmockfw.model.nomad;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Map;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class LocalJobStartDto {

    @NotBlank
    private String nomadUrl;

    @NotBlank
    private String fileName;

    private Map<String, String> envs;

}

