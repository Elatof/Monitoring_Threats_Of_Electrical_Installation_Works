package com.korbiak.service.service;

import com.korbiak.service.model.bing.BingMapPolygon;
import com.korbiak.service.model.bing.Option;
import com.korbiak.service.model.weathermodels.WeatherApiResponse;

import java.util.List;

public interface ElectTreatmentService {

    List<BingMapPolygon> getAllMapPolygons();

    List<BingMapPolygon> convertTo(List<WeatherApiResponse> apiResponses);

    BingMapPolygon convertTo(WeatherApiResponse apiResponse);

    int calculateTreatmentLevel(WeatherApiResponse apiResponse);

    Option getColor(int level);
}
