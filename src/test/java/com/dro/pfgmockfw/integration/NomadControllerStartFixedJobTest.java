package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.integration.stubs.NomadStubs;
import com.dro.pfgmockfw.model.nomad.FixedJobDto;
import com.dro.pfgmockfw.utils.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@Slf4j
public class NomadControllerStartFixedJobTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;


    private static final FixedJobDto fixedJobStartDataDto = FixedJobDto.builder()
            .nomadUrl("http://localhost:8888")
            .appName("keycloak")
            .appVersion("18.0")
            .fileName("nomad-keycloak-18.0")
            .build();


    @Test
    public void shouldStartJob_whenResponseIsOk() {
        //given
        NomadStubs.stubNomadStartJob();


        //when //then
        webTestClient.post()
                .uri("/nomad/start-fixed-job")
                .body(BodyInserters.fromValue(fixedJobStartDataDto))
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
                .uri("/nomad/start-fixed-job")
                .body(BodyInserters.fromValue(new FixedJobDto()))
                .exchange()
                .expectStatus().isBadRequest();

    }


    @Test
    public void shouldReturn500_whenTechnicalExceptionInNomad() {
        //given
        NomadStubs.stubNomadStartJobWithStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        //when //then
        webTestClient.post()
                .uri("/nomad/start-fixed-job")
                .body(BodyInserters.fromValue(fixedJobStartDataDto))
                .exchange()
                .expectStatus().is5xxServerError();

    }

    @Test
    public void shouldReturn404_whenWebClientResponseInNomad() {
        //given
        NomadStubs.stubNomadStartJobWithStatus(HttpStatus.BAD_REQUEST.value());

        //when //then
        webTestClient.post()
                .uri("/nomad/start-fixed-job")
                .body(BodyInserters.fromValue(fixedJobStartDataDto))
                .exchange()
                .expectStatus().isNotFound();

    }
}


