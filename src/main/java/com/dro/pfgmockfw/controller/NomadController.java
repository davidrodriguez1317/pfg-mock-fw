package com.dro.pfgmockfw.controller;

import com.dro.pfgmockfw.model.nomad.FixedJobStartDto;
import com.dro.pfgmockfw.model.nomad.JobStartDto;
import com.dro.pfgmockfw.model.nomad.LocalJobStartDto;
import com.dro.pfgmockfw.model.nomad.RunningJobDto;
import com.dro.pfgmockfw.service.NomadService;
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
@RequestMapping("/nomad")
public class NomadController {

    private final NomadService nomadService;

    @GetMapping("/jobs")
    @ResponseBody
    public Flux<RunningJobDto> listRunningJobs(
            @RequestParam("nomadUrl") @NotBlank String nomadUrl
    ) {
        log.info("Getting running jobs");
        return nomadService.getRunningJobs(nomadUrl);
    }

    @GetMapping("/fixed-jobs")
    @ResponseBody
    public Flux<FixedJobStartDto> listFixedJobs() {
        log.info("Getting fixed jobs");
        return nomadService.getFixedJobs();
    }

    @PostMapping("/start-fixed-job")
    @ResponseBody
    public Mono<Boolean> startFixedJob(@RequestBody @Valid FixedJobStartDto fixedJobStartDto) {
        log.info("Starting fixed job {}", fixedJobStartDto.toString());
        return nomadService.startFixedJob(fixedJobStartDto);
    }

    @PostMapping("/upload-local-job")
    @ResponseBody
    public Mono<Boolean> uploadJar(@RequestBody @RequestParam("jarFile") MultipartFile jarFile) {
        log.info("Uploading file {}", jarFile.getOriginalFilename());
        return nomadService.uploadJar(jarFile);
    }

    @PostMapping("/start-local-job")
    @ResponseBody
    public Mono<Boolean> startLocalJob(@RequestBody @Valid LocalJobStartDto localJobStartDto) {
        log.info("Starting local job {}",localJobStartDto);
        return nomadService.startLocalJob(localJobStartDto);
    }

    @PostMapping("/start")
    @ResponseBody
    public Mono<Boolean> startJob(@RequestBody @Valid JobStartDto jobStartDto) {
        log.info("Starting nomad job for {}", jobStartDto);
        return nomadService.startJob(jobStartDto);
    }

    @DeleteMapping("/stop")
    @ResponseBody
    public Mono<Boolean> stopAndPurgeJob(@RequestParam (name = "nomadUrl") @Valid String nomadUrl,
                                         @RequestParam (name = "jobName") @Valid String jobName) {
        log.info("Stopping nomad job {} in {}", jobName, nomadUrl);
        return nomadService.stopAndPurgeJob(nomadUrl, jobName);
    }

    @GetMapping("/logs")
    @ResponseBody
    public String getLogs(@RequestParam (name = "nomadUrl") @Valid String nomadUrl,
                                                @RequestParam (name = "jobName") @Valid String jobName) {
        log.info("Getting logs for {} in {}", jobName, nomadUrl);
        return nomadService.getLogs(nomadUrl, jobName);
    }

}
