package com.korbiak.service.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.korbiak.service.model.weathermodels.WeatherApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherCache {

    private final WeatherCacheLoader weatherCacheLoader;

    private LoadingCache<String, List<WeatherApiResponse>> cache;

    private static final String DEFAULT_KEY = "default";

    @PostConstruct
    void initCache() {
        cache = CacheBuilder.newBuilder()
                .refreshAfterWrite(Duration.of(1, ChronoUnit.HOURS)) //update cache each 1 hour
                .build(weatherCacheLoader);
        //load cache for first time
        get();
    }

    public List<WeatherApiResponse> get() {
        try {
            List<WeatherApiResponse> apiResponses = cache.get(DEFAULT_KEY);
            return new ArrayList<>(apiResponses);
        } catch (ExecutionException e) {
            log.error("Error getting weather info from cache: {}", e.getMessage());
            throw new CacheException("Error getting weather info from cache", e);
        }
    }


    public void refresh() {
        cache.refresh(DEFAULT_KEY);
    }
}
