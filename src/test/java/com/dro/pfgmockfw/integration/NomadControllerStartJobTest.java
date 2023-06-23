package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.integration.stubs.NomadStubs;
import com.dro.pfgmockfw.model.nomad.JobStartDto;
import com.dro.pfgmockfw.utils.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Map;

@Slf4j
public class NomadControllerStartJobTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final JobStartDto JOB_START_DTO = JobStartDto.builder()
            .dockerUrl("http://localhost:5001")
            .nomadUrl("http://localhost:8888")
            .appName("first-app")
            .appVersion("0.0.1-SNAPSHOT")
            .envs(Map.of("ENV1", "value1", "ENV2", "value2"))
            .build();

    @Test
    public void shouldStartJob_whenResponseIsOk() {
        //given
        NomadStubs.stubNomadStartJob();

        //when //then
        webTestClient.post()
                .uri("/nomad/start")
                .body(BodyInserters.fromValue(JOB_START_DTO))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);

    }


    @Test
    public void shouldReturn400_whenBodyNotValid() {
        //given
        NomadStubs.stubNomadStartJob();

        // when //then
        webTestClient.post()
                .uri("/nomad/start")
                .body(BodyInserters.fromValue(new JobStartDto()))
                .exchange()
                .expectStatus().isBadRequest();

    }


    @Test
    public void shouldReturn500_whenTechnicalExceptionInNomad() {
        //given
        NomadStubs.stubNomadStartJobWithStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        //when //then
        webTestClient.post()
                .uri("/nomad/start")
                .body(BodyInserters.fromValue(JOB_START_DTO))
                .exchange()
                .expectStatus().is5xxServerError();

    }

    @Test
    public void shouldReturn404_whenWebClientResponseInNomad() {
        //given
        NomadStubs.stubNomadStartJobWithStatus(HttpStatus.BAD_REQUEST.value());

        //when //then
        webTestClient.post()
                .uri("/nomad/start")
                .body(BodyInserters.fromValue(JOB_START_DTO))
                .exchange()
                .expectStatus().isNotFound();

    }
}


