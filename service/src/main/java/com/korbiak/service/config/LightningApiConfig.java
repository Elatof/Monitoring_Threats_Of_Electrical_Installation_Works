package com.korbiak.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix = "lightning-api")
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class LightningApiConfig {
    private String url;
    private String format;
    private String iconImage;
}
