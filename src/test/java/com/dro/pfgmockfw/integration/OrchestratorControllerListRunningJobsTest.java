package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.integration.stubs.NomadStubs;
import com.dro.pfgmockfw.model.nomad.JobStatusType;
import com.dro.pfgmockfw.model.nomad.RunningJobDto;
import com.dro.pfgmockfw.utils.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class OrchestratorControllerListRunningJobsTest extends BaseIntegrationTest {

    @Test
    void shouldReturnJobs_whenResponseIsOk() {
        //given
        NomadStubs.stubNomadRunningJobs();

        //when
        var result = webTestClient.get()
                .uri("/orchestrator/jobs?orchestratorUrl=http://localhost:8888")
                .exchange()
                .expectStatus().isOk()
                .expectBody(RunningJobDto[].class)
                .returnResult();

        // then
        RunningJobDto[] actualRunningJobDtos = result.getResponseBody();
        assertNotNull(actualRunningJobDtos);
        assertEquals(2, actualRunningJobDtos.length);

        assertEquals("price-calculator", actualRunningJobDtos[0].getId());
        assertEquals(JobStatusType.RUNNING, actualRunningJobDtos[0].getStatus());
        assertEquals("product-cost", actualRunningJobDtos[1].getId());
        assertEquals(JobStatusType.COMPLETE, actualRunningJobDtos[1].getStatus());

    }

    @Test
    void shouldReturnJobs_whenResponseIsOkButNoContent() {
        //given
        NomadStubs.stubNomadRunningJobsNoContent();

        //when
        var result = webTestClient.get()
                .uri("/orchestrator/jobs?orchestratorUrl=http://localhost:8888")
                .exchange()
                .expectStatus().isOk()
                .expectBody(RunningJobDto[].class)
                .returnResult();

        // then
        RunningJobDto[] actualRunningJobDtos = result.getResponseBody();
        assertNotNull(actualRunningJobDtos);
        assertEquals(0, actualRunningJobDtos.length);

    }

    @Test
    void shouldReturn400_whenArgumentsNotPresent() {
        //given //when //then
        webTestClient.get()
                .uri("/orchestrator/jobs")
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    void shouldReturn400_whenArgumentsNotCorrect() {
        //given //when //then
        webTestClient.get()
                .uri("/orchestrator/jobs?orchestratorUrlxxx=http://localhost:8888")
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    void shouldReturn500_whenTechnicalExceptionInNomad() {
        //given
        NomadStubs.stubNomadRunningJobsWithStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        //when //then
        webTestClient.get()
                .uri("/orchestrator/jobs?orchestratorUrl=http://localhost:8888")
                .exchange()
                .expectStatus().is5xxServerError();

    }

    @Test
    void shouldReturn404_whenWebClientResponseInNomad() {
        //given
        NomadStubs.stubNomadRunningJobsWithStatus(HttpStatus.BAD_REQUEST.value());

        //when //then
        webTestClient.get()
                .uri("/orchestrator/jobs?orchestratorUrl=http://localhost:8888")
                .exchange()
                .expectStatus().isNotFound();

    }
}


