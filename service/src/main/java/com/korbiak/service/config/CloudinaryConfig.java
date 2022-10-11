package com.korbiak.service.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    private static final String CLOUD_NAME = "cloud_name";

    private static final String API_KEY = "api_key";

    private static final String API_SECRET = "api_secret";

    @Bean
    public Cloudinary cloudinaryBean(@Value("${cloudinaryValue.cloudName}") final String cloudName,
                                     @Value("${cloudinaryValue.apiKey}") final String apiKey,
                                     @Value("${cloudinaryValue.apiSecretKey}") final String apiSecretKey) {

        return new Cloudinary(ObjectUtils.asMap(
                CLOUD_NAME, cloudName,
                API_KEY, apiKey,
                API_SECRET, apiSecretKey));
    }

}
