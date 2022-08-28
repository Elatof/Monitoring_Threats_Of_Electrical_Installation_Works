package com.korbiak.service.clients;

import com.korbiak.service.config.OpenApiConfig;
import com.korbiak.service.model.weathermodels.WeatherApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Http client service for calling Open Weather Api
 */
@Component
@Slf4j
public class OpenWeatherHttpClientImpl implements OpenWeatherHttpClient {
    private final RestTemplate restTemplate;

    private final HttpEntity<?> commonEntity;

    private OpenApiConfig openApiConfig;

    public OpenWeatherHttpClientImpl() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        commonEntity = new HttpEntity<>(headers);
        restTemplate = new RestTemplate();
    }

    @Autowired
    public void setOpenApiConfig(OpenApiConfig openApiConfig) {
        this.openApiConfig = openApiConfig;
    }

    /**
     * Get current weather condition using
     *
     * @param lat latitude
     * @param lon longitude
     * @return object of weather info
     */
    public WeatherApiResponse call(String lat, String lon) {
        ResponseEntity<WeatherApiResponse> responseEntity;
        try {
            responseEntity = restTemplate.exchange(openApiConfig.getUrl(),
                    HttpMethod.GET, commonEntity, WeatherApiResponse.class, lat, lon, openApiConfig.getToken());
        } catch (RestClientException ex) {
            log.warn("Error calling service : {}, ex : {}", openApiConfig.getUrl(), ex.getMessage());
            return null;
        }

        //ok response
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("200 response from service : {}", openApiConfig.getUrl());
            return responseEntity.getBody();
        }

        //bad response 400, 500 and etc.
        log.warn("{} response from service : {}", responseEntity.getStatusCode().value(), openApiConfig.getUrl());
        return null;
    }
}


