package com.dro.pfgmockfw.client;

import com.dro.pfgmockfw.exception.EnumDoesNotExistException;
import com.dro.pfgmockfw.exception.WebClientResponseException;
import com.dro.pfgmockfw.exception.WebClientTechnicalException;
import com.dro.pfgmockfw.model.nomad.JobAllocationDto;
import com.dro.pfgmockfw.model.nomad.JobLogsDto;
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

import static com.dro.pfgmockfw.utils.StringUtils.CLIENT_ERROR;
import static com.dro.pfgmockfw.utils.StringUtils.SERVER_ERROR;

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
                        response -> Mono.error(new WebClientResponseException(CLIENT_ERROR + response.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new WebClientTechnicalException(SERVER_ERROR + response.statusCode())))
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
                        response -> Mono.error(new WebClientResponseException(CLIENT_ERROR + response.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new WebClientTechnicalException(SERVER_ERROR + response.statusCode())))
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
                        response -> Mono.error(new WebClientResponseException(CLIENT_ERROR + response.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new WebClientTechnicalException(SERVER_ERROR + response.statusCode())))
                .bodyToMono(ServerJobStopDto.class)
                .map(stopDto -> Objects.nonNull(stopDto) && Strings.isNotBlank(stopDto.getEvalId()));

    }

    public Flux<JobAllocationDto> getAllocationsForJob(final String nomadUrl, final String jobId) {

        String uri = String.format("%s/v1/job/%s/allocations", nomadUrl, jobId);
        log.info("Getting allocations from ".concat(uri));

        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new WebClientResponseException(CLIENT_ERROR + response.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new WebClientTechnicalException(SERVER_ERROR + response.statusCode())))
                .bodyToMono(JobAllocationDto[].class)
                .flatMapMany(Flux::fromArray)
                .onErrorMap(JsonParseException.class, ex -> new WebClientResponseException("JSON parse error", ex));
    }

    public Mono<JobLogsDto> getLogsForAllocation(final String nomadUrl,
                                                 final String jobName,
                                                 final String allocationId,
                                                 final int allowedCharacters) {

        String uri = String.format("%s/v1/client/fs/logs/%s?offset=%d&origin=end&task=%s&type=stdout",
                nomadUrl, allocationId, allowedCharacters, jobName);

        log.info("Reading logs from " + uri);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new WebClientResponseException(CLIENT_ERROR + response.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new WebClientTechnicalException(SERVER_ERROR + response.statusCode())))
                .bodyToMono(JobLogsDto.class);

    }

}
