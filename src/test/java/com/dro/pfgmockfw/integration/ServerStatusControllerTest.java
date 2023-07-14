package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.integration.stubs.ServerStubs;
import com.dro.pfgmockfw.utils.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

@Slf4j
class ServerStatusControllerTest extends BaseIntegrationTest {

    @ParameterizedTest
    @ValueSource(ints = {200, 301})
    void shouldReturnTrueWhenServerIsHealthy(int httpStatus) {
        //given
        ServerStubs.stubServerResponseStatus(httpStatus);

        //when
        webTestClient.get()
                .uri("/status/server?serverUrl=http://localhost:8888")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);

    }

    @ParameterizedTest
    @ValueSource(ints = {400, 404, 500, 503})
    void shouldReturnFalseWhenServerIsNotHealthy(int httpStatus) {
        //given
        ServerStubs.stubServerResponseStatus(httpStatus);

        //when
        webTestClient.get()
                .uri("/status/server?serverUrl=http://localhost:8888")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(false);

    }


}


