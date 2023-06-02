package com.dro.pfgmockfw.service;

import com.dro.pfgmockfw.client.NomadWebClient;
import com.dro.pfgmockfw.model.nomad.JobStatusType;
import com.dro.pfgmockfw.model.nomad.RunningJobDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NomadServiceTest {

    @Mock
    private NomadWebClient nomadWebClient;

    @InjectMocks
    private NomadService nomadService;

    @Test
    public void getRunningJobs_returnsMonoOfRunningJobDtoArray() {
        //given
        RunningJobDto[] jobDtos = new RunningJobDto[]{
                RunningJobDto.builder().id("job1").status(JobStatusType.RUNNING).build(),
                RunningJobDto.builder().id("job2").status(JobStatusType.PENDING).build()
        };

        when(nomadWebClient.getRunningJobs(anyString())).thenReturn(Mono.just(jobDtos));

        //when
        Mono<RunningJobDto[]> resultMono = nomadService.getRunningJobs("http://localhost:8888");

        //then
        StepVerifier.create(resultMono)
                .expectNext(jobDtos)
                .expectComplete()
                .verify();
    }
}




