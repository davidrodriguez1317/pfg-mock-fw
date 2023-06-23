package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.model.nomad.FixedJobStartDto;
import com.dro.pfgmockfw.utils.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
                .expectBody(FixedJobStartDto[].class)
                .returnResult();

        // then
        FixedJobStartDto[] fixedJobStartDtos = result.getResponseBody();
        assertNotNull(fixedJobStartDtos);
        assertEquals(1, fixedJobStartDtos.length);

        assertEquals("keycloak", fixedJobStartDtos[0].getName());
        assertEquals("18.0", fixedJobStartDtos[0].getVersion());
        assertEquals("nomad-keycloak-18.0.json", fixedJobStartDtos[0].getFileName());
        assertNull(fixedJobStartDtos[0].getNomadUrl());

    }

}


