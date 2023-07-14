package com.dro.pfgmockfw.service;

import com.dro.pfgmockfw.client.DockerWebClient;
import com.dro.pfgmockfw.model.docker.RepositoriesResponseDto;
import com.dro.pfgmockfw.model.docker.RepositoryWithTagsResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DockerServiceTest {

    @Mock
    private DockerWebClient dockerWebClient;

    @InjectMocks
    private DockerService dockerService;


    @Test
    void getAllRepositoriesWithVersions_returnsFluxOfRepositoryWithTagsResponseDto() {
        //given
        RepositoriesResponseDto repositoriesResponseDto = RepositoriesResponseDto.builder()
                .repositories(List.of("repository"))
                .build();
        RepositoryWithTagsResponseDto responseDto = RepositoryWithTagsResponseDto.builder()
                .name("repository")
                .tags(Arrays.asList("tag1", "tag2"))
                .build();

        when(dockerWebClient.getRepositories(anyString())).thenReturn(Mono.just(repositoriesResponseDto));
        when(dockerWebClient.getVersionsFromRepository(anyString(), anyString())).thenReturn(Mono.just(responseDto));

        //when
        Flux<RepositoryWithTagsResponseDto> resultFlux =
                dockerService.getAllRepositoriesWithVersions("http://localhost:8888");

        //then
        StepVerifier.create(resultFlux)
                .expectNextMatches(dto -> dto.getName().equals("repository")
                        && dto.getTags().contains("tag1")
                        && dto.getTags().contains("tag2"))
                .expectComplete()
                .verify();
    }
}

