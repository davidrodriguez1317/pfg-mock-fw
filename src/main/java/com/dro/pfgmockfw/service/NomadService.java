package com.dro.pfgmockfw.service;

import com.dro.pfgmockfw.client.NomadWebClient;
import com.dro.pfgmockfw.config.MockFwProperties;
import com.dro.pfgmockfw.exception.LaunchingLocalJobException;
import com.dro.pfgmockfw.mapper.NomadMapper;
import com.dro.pfgmockfw.model.nomad.FixedJobDto;
import com.dro.pfgmockfw.model.nomad.JobStartDataDto;
import com.dro.pfgmockfw.model.nomad.RunningJobDto;
import com.dro.pfgmockfw.model.nomad.server.ServerRunningJobDto;
import com.dro.pfgmockfw.utils.ResourceUtils;
import com.dro.pfgmockfw.utils.StringUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NomadService {
    private final NomadWebClient nomadWebClient;

    private final MockFwProperties mockFwProperties;

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

        String envsAsString = jobStartDataDto.getEnvs().entrySet().stream()
                .map(entry -> "\"" + entry.getKey() + "\" : \"" + entry.getValue() + "\"")
                .collect(Collectors.joining(","));

        String jsonTemplate = ResourceUtils.getStringFromResources("templates/nomad-springboot-docker.json");
        String job = String.format(jsonTemplate, jobStartDataDto.getAppName(), imageName, envsAsString);
        
        return nomadWebClient.startJob(jobStartDataDto.getNomadUrl(), job);
    }

    public Mono<Boolean> stopAndPurgeJob(String nomadUrl, String appName) {
        return nomadWebClient.stopAndPurgeJob(nomadUrl, appName);
    }

    public Flux<FixedJobDto> getFixedJobs() {
        List<String> fileNames = ResourceUtils.listFilesFromDirectory("fixed-templates");
        return Flux.fromIterable(parseFixedJobDtoFromFilenames(fileNames));
    }

    private static List<FixedJobDto> parseFixedJobDtoFromFilenames(final List<String> filenames) {
        List<FixedJobDto> fixedJobDtos = new ArrayList<>();

        Pattern pattern = Pattern.compile("nomad-(\\w+)-(\\d+\\.\\d+)\\.json");

        for (String filename : filenames) {
            Matcher matcher = pattern.matcher(filename);

            if (matcher.matches()) {
                String appName = matcher.group(1);
                String appVersion = matcher.group(2);

                FixedJobDto fixedJobDto = FixedJobDto.builder()
                        .name(appName)
                        .version(appVersion)
                        .fileName(filename)
                        .build();

                fixedJobDtos.add(fixedJobDto);
            }
        }

        return fixedJobDtos;
    }

    public Mono<Boolean> startFixedJob(final FixedJobDto fixedJobDto) {

        String jobFileLocation = "fixed-templates/".concat(fixedJobDto.getFileName());
        String job = ResourceUtils.getStringFromResources(jobFileLocation);

        return nomadWebClient.startJob(fixedJobDto.getNomadUrl(), job);
    }

    public Mono<Boolean> startLocalJob(MultipartFile jarFile, String nomadUrl) {
        String fileName = jarFile.getOriginalFilename();
        assert fileName != null;
        String localPath = mockFwProperties.getLocalShareFolderPath().concat(fileName);

        try {
            Files.copy(jarFile.getInputStream(), Path.of(localPath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new LaunchingLocalJobException("Error while copying jar", ex);
        }

        String vmPath = mockFwProperties.getVmShareFolderPath().concat(fileName);
        String jsonTemplate = ResourceUtils.getStringFromResources("templates/nomad-springboot-java.json");
        String job = String.format(jsonTemplate, fileName, vmPath);

        return nomadWebClient.startJob(nomadUrl, job);
    }
}
