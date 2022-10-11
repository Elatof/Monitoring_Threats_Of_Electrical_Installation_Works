package com.korbiak.service.controller;

import com.korbiak.service.dto.bing.BingMapPolygon;
import com.korbiak.service.model.weathermodels.WeatherApiResponse;
import com.korbiak.service.service.ElectTreatmentService;
import com.korbiak.service.service.WeatherScheduler;
import com.korbiak.service.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("service-api/weather/")
@CrossOrigin(origins = "http://localhost:3000")
public class WeatherController {
    private final WeatherService weatherService;
    private final WeatherScheduler weatherScheduler;

    private final ElectTreatmentService treatmentService;

    @GetMapping("current")
    public WeatherApiResponse getCurrentWeather(@RequestParam String lon, @RequestParam String lat) {
        return weatherService.getCurrentWeather(lon, lat);
    }

    @GetMapping("all")
    public List<WeatherApiResponse> getCurrentWeather() {
        return weatherService.getAllWeather();
    }

    @GetMapping("all/polygons")
    public List<BingMapPolygon> getAllMapPolygons(@RequestParam int jumpTime) {
        return treatmentService.getAllMapPolygons(jumpTime);
    }

    @PostMapping("manual-update")
    public String updateWeather() {
        weatherScheduler.updateWeatherCondition();
        return "updated";
    }
}
