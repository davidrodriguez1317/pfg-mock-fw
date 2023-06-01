package com.dro.pfgmockfw.integration.stubs;

import org.springframework.http.HttpStatus;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class NomadStubs {
    public static void stubNomadRunningJobs(){
        stubFor(get(urlEqualTo("/v1/jobs"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\": \"Job 1\", \"status\": \"running\"}, " +
                                "{\"id\": \"Job 2\", \"status\": \"complete\"}]")));

    }

    public static void stubNomadRunningJobsNoContent(){
        stubFor(get(urlEqualTo("/v1/jobs"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")));

    }

    public static void stubNomadRunningJobsWithStatus(int httpStatus){
        stubFor(get(urlEqualTo("/v1/jobs"))
                .willReturn(aResponse()
                        .withStatus(httpStatus)));

    }
}
