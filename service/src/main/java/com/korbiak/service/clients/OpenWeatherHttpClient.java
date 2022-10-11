package com.korbiak.service.clients;

import com.korbiak.service.model.weathermodels.WeatherApiResponse;
import com.korbiak.service.model.weathermodels.WeatherApiResponses;

public interface OpenWeatherHttpClient {

    WeatherApiResponse call(String lat, String lon);

    WeatherApiResponses callForecast(String lat, String lon);
}
