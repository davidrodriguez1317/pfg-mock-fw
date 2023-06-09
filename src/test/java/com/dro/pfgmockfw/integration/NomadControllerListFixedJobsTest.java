package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.integration.stubs.NomadStubs;
import com.dro.pfgmockfw.model.nomad.FixedJobDto;
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
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
public class NomadControllerListFixedJobsTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    public void shouldReturnJobs_whenResponseIsOk() {
        //given //when
        var result = webTestClient.get()
                .uri("/nomad/fixed-jobs")
                .exchange()
                .expectStatus().isOk()
                .expectBody(FixedJobDto[].class)
                .returnResult();

        // then
        FixedJobDto[] fixedJobDtos = result.getResponseBody();
        assertNotNull(fixedJobDtos);
        assertEquals(1, fixedJobDtos.length);

        assertEquals("keycloak", fixedJobDtos[0].getAppName());
        assertEquals("18.0", fixedJobDtos[0].getAppVersion());
        assertEquals("nomad-keycloak-18.0.json", fixedJobDtos[0].getFileName());
        assertNull(fixedJobDtos[0].getNomadUrl());

    }

}


