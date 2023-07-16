package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.integration.stubs.NomadStubs;
import com.dro.pfgmockfw.model.nomad.FixedJobStartDto;
import com.dro.pfgmockfw.utils.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;

@Slf4j
class OrchestratorControllerStartFixedJobTest extends BaseIntegrationTest {

    private static final FixedJobStartDto fixedJobStartDataDto = FixedJobStartDto.builder()
            .orchestratorUrl("http://localhost:8888")
            .name("keycloak")
            .version("18.0")
            .fileName("nomad-keycloak-18.0.json")
            .build();


    @Test
    void shouldStartJob_whenResponseIsOk() {
        //given
        NomadStubs.stubNomadStartJob();


        //when //then
        webTestClient.post()
                .uri("/orchestrator/start-fixed-job")
                .body(BodyInserters.fromValue(fixedJobStartDataDto))
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
                .uri("/orchestrator/start-fixed-job")
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
                .uri("/orchestrator/start-fixed-job")
                .body(BodyInserters.fromValue(fixedJobStartDataDto))
                .exchange()
                .expectStatus().is5xxServerError();

    }

    @Test
    void shouldReturn404_whenWebClientResponseInNomad() {
        //given
        NomadStubs.stubNomadStartJobWithStatus(HttpStatus.BAD_REQUEST.value());

        //when //then
        webTestClient.post()
                .uri("/orchestrator/start-fixed-job")
                .body(BodyInserters.fromValue(fixedJobStartDataDto))
                .exchange()
                .expectStatus().isNotFound();

    }
}


