package com.dro.pfgmockfw.client;

import com.dro.pfgmockfw.exception.WebClientResponseException;
import com.dro.pfgmockfw.exception.WebClientTechnicalException;
import com.dro.pfgmockfw.model.docker.RepositoriesResponseDto;
import com.dro.pfgmockfw.model.docker.RepositoryWithTagsResponseDto;
import com.fasterxml.jackson.core.JsonParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.dro.pfgmockfw.utils.StringUtils.CLIENT_ERROR;
import static com.dro.pfgmockfw.utils.StringUtils.SERVER_ERROR;


@Slf4j
@Service
@RequiredArgsConstructor
public class DockerWebClient {

    private final WebClient webClient;

    public Mono<RepositoriesResponseDto> getRepositories(final String dockerUrl) {
        String uri = String.format("%s/v2/_catalog", dockerUrl);
        log.info("Getting repositories from " + uri);
        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new WebClientResponseException(CLIENT_ERROR + response.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new WebClientTechnicalException(SERVER_ERROR + response.statusCode())))
                .bodyToMono(RepositoriesResponseDto.class)
                .onErrorMap(JsonParseException.class, ex -> new WebClientResponseException("JSON parse error", ex));
    }

    public Mono<RepositoryWithTagsResponseDto> getVersionsFromRepository(final String dockerUrl, final String repository) {
        String uri = String.format("%s/v2/%s/tags/list", dockerUrl, repository);
        log.info("Getting app versions from " + uri);
        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new WebClientResponseException(CLIENT_ERROR + response.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new WebClientTechnicalException(SERVER_ERROR + response.statusCode())))
                .bodyToMono(RepositoryWithTagsResponseDto.class)
                .onErrorMap(JsonParseException.class, ex -> new WebClientResponseException("JSON parse error", ex));
    }

}
