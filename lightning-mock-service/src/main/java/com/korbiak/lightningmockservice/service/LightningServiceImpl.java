package com.korbiak.lightningmockservice.service;

import com.korbiak.lightningmockservice.config.WeatherCondTreatConfig;
import com.korbiak.lightningmockservice.model.Coord;
import com.korbiak.lightningmockservice.model.LightningResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class LightningServiceImpl implements LightningService {

    private final WeatherCondTreatConfig weatherCondTreatConfig;
    private final Random random = new Random();

    @Override
    public List<LightningResponse> findLighting(double lat, double lon, int weatherCode) {
        List<LightningResponse> lightningResponses = new ArrayList<>();
        if (weatherCondTreatConfig.getWeatherConditions().contains(weatherCode)) {
            int numberOfHits = ThreadLocalRandom.current().nextInt(1, 4); // number random lightning hits 1-3
            for (int i = 0; i < numberOfHits; i++) {
                double latShift = ThreadLocalRandom.current().nextDouble(0.1, 0.3);
                double lonShift = ThreadLocalRandom.current().nextDouble(0.1, 0.3);
                double currentLat = lat;
                double currentLon = lon;
                //random lat
                if (random.nextBoolean()) {
                    currentLat = currentLat + latShift;
                } else {
                    currentLat = currentLat - latShift;
                }
                //rand lon
                if (random.nextBoolean()) {
                    currentLon = currentLon + lonShift;
                } else {
                    currentLon = currentLon - lonShift;
                }
                lightningResponses.add(new LightningResponse(new Coord(currentLon, currentLat)));
            }
        }
        return lightningResponses;
    }
}
