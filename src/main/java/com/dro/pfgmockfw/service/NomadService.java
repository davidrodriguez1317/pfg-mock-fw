package com.dro.pfgmockfw.service;

import com.dro.pfgmockfw.client.NomadWebClient;
import com.dro.pfgmockfw.mapper.NomadMapper;
import com.dro.pfgmockfw.model.nomad.JobStartDataDto;
import com.dro.pfgmockfw.model.nomad.JobStopDataDto;
import com.dro.pfgmockfw.model.nomad.RunningJobDto;
import com.dro.pfgmockfw.model.nomad.server.ServerRunningJobDto;
import com.dro.pfgmockfw.utils.StringUtils;
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

    public Flux<RunningJobDto> getRunningJobs(final String nomadUrl) {
        Flux<ServerRunningJobDto> serverRunningJobs = nomadWebClient.getRunningJobs(nomadUrl);
        return serverRunningJobs.map(NomadMapper.INSTANCE::fromServerRunningJobDto);
    }

    public Mono<Boolean> startJob(JobStartDataDto jobStartDataDto) {

        String imageName = StringUtils.stripHttpFromUrl(jobStartDataDto.getDockerUrl())
                .concat("/")
                .concat(jobStartDataDto.getAppName())
                .concat(":")
                .concat(jobStartDataDto.getAppVersion());

        return nomadWebClient.startJob(jobStartDataDto.getNomadUrl(), jobStartDataDto.getAppName(), imageName);
    }

    public Mono<Boolean> stopJob(JobStopDataDto jobStopDataDto) {
        return null;
    }
}
