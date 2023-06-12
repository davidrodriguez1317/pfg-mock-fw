package com.dro.pfgmockfw.service;

import com.dro.pfgmockfw.client.NomadWebClient;
import com.dro.pfgmockfw.mapper.NomadMapper;
import com.dro.pfgmockfw.model.nomad.FixedJobDto;
import com.dro.pfgmockfw.model.nomad.JobStartDataDto;
import com.dro.pfgmockfw.model.nomad.JobStatusType;
import com.dro.pfgmockfw.model.nomad.RunningJobDto;
import com.dro.pfgmockfw.model.nomad.server.ServerRunningJobDto;
import com.dro.pfgmockfw.utils.ResourceUtils;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NomadServiceTest {

    @Mock
    private NomadWebClient nomadWebClient;

    @InjectMocks
    private NomadService nomadService;

    @Captor
    private ArgumentCaptor<String> jobCaptor;

    @Test
    public void getRunningJobs_returnsMonoOfRunningJobDtoArray() {
        //given
        ServerRunningJobDto[] jobDtos = new ServerRunningJobDto[]{
                ServerRunningJobDto.builder().id("job1").status(JobStatusType.RUNNING).build(),
                ServerRunningJobDto.builder().id("job2").status(JobStatusType.PENDING).build()
        };

        when(nomadWebClient.getRunningJobs(anyString())).thenReturn(Flux.fromArray(jobDtos));

        //when
        Flux<RunningJobDto> resultFlux = nomadService.getRunningJobs("http://localhost:8888");

        //then
        StepVerifier.create(resultFlux)
                .expectNext(NomadMapper.INSTANCE.fromServerRunningJobDto(jobDtos[0]))
                .expectNext(NomadMapper.INSTANCE.fromServerRunningJobDto(jobDtos[1]))
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetFixedJobs() {

        //given //when
        Flux<FixedJobDto> fixedJobsFlux = nomadService.getFixedJobs();

        //then
        List<FixedJobDto> fixedJobsList = fixedJobsFlux.collectList().block();
        assertNotNull(fixedJobsList);
        assertEquals(1, fixedJobsList.size());

        FixedJobDto fixedJobDto1 = fixedJobsList.get(0);
        assertEquals("keycloak", fixedJobDto1.getName());
        assertEquals("18.0", fixedJobDto1.getVersion());
        assertEquals("nomad-keycloak-18.0.json", fixedJobDto1.getFileName());

    }

    @Test
    public void testStartJob() throws JSONException {

        //given
        String expectedJob = ResourceUtils.getStringFromResources("fixtures/start-job.json");

        JobStartDataDto jobStartDataDto =  JobStartDataDto.builder()
                .dockerUrl("http://localhost:5000")
                .nomadUrl("http://localhost:4646")
                .appName("some-job")
                .appVersion("1.0.0-SNAPSHOT")
                .envs(Map.of("ENV_1", "value1", "ENV_2", "value2"))
                .build();


        //when
        nomadService.startJob(jobStartDataDto);

        // then
        verify(nomadWebClient).startJob(eq(jobStartDataDto.getNomadUrl()), jobCaptor.capture());

        JSONAssert.assertEquals(expectedJob, jobCaptor.getValue(), false);
    }

}




