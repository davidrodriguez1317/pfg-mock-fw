package com.dro.pfgmockfw.service.interfaces;

import com.dro.pfgmockfw.model.docker.RepositoryWithTagsResponseDto;
import reactor.core.publisher.Flux;

public interface PlatformService {
    Flux<RepositoryWithTagsResponseDto> getAllRepositoriesWithVersions(final String platformUrl);

}
