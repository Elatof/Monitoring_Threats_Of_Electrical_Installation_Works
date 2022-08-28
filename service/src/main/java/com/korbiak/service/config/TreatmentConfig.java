package com.korbiak.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "treatment-config")
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class TreatmentConfig {
    private int radius;
    private int points;
    private Map<Integer, String> levelsColor;
}
