package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.integration.stubs.NomadStubs;
import com.dro.pfgmockfw.utils.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Slf4j
class NomadControllerStopJobTest extends BaseIntegrationTest {

    @Test
    void shouldStopJob_whenResponseIsOk_andEvalIdCorrect() {
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
    void shouldNotStopJob_whenResponseIsOk_andEvalIdNotCorrect() {
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
    void shouldReturn400_whenNotValidRequestParam() {
        //given //when //then
        webTestClient.delete()
                .uri("/nomad/stop?nomadUrlxxx=http://localhost:8888&jobName=some-job")
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    void shouldReturn400_whenArgumentNotPresent() {
        //given //when //then
        webTestClient.delete()
                .uri("/nomad/stop?nomadUrl=http://localhost:8888")
                .exchange()
                .expectStatus().isBadRequest();

    }


    @ParameterizedTest
    @ValueSource(ints = {400, 404})
    void shouldReturnNotFound_whenResponseIsNotHealthy(int httpStatus) {
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
    void shouldReturnInternalServerError_whenResponseIsNotHealthy(int httpStatus) {
        //given
        NomadStubs.stubNomadStopJobWithStatus(httpStatus);

        //when
        webTestClient.delete()
                .uri("/nomad/stop?nomadUrl=http://localhost:8888&jobName=some-job")
                .exchange()
                .expectStatus().is5xxServerError();

    }
}


