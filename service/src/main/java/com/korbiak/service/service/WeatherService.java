package com.korbiak.service.service;

import com.korbiak.service.model.weathermodels.WeatherApiResponse;

import java.util.List;

public interface WeatherService {

    WeatherApiResponse getCurrentWeather(String lon, String lat);

    List<WeatherApiResponse> getAllWeather();
}
