package com.korbiak.service.service.impl;

import com.korbiak.service.clients.LightningApiHttpClient;
import com.korbiak.service.clients.OpenWeatherHttpClient;
import com.korbiak.service.config.WeatherCondTreatConfig;
import com.korbiak.service.config.WeatherCoordinateConfig;
import com.korbiak.service.model.PartOfCountry;
import com.korbiak.service.model.entities.Coordinate;
import com.korbiak.service.model.entities.LightningCoordinate;
import com.korbiak.service.model.entities.WeatherConditions;
import com.korbiak.service.model.lightningmodels.LightningResponse;
import com.korbiak.service.model.weathermodels.Coord;
import com.korbiak.service.model.weathermodels.Weather;
import com.korbiak.service.model.weathermodels.WeatherApiResponse;
import com.korbiak.service.model.weathermodels.WeatherApiResponses;
import com.korbiak.service.repos.LightningCoordinateRepo;
import com.korbiak.service.repos.WeatherRepo;
import com.korbiak.service.service.WeatherScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherLightningSchedulerImpl implements WeatherScheduler {
    private final WeatherRepo weatherRepo;
    private final WeatherCoordinateConfig weatherCoordinateConfig;
    private final OpenWeatherHttpClient openWeatherHttpClient;
    private final WeatherCondTreatConfig weatherCondTreatConfig;
    private final LightningCoordinateRepo lightningCoordinateRepo;
    private final LightningApiHttpClient lightningApiHttpClient;

    @Override
    @Scheduled(cron = "0 0 3 ? * * ") //every day at 3 am
    public void updateWeatherCondition() {
        log.info("Start updating weather in db");
        log.info("Accepting western part");
        List<List<String>> allPoints = new ArrayList<>(weatherCoordinateConfig.getWesternPoints());
        log.info("Accepting central part");
        allPoints.addAll(weatherCoordinateConfig.getCentralPoints());
        log.info("Accepting eastern part");
        allPoints.addAll(weatherCoordinateConfig.getEasternPoints());

        int count = 0;
        for (List<String> coordinate : allPoints) {
            String lat = coordinate.get(0);
            String lon = coordinate.get(1);

            WeatherApiResponses response = openWeatherHttpClient.callForecast(lat, lon);
            saveToDb(response, lat, lon);

            count++;
            if (count == weatherCoordinateConfig.getLimitOfCallsPerMinute()) {
                log.info("Limitation of apis exceeded, waiting 50 seconds");
                try {
                    TimeUnit.SECONDS.sleep(55);
                } catch (InterruptedException e) {
                    log.info("Interrupt exception, retrying waiting");
                    break;
                }
                count = 0;
            }
        }
    }

    private void saveToDb(WeatherApiResponses response, String lat, String lon) {
        Coordinate coordinate = weatherRepo.findByLatAndLon(Double.parseDouble(lat), Double.parseDouble(lon));
        if (coordinate == null) {
            coordinate = new Coordinate();
            coordinate.setLat(Double.parseDouble(lat));
            coordinate.setLon(Double.parseDouble(lon));
            coordinate.setPartOfCountry(PartOfCountry.CENTRAL);
        }

        List<WeatherConditions> conditionReports = coordinate.getConditionReports();
        for (WeatherApiResponse weatherApiResponse : response.getList()) {
            WeatherConditions conditions = conditionReports.stream()
                    .filter(weatherConditions -> weatherConditions.getDateTime()
                        .equals(new Timestamp(weatherApiResponse.getDt() * 1000L)))
                    .findFirst()
                    .orElse(null);
            if (conditions != null) {
                continue;
            }

            Weather weather = getCriticalWeather(weatherApiResponse);

            callLightningService(weather, lat, lon, weatherApiResponse);
            conditions = new WeatherConditions();
            conditions.setWeatherId(weather.getId());
            conditions.setDescription(weather.getDescription());
            conditions.setIcon(weather.getIcon());
            conditions.setDateTime(new Timestamp(weatherApiResponse.getDt() * 1000L));
            conditions.setDescription(weather.getDescription());
            conditions.setWindSpeed(weatherApiResponse.getWind().getSpeed());
            conditions.setMain(weather.getMain());
            conditions.setThreatLevel(weather.getCriticalLevel());
            conditionReports.add(conditions);
            conditions.setCoordinate(coordinate);

        }
        weatherRepo.save(coordinate);
    }

    private void callLightningService(Weather weather, String lat, String lon, WeatherApiResponse weatherApiResponse) {
        List<HashMap<String, HashMap>> call = lightningApiHttpClient.call(lat, lon, String.valueOf(weather.getId()));

        //save into db
        for (HashMap<String, HashMap> lightningResponse : call) {
            HashMap<String, Double> coord = lightningResponse.get("coord");
            LightningCoordinate lightningCoordinate = new LightningCoordinate();
            lightningCoordinate.setDateTime(new Timestamp(weatherApiResponse.getDt() * 1000L));
            lightningCoordinate.setLat(coord.get("lat"));
            lightningCoordinate.setLon(coord.get("lon"));
            lightningCoordinateRepo.save(lightningCoordinate);
        }
    }

    public Weather getCriticalWeather(WeatherApiResponse weatherApiResponse) {
        Map<Integer, WeatherCondTreatConfig.WeatherCondition> weatherConditions
                = weatherCondTreatConfig.getWeatherConditions();

        int currentLevel = 0;
        Weather targetWeather = null;
        for (Weather weather : weatherApiResponse.getWeather()) {
            int threatLevel = 0;
            if (weatherConditions.containsKey((int) weather.getId())) {
                threatLevel = weatherConditions.get((int) weather.getId()).getTreatmentLevel();
            }
            if (threatLevel >= currentLevel) {
                currentLevel = threatLevel;
                targetWeather = weather;
            }
        }
        if (weatherApiResponse.getWind().getSpeed() > weatherCondTreatConfig.getCriticalWindSpeed()) {
            currentLevel++;
        }

        if (currentLevel == 0) {
            currentLevel ++;
        }
        targetWeather.setCriticalLevel(currentLevel);
        return targetWeather;
    }
}
