package com.korbiak.service.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class RestTemplateConfig {

    @Bean
    public RestTemplate getRestTemplateBean() {
        RestTemplate restTemplate = new RestTemplate();
        //additional configuration if needed (ssl)

        return restTemplate;
    }
}
