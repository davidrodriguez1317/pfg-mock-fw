package com.dro.pfgmockfw.controller;

import com.dro.pfgmockfw.model.nomad.JobStartDataDto;
import com.dro.pfgmockfw.model.nomad.JobStopDataDto;
import com.dro.pfgmockfw.model.nomad.RunningJobDto;
import com.dro.pfgmockfw.service.NomadService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/start")
    @ResponseBody
    public Mono<Boolean> startJob(@RequestBody @Valid JobStartDataDto jobStartDataDto) {
        log.info("Starting nomad job for " + jobStartDataDto);
        return nomadService.startJob(jobStartDataDto);
    }

    @PostMapping("/stop")
    @ResponseBody
    public Mono<Boolean> stopJob(@RequestBody @Valid JobStopDataDto jobStopDataDto) {
        log.info("Stopping nomad job for " + jobStopDataDto);
        return nomadService.stopJob(jobStopDataDto);
    }

}
