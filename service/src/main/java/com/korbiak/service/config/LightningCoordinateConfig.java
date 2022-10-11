package com.korbiak.service.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "lightning-config")
@PropertySource(value = "classpath:pointsCoordinateConf.yml", factory = YamlPropertySourceFactory.class)
public class LightningCoordinateConfig {
    private List<List<String>> points;
}
