package com.dro.pfgmockfw.controller;

import com.dro.pfgmockfw.model.docker.RepositoryWithTagsResponseDto;
import com.dro.pfgmockfw.service.DockerService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/docker")
public class DockerController {

    private final DockerService dockerService;

    @GetMapping("/images")
    @ResponseBody
    public Flux<RepositoryWithTagsResponseDto> listDockerRegistryImages(@RequestParam("dockerUrl") @NotBlank String dockerUrl) {
        log.info("Getting docker images for registryUrl {}", dockerUrl);
        return dockerService.getAllRepositoriesWithVersions(dockerUrl);
    }

}
