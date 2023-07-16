package com.dro.pfgmockfw.service.interfaces;

import com.dro.pfgmockfw.model.nomad.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



public interface OrchestratorService {
    Flux<RunningJobDto> getRunningJobs(final String orchestratorUrl);
    Mono<Boolean> startJob(JobStartDto jobStartDto);
    Mono<Boolean> stopAndPurgeJob(String orchestratorUrl, String appName);
    Flux<FixedJobStartDto> getFixedJobs();
    Mono<Boolean> startFixedJob(final FixedJobStartDto fixedJobStartDto);
    Mono<Boolean> uploadJar(MultipartFile jarFile);
    Mono<Boolean> startLocalJob(final LocalJobStartDto localJobStartDto);
    Flux<String> getLogs(final String orchestratorUrl, final String jobName);
}
