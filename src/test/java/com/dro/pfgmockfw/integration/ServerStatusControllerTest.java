package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.integration.stubs.DockerStubs;
import com.dro.pfgmockfw.integration.stubs.ServerStubs;
import com.dro.pfgmockfw.model.docker.RepositoryWithTagsResponseDto;
import com.dro.pfgmockfw.utils.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ServerStatusControllerTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    public void shouldReturnTrueWhenServerIsHealthy() {
        //given
        ServerStubs.stubServerResponseStatus(HttpStatus.OK.value());

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
    public void shouldReturnFalseWhenServerIsNotHealthy(int httpStatus) {
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


