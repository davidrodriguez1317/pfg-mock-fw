package com.dro.pfgmockfw.integration;

import com.dro.pfgmockfw.integration.stubs.DockerStubs;
import com.dro.pfgmockfw.model.docker.RepositoryWithTagsResponseDto;
import com.dro.pfgmockfw.utils.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class PlatformControllerGetImagesTest extends BaseIntegrationTest {

    @Test
    void shouldReturnRepositoriesWithTags_whenResponseIsOk() {
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
                .uri("/platform/images?platformUrl=http://localhost:8888")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RepositoryWithTagsResponseDto.class)
                .hasSize(2)
                .contains(repoWithTags01, repoWithTags02);

    }

    @Test
    void shouldReturnRepositoriesWithTags_whenResponseIsOkButNoContent() {
        //given
        DockerStubs.stubDockerCatalogNoContent();

        //when //then
        webTestClient.get()
                .uri("/platform/images?platformUrl=http://localhost:8888")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RepositoryWithTagsResponseDto.class)
                .consumeWith(response -> {
                    var responseBody = response.getResponseBody();
                    assertNotNull(responseBody);
                    assertEquals(0, responseBody.size());
                });

    }

    @Test
    void shouldReturn400_whenArgumentsNotPresent() {
        //given //when //then
        webTestClient.get()
                .uri("/platform/images")
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    void shouldReturn400_whenArgumentsNotCorrect() {
        //given //when //then
        webTestClient.get()
                .uri("/platform/images?platformUrlxxx=http://localhost:8888")
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    void shouldReturn500_whenTechnicalExceptionInDocker() {
        //given
        DockerStubs.stubDockerCatalogWithStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        //when //then
        webTestClient.get()
                .uri("/platform/images?platformUrl=http://localhost:8888")
                .exchange()
                .expectStatus().is5xxServerError();

    }

    @Test
    void shouldReturn404_whenWebClientResponseInDocker() {
        //given
        DockerStubs.stubDockerCatalogWithStatus(HttpStatus.BAD_REQUEST.value());

        //when //then
        webTestClient.get()
                .uri("/platform/images?platformUrl=http://localhost:8888")
                .exchange()
                .expectStatus().isNotFound();

    }
}


