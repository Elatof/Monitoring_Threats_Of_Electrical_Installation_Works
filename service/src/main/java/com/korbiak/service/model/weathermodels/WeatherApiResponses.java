package com.korbiak.service.model.weathermodels;

import lombok.Data;

import java.util.List;

@Data
public class WeatherApiResponses {
    private List<WeatherApiResponse> list;
}
