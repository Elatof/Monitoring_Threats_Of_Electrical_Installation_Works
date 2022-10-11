package com.korbiak.lightningmockservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "config-conditions")
@PropertySource(value = "classpath:weatherCodes.yml", factory = YamlPropertySourceFactory.class)
public class WeatherCondTreatConfig {
    private List<Integer> weatherConditions;
}
