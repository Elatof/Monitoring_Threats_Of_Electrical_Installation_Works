package com.korbiak.service.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "config")
@PropertySource(value = "classpath:pointsCoordinateConf.yml", factory = YamlPropertySourceFactory.class)
public class PointsCoordinateConfig {
    private int limitOfCallsPerMinute;

    private boolean acceptWestern;
    private boolean acceptCentral;
    private boolean acceptEastern;

    private List<List<String>> westernPoints;
    private List<List<String>> centralPoints;
    private List<List<String>> easternPoints;
}
