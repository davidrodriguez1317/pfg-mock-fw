package com.dro.pfgmockfw.client;

import com.dro.pfgmockfw.exception.EnumDoesNotExistException;
import com.dro.pfgmockfw.exception.WebClientResponseException;
import com.dro.pfgmockfw.exception.WebClientTechnicalException;
import com.dro.pfgmockfw.model.nomad.RunningJobDto;
import com.dro.pfgmockfw.model.nomad.server.ServerJobStopDto;
import com.dro.pfgmockfw.model.nomad.server.ServerRunningJobDto;
import com.dro.pfgmockfw.utils.ResourceUtils;
import com.fasterxml.jackson.core.JsonParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class NomadWebClient {

    private final WebClient webClient;

    public Flux<ServerRunningJobDto> getRunningJobs(final String nomadUrl) {
        String uri = String.format("%s/v1/jobs", nomadUrl);
        log.info("Getting running containers from " + uri);
        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new WebClientResponseException("Client error: " + response.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new WebClientTechnicalException("Server error: " + response.statusCode())))
                .bodyToMono(ServerRunningJobDto[].class)
                .flatMapMany(Flux::fromArray)
                .onErrorMap(JsonParseException.class, ex -> new WebClientResponseException("JSON parse error", ex))
                .onErrorMap(EnumDoesNotExistException.class, ex -> new WebClientResponseException("Not valid enum in response"));
    }

    public Mono<Boolean> startJob(final String nomadUrl, final String job) {

        String uri = String.format("%s/v1/jobs", nomadUrl);

        log.info("Starting job from " + uri);
        log.info("Job request: " + job);

        return webClient.post()
                .uri(uri)
                .body(BodyInserters.fromValue(job))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new WebClientResponseException("Client error: " + response.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new WebClientTechnicalException("Server error: " + response.statusCode())))
                .toBodilessEntity()
                .map(responseEntity -> responseEntity.getStatusCode().is2xxSuccessful());

    }

    public Mono<Boolean> stopAndPurgeJob(final String nomadUrl, final String jobId) {

        String uri = String.format("%s/v1/job/%s?purge=true", nomadUrl, jobId);

        log.info("Stopping job from " + uri);

        return webClient.delete()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new WebClientResponseException("Client error: " + response.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new WebClientTechnicalException("Server error: " + response.statusCode())))
                .bodyToMono(ServerJobStopDto.class)
                .map(stopDto -> Objects.nonNull(stopDto) && Strings.isNotBlank(stopDto.getEvalId()));

    }

}
