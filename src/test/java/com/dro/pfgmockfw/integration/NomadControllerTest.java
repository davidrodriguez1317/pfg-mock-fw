package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.controller.NomadController;
import com.dro.pfgmockfw.integration.stubs.NomadStubs;
import com.dro.pfgmockfw.model.nomad.JobStatus;
import com.dro.pfgmockfw.model.nomad.RunningJob;
import com.dro.pfgmockfw.model.nomad.RunningJobs;
import com.dro.pfgmockfw.utils.BaseIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class NomadControllerTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    public void testListDockerRegistryContainers() throws Exception {
        //given
        NomadStubs.stubNomadRunningJobs();

        //when
        var result = webTestClient.get()
                .uri("/nomad/jobs?nomadUrl=http://localhost:8888")
                .exchange()
                .expectStatus().isOk().returnResult(List.class);

        log.info("Result= " + result);

//        var actualRunningJobs = objectMapper.readValue(response.getContentAsString(), RunningJobs.class);
//
//        //then
//        Assertions.assertEquals("Job 1", actualRunningJobs.getRunningJobs().get(0).getId());
//        Assertions.assertEquals(JobStatus.RUNNING, actualRunningJobs.getRunningJobs().get(0).getStatus());
//        Assertions.assertEquals("Job 2", actualRunningJobs.getRunningJobs().get(1).getId());
//        Assertions.assertEquals(JobStatus.COMPLETE, actualRunningJobs.getRunningJobs().get(1).getStatus());

    }
}


