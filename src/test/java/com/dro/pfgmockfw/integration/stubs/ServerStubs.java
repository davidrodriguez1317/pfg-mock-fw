package com.dro.pfgmockfw.integration.stubs;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ServerStubs {

    public static void stubServerResponseStatus(int httpStatus){
        stubFor(get(anyUrl())
                .willReturn(aResponse()
                        .withStatus(httpStatus)));

    }
}
