package com.dro.pfgmockfw.controller;

import com.dro.pfgmockfw.service.StatusService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/status")
public class ServerStatusController {

    private final StatusService statusService;

    @GetMapping("/server")
    @ResponseBody
    public Mono<Boolean> checkStatus(@RequestParam("serverUrl") @NotBlank String serverUrl) {
        log.info("Checking if server {} is active", serverUrl);
        return statusService.checkServerActive(serverUrl);
    }
}
