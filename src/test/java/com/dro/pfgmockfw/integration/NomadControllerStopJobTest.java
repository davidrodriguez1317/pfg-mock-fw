package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.integration.stubs.NomadStubs;
import com.dro.pfgmockfw.utils.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

@Slf4j
public class NomadControllerStopJobTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    public void shouldStopJob_whenResponseIsOk_andEvalIdCorrect() {
        //given
        NomadStubs.stubNomadStopJobWithSuccess();

        //when //then
        webTestClient.delete()
                .uri("/nomad/stop?nomadUrl=http://localhost:8888&jobName=some-job")
                .exchange()

                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);

    }

    @Test
    public void shouldNotStopJob_whenResponseIsOk_andEvalIdNotCorrect() {
        //given
        NomadStubs.stubNomadStopJobNoSuccess();

        //when //then
        webTestClient.delete()
                .uri("/nomad/stop?nomadUrl=http://localhost:8888&jobName=some-job")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(false);

    }


    @Test
    public void shouldReturn400_whenNotValidRequestParam() {
        //given //when //then
        webTestClient.delete()
                .uri("/nomad/stop?nomadUrlxxx=http://localhost:8888&jobName=some-job")
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    public void shouldReturn400_whenArgumentNotPresent() {
        //given //when //then
        webTestClient.delete()
                .uri("/nomad/stop?nomadUrl=http://localhost:8888")
                .exchange()
                .expectStatus().isBadRequest();

    }


    @ParameterizedTest
    @ValueSource(ints = {400, 404})
    public void shouldReturnNotFound_whenResponseIsNotHealthy(int httpStatus) {
        //given
        NomadStubs.stubNomadStopJobWithStatus(httpStatus);

        //when
        webTestClient.delete()
                .uri("/nomad/stop?nomadUrl=http://localhost:8888&jobName=some-job")
                .exchange()
                .expectStatus().isNotFound();

    }

    @ParameterizedTest
    @ValueSource(ints = {500, 503})
    public void shouldReturnInternalServerError_whenResponseIsNotHealthy(int httpStatus) {
        //given
        NomadStubs.stubNomadStopJobWithStatus(httpStatus);

        //when
        webTestClient.delete()
                .uri("/nomad/stop?nomadUrl=http://localhost:8888&jobName=some-job")
                .exchange()
                .expectStatus().is5xxServerError();

    }
}


