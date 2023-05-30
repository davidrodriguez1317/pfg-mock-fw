package com.dro.pfgmockfw.controller;

import com.dro.pfgmockfw.model.nomad.JobStartData;
import com.dro.pfgmockfw.model.nomad.RunningJob;
import com.dro.pfgmockfw.model.nomad.RunningJobs;
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
    public Mono<RunningJob[]> listRunningJobs(
            @RequestParam("nomadUrl") @NotBlank String nomadUrl
    ) {
        log.info("Getting docker containers");
        return nomadService.getRunningJobs(nomadUrl);
    }

    @PostMapping("/start")
    @ResponseBody
    public Mono<Void> startJob(@RequestBody @Valid JobStartData jobStartData) {
        log.info("Starting nomad job for " + jobStartData);
        return Mono.empty();
    }

}
