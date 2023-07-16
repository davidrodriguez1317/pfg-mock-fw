package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.integration.stubs.NomadStubs;
import com.dro.pfgmockfw.model.nomad.FixedJobStartDto;
import com.dro.pfgmockfw.model.nomad.LocalJobStartDto;
import com.dro.pfgmockfw.utils.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Map;

@Slf4j
class OrchestratorControllerStartLocalJobTest extends BaseIntegrationTest {

    private static final LocalJobStartDto localJobStartDataDto = LocalJobStartDto.builder()
            .orchestratorUrl("http://localhost:8888")
            .fileName("some-job.jar")
            .envs(Map.of("ENV_1", "VALUE_1", "ENV_2", "VALUE_2"))
            .build();


    @Test
    void shouldStartJob_whenResponseIsOk() {
        //given
        NomadStubs.stubNomadStartJob();


        //when //then
        webTestClient.post()
                .uri("/orchestrator/start-local-job")
                .body(BodyInserters.fromValue(localJobStartDataDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);

    }


    @Test
    void shouldReturn400_whenBodyNotValid() {
        //given
        NomadStubs.stubNomadStartJob();

        // when //then
        webTestClient.post()
                .uri("/orchestrator/start-local-job")
                .body(BodyInserters.fromValue(new FixedJobStartDto()))
                .exchange()
                .expectStatus().isBadRequest();

    }


    @Test
    void shouldReturn500_whenTechnicalExceptionInNomad() {
        //given
        NomadStubs.stubNomadStartJobWithStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        //when //then
        webTestClient.post()
                .uri("/orchestrator/start-local-job")
                .body(BodyInserters.fromValue(localJobStartDataDto))
                .exchange()
                .expectStatus().is5xxServerError();

    }

    @Test
    void shouldReturn404_whenWebClientResponseInNomad() {
        //given
        NomadStubs.stubNomadStartJobWithStatus(HttpStatus.BAD_REQUEST.value());

        //when //then
        webTestClient.post()
                .uri("/orchestrator/start-local-job")
                .body(BodyInserters.fromValue(localJobStartDataDto))
                .exchange()
                .expectStatus().isNotFound();

    }
}


