package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.integration.stubs.DockerStubs;
import com.dro.pfgmockfw.integration.stubs.NomadStubs;
import com.dro.pfgmockfw.model.docker.RepositoryWithTagsResponseDto;
import com.dro.pfgmockfw.model.nomad.JobStatusType;
import com.dro.pfgmockfw.model.nomad.RunningJobDto;
import com.dro.pfgmockfw.utils.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class DockerControllerTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    public void shouldReturnRepositoriesWithTags_whenResponseIsOk() {
        //given
        var repoWithTags01 = RepositoryWithTagsResponseDto.builder()
                .name("pfg-price-calculator")
                .tags(List.of("0.0.1-SNAPSHOT", "0.0.2-SNAPSHOT"))
                .build();

        var repoWithTags02 = RepositoryWithTagsResponseDto.builder()
                .name("pfg-product-cost")
                .tags(List.of("0.0.1-SNAPSHOT"))
                .build();

        DockerStubs.stubDockerCatalog();
        DockerStubs.stubDockerTags(repoWithTags01.getName(), repoWithTags01.getTags());
        DockerStubs.stubDockerTags(repoWithTags02.getName(), repoWithTags02.getTags());

        //when
        webTestClient.get()
                .uri("/docker/images?dockerUrl=http://localhost:8888")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RepositoryWithTagsResponseDto.class)
                .hasSize(2)
                .contains(repoWithTags01, repoWithTags02);

    }

    @Test
    public void shouldReturnRepositoriesWithTags_whenResponseIsOkButNoContent() {
        //given
        DockerStubs.stubDockerCatalogNoContent();

        //when
        var result = webTestClient.get()
                .uri("/docker/images?dockerUrl=http://localhost:8888")
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
                .uri("/docker/images")
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    public void shouldReturn400_whenArgumentsNotCorrect() {
        //given //when //then
        webTestClient.get()
                .uri("/docker/images?dockerUrlxxx=http://localhost:8888")
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    public void shouldReturn500_whenTechnicalExceptionInNomad() {
        //given
        DockerStubs.stubDockerCatalogWithStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        //when //then
        webTestClient.get()
                .uri("/docker/images?dockerUrl=http://localhost:8888")
                .exchange()
                .expectStatus().is5xxServerError();

    }

    @Test
    public void shouldReturn404_whenWebClientResponseInDocker() {
        //given
        DockerStubs.stubDockerCatalogWithStatus(HttpStatus.BAD_REQUEST.value());

        //when //then
        webTestClient.get()
                .uri("/docker/images?dockerUrl=http://localhost:8888")
                .exchange()
                .expectStatus().isNotFound();

    }
}


