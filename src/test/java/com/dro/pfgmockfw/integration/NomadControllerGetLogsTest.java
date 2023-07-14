package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.integration.stubs.NomadStubs;
import com.dro.pfgmockfw.utils.BaseIntegrationTest;
import com.dro.pfgmockfw.utils.ResourceUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class NomadControllerGetLogsTest extends BaseIntegrationTest {

    @Test
    void shouldReturnLogs_whenResponseIsOk() {
        //given
        NomadStubs.stubNomadGetAllocations();
        NomadStubs.stubNomadGetLogs();
        String expectedLogs = ResourceUtils.getStringFromResources("fixtures/nomad-logs-string.txt");

        // when
        var result = webTestClient.get()
                .uri("/nomad/logs?nomadUrl=http://localhost:8888&jobName=mi-job-3")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(String.class)
                .returnResult();

        // then
        String logs = String.join("\n", result.getResponseBody());
        assertThat(logs).isEqualToNormalizingNewlines(expectedLogs);

    }

    @Test
    void shouldReturn404_whenErrorInGettingAllocations() {
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
    void shouldReturn404_whenErrorInGettingLogs() {
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


