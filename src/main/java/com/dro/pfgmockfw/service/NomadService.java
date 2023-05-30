package com.dro.pfgmockfw.service;

import com.dro.pfgmockfw.client.NomadWebClient;
import com.dro.pfgmockfw.model.nomad.RunningJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class NomadService {
    private final NomadWebClient nomadWebClient;

    public Mono<RunningJob[]> getRunningJobs(final String dockerUrl) {
        return nomadWebClient.getRunningJobs(dockerUrl);
    }

}
