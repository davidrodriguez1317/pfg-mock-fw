package com.dro.pfgmockfw.integration.stubs;

import com.dro.pfgmockfw.utils.ResourceUtils;
import org.springframework.http.HttpStatus;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class NomadStubs {
    public static void stubNomadRunningJobs(){

        String bodyResponse = ResourceUtils.getStringFromResources("responses/nomad-running-jobs.json");

        stubFor(get(urlEqualTo("/v1/jobs"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyResponse)));

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

    public static void stubNomadStartJob(){
        stubFor(post(urlEqualTo("/v1/jobs"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())));

    }

    public static void stubNomadStartJobWithStatus(int httpStatus){
        stubFor(post(urlEqualTo("/v1/jobs"))
                .willReturn(aResponse()
                        .withStatus(httpStatus)));

    }

    public static void stubNomadStopJobWithSuccess() {
        String bodyResponse = ResourceUtils.getStringFromResources("responses/nomad-stop-job-success.json");

        stubFor(delete(urlPathMatching("\\/v1\\/job\\/.*"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyResponse)));
    }

    public static void stubNomadStopJobNoSuccess() {
        String bodyResponse = ResourceUtils.getStringFromResources("responses/nomad-stop-job-no-success.json");

        stubFor(delete(urlPathMatching("\\/v1\\/job\\/.*"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyResponse)));
    }

    public static void stubNomadStopJobWithStatus(int httpStatus){
        stubFor(delete(urlPathMatching("\\/v1\\/job\\/.*"))
                .willReturn(aResponse()
                        .withStatus(httpStatus)));

    }

    public static void stubNomadGetAllocations(){
        String bodyResponse = ResourceUtils.getStringFromResources("responses/nomad-allocations.json");

        stubFor(get(urlPathMatching("\\/v1\\/job\\/.*"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyResponse)));

    }

    public static void stubNomadGetAllocationsWithStatus(int httpStatus){
        stubFor(get(urlPathMatching("\\/v1\\/job\\/.*"))
                .willReturn(aResponse()
                        .withStatus(httpStatus)));

    }

    public static void stubNomadGetLogs(){
        String bodyResponse = ResourceUtils.getStringFromResources("responses/nomad-logs-base64.json");

        stubFor(get(urlPathMatching("\\/v1\\/client\\/.*"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyResponse)));

    }

    public static void stubNomadGetLogsWithStatus(int httpStatus){
        stubFor(get(urlPathMatching("\\/v1\\/client\\/.*"))
                .willReturn(aResponse()
                        .withStatus(httpStatus)));

    }
}
