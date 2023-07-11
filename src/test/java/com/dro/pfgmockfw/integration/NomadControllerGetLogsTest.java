package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.integration.stubs.DockerStubs;
import com.dro.pfgmockfw.integration.stubs.NomadStubs;
import com.dro.pfgmockfw.model.nomad.FixedJobStartDto;
import com.dro.pfgmockfw.utils.BaseIntegrationTest;
import com.dro.pfgmockfw.utils.ResourceUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.dro.pfgmockfw.integration.stubs.NomadStubs.stubNomadGetAllocations;
import static com.dro.pfgmockfw.integration.stubs.NomadStubs.stubNomadGetLogs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
public class NomadControllerGetLogsTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    public void shouldReturnLogs_whenResponseIsOk() {
        //given
        NomadStubs.stubNomadGetAllocations();
        NomadStubs.stubNomadGetLogs();
        String expectedLogs = ResourceUtils.getStringFromResources("fixtures/nomad-logs-string.txt");

        // when
        var result = webTestClient.get()
                .uri("/nomad/logs?nomadUrl=http://localhost:8888&jobName=mi-job-3")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult();

        // then
        String logs = result.getResponseBody();
        assertThat(logs).isEqualToNormalizingNewlines(expectedLogs);

    }

    @Test
    public void shouldReturn404_whenErrorInGettingAllocations() {
        //given
        NomadStubs.stubNomadGetAllocationsWithStatus(HttpStatus.BAD_REQUEST.value());
        NomadStubs.stubNomadGetLogs();

        //when //then
        webTestClient.get()
                .uri("/nomad/logs?nomadUrl=http://localhost:8888&jobName=mi-job-3")
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    public void shouldReturn404_whenErrorInGettingLogs() {
        //given
        NomadStubs.stubNomadGetAllocations();
        NomadStubs.stubNomadGetLogsWithStatus(HttpStatus.BAD_REQUEST.value());

        //when //then
        webTestClient.get()
                .uri("/nomad/logs?nomadUrl=http://localhost:8888&jobName=mi-job-3")
                .exchange()
                .expectStatus().isNotFound();

    }

}


