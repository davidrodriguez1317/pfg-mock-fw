package com.dro.pfgmockfw.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerStatusWebClient {

    private final WebClient webClient;

    public Mono<Boolean> checkServerActive(final String serverUrl) {
        log.info("Checking server health " + serverUrl);
        return webClient.get()
                .uri(serverUrl)
                .exchangeToMono(clientResponse ->
                        Mono.just(clientResponse.statusCode().is2xxSuccessful() ||
                                clientResponse.statusCode().is3xxRedirection()))
                .onErrorReturn(Boolean.FALSE);
    }

}
