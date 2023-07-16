package com.dro.pfgmockfw.controller;

import com.dro.pfgmockfw.model.nomad.FixedJobStartDto;
import com.dro.pfgmockfw.model.nomad.JobStartDto;
import com.dro.pfgmockfw.model.nomad.LocalJobStartDto;
import com.dro.pfgmockfw.model.nomad.RunningJobDto;
import com.dro.pfgmockfw.service.interfaces.OrchestratorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/orchestrator")
public class OrchestratorController {

    private final OrchestratorService orchestratorService;

    @GetMapping("/jobs")
    @ResponseBody
    public Flux<RunningJobDto> listRunningJobs(
            @RequestParam("orchestratorUrl") @NotBlank String orchestratorUrl
    ) {
        log.info("Getting running jobs");
        return orchestratorService.getRunningJobs(orchestratorUrl);
    }

    @GetMapping("/fixed-jobs")
    @ResponseBody
    public Flux<FixedJobStartDto> listFixedJobs() {
        log.info("Getting fixed jobs");
        return orchestratorService.getFixedJobs();
    }

    @PostMapping("/start-fixed-job")
    @ResponseBody
    public Mono<Boolean> startFixedJob(@RequestBody @Valid FixedJobStartDto fixedJobStartDto) {
        log.info("Starting fixed job {}", fixedJobStartDto.toString());
        return orchestratorService.startFixedJob(fixedJobStartDto);
    }

    @PostMapping("/upload-local-job")
    @ResponseBody
    public Mono<Boolean> uploadJar(@RequestBody @RequestParam("jarFile") MultipartFile jarFile) {
        log.info("Uploading file {}", jarFile.getOriginalFilename());
        return orchestratorService.uploadJar(jarFile);
    }

    @PostMapping("/start-local-job")
    @ResponseBody
    public Mono<Boolean> startLocalJob(@RequestBody @Valid LocalJobStartDto localJobStartDto) {
        log.info("Starting local job {}",localJobStartDto);
        return orchestratorService.startLocalJob(localJobStartDto);
    }

    @PostMapping("/start")
    @ResponseBody
    public Mono<Boolean> startJob(@RequestBody @Valid JobStartDto jobStartDto) {
        log.info("Starting orchestrator job for {}", jobStartDto);
        return orchestratorService.startJob(jobStartDto);
    }

    @DeleteMapping("/stop")
    @ResponseBody
    public Mono<Boolean> stopAndPurgeJob(@RequestParam (name = "orchestratorUrl") @Valid String orchestratorUrl,
                                         @RequestParam (name = "jobName") @Valid String jobName) {
        log.info("Stopping orchestrator job {} in {}", jobName, orchestratorUrl);
        return orchestratorService.stopAndPurgeJob(orchestratorUrl, jobName);
    }

    @GetMapping("/logs")
    @ResponseBody
    public Flux<String> getLogs(@RequestParam (name = "orchestratorUrl") @Valid String orchestratorUrl,
                                                @RequestParam (name = "jobName") @Valid String jobName) {
        log.info("Getting logs for {} in {}", jobName, orchestratorUrl);
        return orchestratorService.getLogs(orchestratorUrl, jobName);
    }

}
