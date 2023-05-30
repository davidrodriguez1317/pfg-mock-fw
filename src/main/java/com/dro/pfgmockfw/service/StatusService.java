package com.dro.pfgmockfw.service;

import com.dro.pfgmockfw.client.ServerStatusWebClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatusService {
    private final ServerStatusWebClient serverStatusWebClient;

    public Mono<Boolean> checkServerActive(final String serverUrl) {
        return serverStatusWebClient.checkServerActive(serverUrl);
    }

}
