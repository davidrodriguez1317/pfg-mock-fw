package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.integration.stubs.NomadStubs;
import com.dro.pfgmockfw.model.nomad.JobStatusType;
import com.dro.pfgmockfw.model.nomad.RunningJobDto;
import com.dro.pfgmockfw.utils.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class NomadControllerTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    public void shouldReturnJobs_whenResponseIsOk() {
        //given
        NomadStubs.stubNomadRunningJobs();

        //when
        var result = webTestClient.get()
                .uri("/nomad/jobs?nomadUrl=http://localhost:8888")
                .exchange()
                .expectStatus().isOk()
                .expectBody(RunningJobDto[].class)
                .returnResult();

        // then
        RunningJobDto[] actualRunningJobDtos = result.getResponseBody();
        assertNotNull(actualRunningJobDtos);
        assertEquals(2, actualRunningJobDtos.length);

        assertEquals("Job 1", actualRunningJobDtos[0].getId());
        assertEquals(JobStatusType.RUNNING, actualRunningJobDtos[0].getStatus());
        assertEquals("Job 2", actualRunningJobDtos[1].getId());
        assertEquals(JobStatusType.COMPLETE, actualRunningJobDtos[1].getStatus());

    }

    @Test
    public void shouldReturnJobs_whenResponseIsOkButNoContent() {
        //given
        NomadStubs.stubNomadRunningJobsNoContent();

        //when
        var result = webTestClient.get()
                .uri("/nomad/jobs?nomadUrl=http://localhost:8888")
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
    public void shouldReturn400_whenArgumentsNotPresent() {
        //given //when //then
        webTestClient.get()
                .uri("/nomad/jobs")
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    public void shouldReturn400_whenArgumentsNotCorrect() {
        //given //when //then
        webTestClient.get()
                .uri("/nomad/jobs?nomadUrlxxx=http://localhost:8888")
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    public void shouldReturn500_whenTechnicalExceptionInNomad() {
        //given
        NomadStubs.stubNomadRunningJobsWithStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        //when //then
        webTestClient.get()
                .uri("/nomad/jobs?nomadUrl=http://localhost:8888")
                .exchange()
                .expectStatus().is5xxServerError();

    }

    @Test
    public void shouldReturn404_whenWebClientResponseInNomad() {
        //given
        NomadStubs.stubNomadRunningJobsWithStatus(HttpStatus.BAD_REQUEST.value());

        //when //then
        webTestClient.get()
                .uri("/nomad/jobs?nomadUrl=http://localhost:8888")
                .exchange()
                .expectStatus().isNotFound();

    }
}


