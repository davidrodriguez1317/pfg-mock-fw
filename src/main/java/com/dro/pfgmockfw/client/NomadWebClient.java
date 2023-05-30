package com.dro.pfgmockfw.client;

import com.dro.pfgmockfw.exception.WebClientResponseException;
import com.dro.pfgmockfw.exception.WebClientTechnicalException;
import com.dro.pfgmockfw.model.nomad.RunningJob;
import com.dro.pfgmockfw.model.nomad.RunningJobs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class NomadWebClient {

    private final WebClient webClient;

    public Mono<RunningJob[]> getRunningJobs(final String nomadUrl) {
        String uri = String.format("%s/v1/jobs", nomadUrl);
        log.info("Getting running containers from " + uri);
        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new WebClientResponseException("Client error: " + response.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new WebClientTechnicalException("Server error: " + response.statusCode())))
                .bodyToMono(RunningJob[].class);
    }

}
