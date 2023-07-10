package com.dro.pfgmockfw.service;

import com.dro.pfgmockfw.client.NomadWebClient;
import com.dro.pfgmockfw.config.MockFwProperties;
import com.dro.pfgmockfw.exception.LaunchingLocalJobException;
import com.dro.pfgmockfw.mapper.NomadMapper;
import com.dro.pfgmockfw.model.nomad.FixedJobStartDto;
import com.dro.pfgmockfw.model.nomad.JobStartDto;
import com.dro.pfgmockfw.model.nomad.LocalJobStartDto;
import com.dro.pfgmockfw.model.nomad.RunningJobDto;
import com.dro.pfgmockfw.model.nomad.server.ServerRunningJobDto;
import com.dro.pfgmockfw.utils.ResourceUtils;
import com.dro.pfgmockfw.utils.StringUtils;
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

    public Mono<Boolean> startJob(JobStartDto jobStartDto) {

        String imageName = StringUtils.stripHttpFromUrl(jobStartDto.getDockerUrl())
                .concat("/")
                .concat(jobStartDto.getAppName())
                .concat(":")
                .concat(jobStartDto.getAppVersion());

        String envsAsString = jobStartDto.getEnvs().entrySet().stream()
                .map(entry -> "\"" + entry.getKey() + "\" : \"" + entry.getValue() + "\"")
                .collect(Collectors.joining(","));

        String jsonTemplate = ResourceUtils.getStringFromResources("templates/nomad-springboot-docker.json");
        String job = String.format(jsonTemplate, jobStartDto.getAppName(), imageName, envsAsString);
        
        return nomadWebClient.startJob(jobStartDto.getNomadUrl(), job);
    }

    public Mono<Boolean> stopAndPurgeJob(String nomadUrl, String appName) {
        return nomadWebClient.stopAndPurgeJob(nomadUrl, appName);
    }

    public Flux<FixedJobStartDto> getFixedJobs() {
        List<String> fileNames = ResourceUtils.listFilesFromDirectory("fixed-templates");
        return Flux.fromIterable(parseFixedJobDtoFromFilenames(fileNames));
    }

    private static List<FixedJobStartDto> parseFixedJobDtoFromFilenames(final List<String> filenames) {
        List<FixedJobStartDto> fixedJobStartDtos = new ArrayList<>();

        Pattern pattern = Pattern.compile("nomad-(\\w+)-(\\d+\\.\\d+)\\.json");

        for (String filename : filenames) {
            Matcher matcher = pattern.matcher(filename);

            if (matcher.matches()) {
                String appName = matcher.group(1);
                String appVersion = matcher.group(2);

                FixedJobStartDto fixedJobStartDto = FixedJobStartDto.builder()
                        .name(appName)
                        .version(appVersion)
                        .fileName(filename)
                        .build();

                fixedJobStartDtos.add(fixedJobStartDto);
            }
        }

        return fixedJobStartDtos;
    }

    public Mono<Boolean> startFixedJob(final FixedJobStartDto fixedJobStartDto) {

        String jobFileLocation = "fixed-templates/".concat(fixedJobStartDto.getFileName());
        String job = ResourceUtils.getStringFromResources(jobFileLocation);

        return nomadWebClient.startJob(fixedJobStartDto.getNomadUrl(), job);
    }

    public Mono<Boolean> uploadJar(MultipartFile jarFile) {
        String fileName = jarFile.getOriginalFilename();
        assert fileName != null;
        String localPath = mockFwProperties.getLocalShareFolderPath().concat(fileName);

        try {
            Files.copy(jarFile.getInputStream(), Path.of(localPath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new LaunchingLocalJobException("Error while copying jar", ex);
        }
        return Mono.just(true);
    }

    public Mono<Boolean> startLocalJob( LocalJobStartDto localJobStartDto) {
        String name = localJobStartDto.getName();
        assert name != null;

        String vmPath = mockFwProperties.getVmShareFolderPath().concat(name);
        String jsonTemplate = ResourceUtils.getStringFromResources("templates/nomad-springboot-raw.json");
        String nameWithoutExtension = StringUtils.removeFileExtension(name);


        String envsAsString = localJobStartDto.getEnvs().entrySet().stream()
                .map(entry -> "\"" + entry.getKey() + "\" : \"" + entry.getValue() + "\"")
                .collect(Collectors.joining(","));

        String job = String.format(jsonTemplate, nameWithoutExtension, vmPath, envsAsString);

        return nomadWebClient.startJob(localJobStartDto.getNomadUrl(), job);
    }
}
