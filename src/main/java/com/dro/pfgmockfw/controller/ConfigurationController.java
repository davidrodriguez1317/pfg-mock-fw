package com.dro.pfgmockfw.controller;

import com.dro.pfgmockfw.config.MockFwProperties;
import com.dro.pfgmockfw.model.configuration.ConfigurationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/configuration")
public class ConfigurationController {

    private final MockFwProperties mockFwProperties;

    @GetMapping
    @ResponseBody
    public Mono<ConfigurationResponseDto> getConfiguration() {
        log.info("Getting initial configuration");

        return Mono.just(ConfigurationResponseDto.builder()
                .runningJobsPollingTime(mockFwProperties.getRunningJobsPollingTime())
                .build());
    }

}
