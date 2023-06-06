package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.integration.stubs.NomadStubs;
import com.dro.pfgmockfw.model.nomad.JobStartDataDto;
import com.dro.pfgmockfw.utils.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@Slf4j
public class NomadControllerStartJobTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    public void shouldStartJob_whenResponseIsOk() {
        //given
        NomadStubs.stubNomadStartJob();
        JobStartDataDto jobStartDataDto = JobStartDataDto.builder()
                .dockerUrl("http://localhost:5001")
                .nomadUrl("http://localhost:8888")
                .appName("first-app")
                .appVersion("0.0.1-SNAPSHOT")
                .build();

        //when //then
        webTestClient.post()
                .uri("/nomad/start")
                .body(BodyInserters.fromValue(jobStartDataDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);

    }


    @Test
    public void shouldReturn400_whenBodyNotValid() {
        //given
        NomadStubs.stubNomadStartJob();
        JobStartDataDto jobStartDataDto = JobStartDataDto.builder()
                .dockerUrl("http://localhost:5001")
                .nomadUrl("http://localhost:8888")
                .appName("first-app")
                .build();


        // when //then
        webTestClient.post()
                .uri("/nomad/start")
                .body(BodyInserters.fromValue(jobStartDataDto))
                .exchange()
                .expectStatus().isBadRequest();

    }


    @Test
    public void shouldReturn500_whenTechnicalExceptionInNomad() {
        //given
        NomadStubs.stubNomadStartJobWithStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        JobStartDataDto jobStartDataDto = JobStartDataDto.builder()
                .dockerUrl("http://localhost:5001")
                .nomadUrl("http://localhost:8888")
                .appName("first-app")
                .appVersion("0.0.1-SNAPSHOT")
                .build();

        //when //then
        webTestClient.post()
                .uri("/nomad/start")
                .body(BodyInserters.fromValue(jobStartDataDto))
                .exchange()
                .expectStatus().is5xxServerError();

    }

    @Test
    public void shouldReturn404_whenWebClientResponseInNomad() {
        //given
        NomadStubs.stubNomadStartJobWithStatus(HttpStatus.BAD_REQUEST.value());
        JobStartDataDto jobStartDataDto = JobStartDataDto.builder()
                .dockerUrl("http://localhost:5001")
                .nomadUrl("http://localhost:8888")
                .appName("first-app")
                .appVersion("0.0.1-SNAPSHOT")
                .build();

        //when //then
        webTestClient.post()
                .uri("/nomad/start")
                .body(BodyInserters.fromValue(jobStartDataDto))
                .exchange()
                .expectStatus().isNotFound();

    }
}


