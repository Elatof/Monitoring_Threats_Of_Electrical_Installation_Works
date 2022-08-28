package com.korbiak.service.cache;

import com.google.common.cache.CacheLoader;
import com.korbiak.service.clients.OpenWeatherHttpClient;
import com.korbiak.service.config.PointsCoordinateConfig;
import com.korbiak.service.model.weathermodels.WeatherApiResponse;
import com.korbiak.service.service.ElectTreatmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherCacheLoader extends CacheLoader<String, List<WeatherApiResponse>> {
    private final PointsCoordinateConfig pointsCoordinateConfig;
    private final OpenWeatherHttpClient openWeatherHttpClient;

    @Override
    public List<WeatherApiResponse> load(String s) {
        log.info("Starting loading cache for '{}' key...", s);
        List<List<String>> allPoints = new ArrayList<>();
        if (pointsCoordinateConfig.isAcceptWestern()) {
            log.info("Accepting western part");
            allPoints.addAll(pointsCoordinateConfig.getWesternPoints());
        }
        if (pointsCoordinateConfig.isAcceptCentral()) {
            log.info("Accepting central part");
            allPoints.addAll(pointsCoordinateConfig.getCentralPoints());
        }
        if (pointsCoordinateConfig.isAcceptEastern()) {
            log.info("Accepting eastern part");
            allPoints.addAll(pointsCoordinateConfig.getEasternPoints());
        }


        List<WeatherApiResponse> responses = new ArrayList<>();
        for (List<String> coordinate : allPoints) {
            String lat = coordinate.get(0);
            String lon = coordinate.get(1);

            WeatherApiResponse response = openWeatherHttpClient.call(lat, lon);
            responses.add(response);
        }

        return responses;
    }
}
