package com.dro.pfgmockfw.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("") // La URL se introducirá en cada llamada
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .filters(exchangeFilterFunctions -> {
                    exchangeFilterFunctions.add(logRequest());
                    exchangeFilterFunctions.add(logResponse());
                })
                .build();

    }

    public ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            logRequestDetails(clientRequest);
            return Mono.just(clientRequest);
        });
    }

    public ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            logResponseDetails(clientResponse);
            return Mono.just(clientResponse);
        });
    }

    private void logRequestDetails(ClientRequest clientRequest) {
        // Obtener los detalles de la solicitud
        String method = clientRequest.method().name();
        String url = clientRequest.url().toString();
        HttpHeaders headers = clientRequest.headers();

        // Registrar los detalles de la solicitud
        log.info("Request Method: " + method);
        log.info("Request URL: " + url);
        log.info("Request Headers: " + headers);
    }

    private void logResponseDetails(ClientResponse clientResponse) {
        // Obtener los detalles de la respuesta
        int statusCode = clientResponse.statusCode().value();
        HttpHeaders headers = clientResponse.headers().asHttpHeaders();

        // Registrar los detalles de la respuesta
        log.info("Response Status Code: " + statusCode);
        log.info("Response Headers: " + headers);
    }
}