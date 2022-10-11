package com.korbiak.service.service;

import com.korbiak.service.dto.bing.BingMapPolygon;
import com.korbiak.service.dto.bing.Option;
import com.korbiak.service.model.entities.WeatherConditions;
import com.korbiak.service.model.weathermodels.WeatherApiResponse;

import java.util.List;

public interface ElectTreatmentService {

    List<BingMapPolygon> getAllMapPolygons(int jumpTime);

    List<BingMapPolygon> convertTo(List<WeatherConditions> apiResponses);

    BingMapPolygon convertTo(WeatherConditions apiResponse);

    Option getColor(int level);
}
