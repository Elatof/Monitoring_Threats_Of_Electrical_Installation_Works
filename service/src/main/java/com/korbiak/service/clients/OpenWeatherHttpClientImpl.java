package com.korbiak.service.clients;

import com.korbiak.service.config.OpenApiConfig;
import com.korbiak.service.model.weathermodels.WeatherApiResponse;
import com.korbiak.service.model.weathermodels.WeatherApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * Http client service for calling Open Weather Api
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OpenWeatherHttpClientImpl implements OpenWeatherHttpClient {
    private final RestTemplate restTemplate;
    private final OpenApiConfig openApiConfig;

    private HttpEntity<?> commonEntity;

    @PostConstruct
    public void setUpCommonEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        commonEntity = new HttpEntity<>(headers);
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

        if (extract(responseEntity)) return responseEntity.getBody();
        return null;
    }

    @Override
    public WeatherApiResponses callForecast(String lat, String lon) {
        ResponseEntity<WeatherApiResponses> responseEntity;
        try {
            responseEntity = restTemplate.exchange(openApiConfig.getForecastUrl(),
                    HttpMethod.GET, commonEntity, WeatherApiResponses.class, lat, lon, openApiConfig.getToken());
        } catch (RestClientException ex) {
            log.warn("Error calling service : {}, ex : {}", openApiConfig.getForecastUrl(), ex.getMessage());
            return null;
        }

        if (extract(responseEntity)) return responseEntity.getBody();
        return null;
    }

    private boolean extract(ResponseEntity<?> responseEntity) {
        //ok response
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("200 response from service : {}", openApiConfig.getUrl());
            return true;
        }

        //bad response 400, 500 and etc.
        log.warn("{} response from service : {}", responseEntity.getStatusCode().value(), openApiConfig.getUrl());
        return false;
    }
}


