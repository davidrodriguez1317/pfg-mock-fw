package com.dro.pfgmockfw.model.nomad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RunningJobs {
    private List<RunningJob> runningJobs;
}
