package com.korbiak.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "config-conditions")
@PropertySource(value = "classpath:weatherConditionsTreatment.yml", factory = YamlPropertySourceFactory.class)
public class WeatherCondTreatConfig {
    @Data
    public static class WeatherCondition {
        private String description;
        private int treatmentLevel;
    }

    private int criticalWindSpeed;
    private Map<Integer, WeatherCondition> weatherConditions;
}
