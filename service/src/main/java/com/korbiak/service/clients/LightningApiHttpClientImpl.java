package com.korbiak.service.clients;

import com.korbiak.service.config.LightningApiConfig;
import com.korbiak.service.model.lightningmodels.LightningResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class LightningApiHttpClientImpl implements LightningApiHttpClient {
    private final RestTemplate restTemplate;

    private final LightningApiConfig apiConfig;

    @Override
    public List<HashMap<String, HashMap>> call(String lat, String lon, String weatherId) {
        ResponseEntity<List> responseEntity;
        try {
            responseEntity = restTemplate.exchange(apiConfig.getUrl(), HttpMethod.GET, new HttpEntity<>(new HttpHeaders()),
                    List.class,
                    lat, lon, (int) Double.valueOf(weatherId).doubleValue());
        } catch (RestClientException ex) {
            log.warn("Error calling service : {}, ex : {}", apiConfig.getUrl(), ex.getMessage());
            return null;
        }

        //ok response
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("200 response from service : {}", apiConfig.getUrl());
            return responseEntity.getBody();
        }

        //bad response 400, 500 and etc.
        log.warn("{} response from service : {}", responseEntity.getStatusCode().value(), apiConfig.getUrl());
        return null;
    }
}
