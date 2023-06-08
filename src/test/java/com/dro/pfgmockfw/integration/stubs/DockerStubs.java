package com.dro.pfgmockfw.integration.stubs;

import com.dro.pfgmockfw.utils.TestUtils;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class DockerStubs {
    public static void stubDockerCatalog(){
        stubFor(get(urlEqualTo("/v2/_catalog"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"repositories\":[\"pfg-price-calculator\",\"pfg-product-cost\"]}")));

    }

    public static void stubDockerTags(String repository, List<String> tags){
        String endpoint = String.format("/v2/%s/tags/list", repository);
        String tagsContent = TestUtils.joinWithCommasInsideQuotes(tags);
        String bodyResponse = String.format("{\"name\":\"%s\",\"tags\":[%s]}", repository, tagsContent);

        stubFor(get(urlEqualTo(endpoint))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyResponse)));

    }

    public static void stubDockerCatalogNoContent(){
        stubFor(get(urlEqualTo("/v2/_catalog"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"repositories\":[]}")));

    }

    public static void stubDockerCatalogWithStatus(int httpStatus){
        stubFor(get(urlEqualTo("/v2/_catalog"))
                .willReturn(aResponse()
                        .withStatus(httpStatus)));

    }
}
