package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.integration.stubs.DockerStubs;
import com.dro.pfgmockfw.model.configuration.ConfigurationResponseDto;
import com.dro.pfgmockfw.model.docker.RepositoryWithTagsResponseDto;
import com.dro.pfgmockfw.utils.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ConfigurationControllerTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    public void shouldReturnRepositoriesWithTags_whenResponseIsOk() {
        //given
        var configuration = ConfigurationResponseDto.builder()
                .runningJobsPollingTime(15)
                .build();

        //when
        webTestClient.get()
                .uri("/configuration")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ConfigurationResponseDto.class)
                .isEqualTo(configuration);

    }
}


