package com.korbiak.service.clients;

import com.korbiak.service.model.weathermodels.WeatherApiResponse;

public interface OpenWeatherHttpClient {

    WeatherApiResponse call(String lat, String lon);
}
