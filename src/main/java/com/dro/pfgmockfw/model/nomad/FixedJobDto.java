package com.dro.pfgmockfw.model.nomad;

import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class FixedJobDto {
    private String appName;
    private String appVersion;
    private String fileName;
}
