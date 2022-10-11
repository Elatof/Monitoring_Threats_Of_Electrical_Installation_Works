package com.korbiak.service.service;

import com.korbiak.service.model.weathermodels.Weather;
import com.korbiak.service.model.weathermodels.WeatherApiResponse;

public interface WeatherScheduler {

    void updateWeatherCondition();
    Weather getCriticalWeather(WeatherApiResponse weatherApiResponse);
}
