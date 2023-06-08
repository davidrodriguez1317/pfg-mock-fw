package com.dro.pfgmockfw.service;

import com.dro.pfgmockfw.client.NomadWebClient;
import com.dro.pfgmockfw.mapper.NomadMapper;
import com.dro.pfgmockfw.model.nomad.FixedJobDto;
import com.dro.pfgmockfw.model.nomad.JobStartDataDto;
import com.dro.pfgmockfw.model.nomad.JobStopDataDto;
import com.dro.pfgmockfw.model.nomad.RunningJobDto;
import com.dro.pfgmockfw.model.nomad.server.ServerRunningJobDto;
import com.dro.pfgmockfw.utils.ResourceUtils;
import com.dro.pfgmockfw.utils.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public Mono<Boolean> stopAndPurgeJob(String nomadUrl, String appName) {
        return nomadWebClient.stopAndPurgeJob(nomadUrl, appName);
    }

    public Flux<FixedJobDto> getFixedJobs() {
        List<String> fileNames = ResourceUtils.listFilesFromDirectory("fixed-templates");
        return Flux.fromIterable(parseFixedJobDtoFromFilenames(fileNames));
    }

    private static List<FixedJobDto> parseFixedJobDtoFromFilenames(List<String> filenames) {
        List<FixedJobDto> fixedJobDtos = new ArrayList<>();

        Pattern pattern = Pattern.compile("nomad-(\\w+)-(\\d+\\.\\d+)\\.json");

        for (String filename : filenames) {
            Matcher matcher = pattern.matcher(filename);

            if (matcher.matches()) {
                String appName = matcher.group(1);
                String appVersion = matcher.group(2);

                FixedJobDto fixedJobDto = FixedJobDto.builder()
                        .appName(appName)
                        .appVersion(appVersion)
                        .fileName(filename)
                        .build();

                fixedJobDtos.add(fixedJobDto);
            }
        }

        return fixedJobDtos;
    }
}
