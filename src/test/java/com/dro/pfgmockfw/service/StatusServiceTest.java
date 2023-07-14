package com.dro.pfgmockfw.service;

import com.dro.pfgmockfw.client.ServerStatusWebClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatusServiceTest {

    @Mock
    private ServerStatusWebClient serverStatusWebClient;

    @InjectMocks
    private StatusService statusService;

    @Test
    void checkServerActive_returnsTrue_whenServerIsActive() {
        //given
        String serverUrl = "http://localhost:8888";
        when(serverStatusWebClient.checkServerActive(anyString())).thenReturn(Mono.just(true));

        //when //then
        StepVerifier.create(statusService.checkServerActive(serverUrl))
                .expectNext(true)
                .expectComplete()
                .verify();
    }

    @Test
    void checkServerActive_returnsFalse_whenServerIsInactive() {
        //given
        String serverUrl = "http://localhost:8888";
        when(serverStatusWebClient.checkServerActive(anyString())).thenReturn(Mono.just(false));

        //when //then
        StepVerifier.create(statusService.checkServerActive(serverUrl))
                .expectNext(false)
                .expectComplete()
                .verify();
    }
}
