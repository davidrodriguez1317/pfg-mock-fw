package com.dro.pfgmockfw.controller;

import com.dro.pfgmockfw.model.docker.RepositoryWithTagsResponseDto;
import com.dro.pfgmockfw.service.interfaces.PlatformService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/platform")
public class PlatformController {

    private final PlatformService platformService;

    @GetMapping("/images")
    @ResponseBody
    public Flux<RepositoryWithTagsResponseDto> listDockerRegistryImages(@RequestParam("platformUrl") @NotBlank String platformUrl) {
        log.info("Getting platform images for platformUrl {}", platformUrl);
        return platformService.getAllRepositoriesWithVersions(platformUrl);
    }

}
