package com.dro.pfgmockfw.service;

import com.dro.pfgmockfw.client.DockerWebClient;
import com.dro.pfgmockfw.model.docker.RepositoryWithTagsResponseDto;
import com.dro.pfgmockfw.service.interfaces.PlatformService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class DockerService implements PlatformService {
    private final DockerWebClient dockerWebClient;


    public Flux<RepositoryWithTagsResponseDto> getAllRepositoriesWithVersions(final String dockerUrl) {
        return dockerWebClient.getRepositories(dockerUrl)
                .flatMapMany(responseDto -> Flux.fromIterable(responseDto.getRepositories())
                        .flatMap(repository -> dockerWebClient.getVersionsFromRepository(dockerUrl, repository)));
    }

}
