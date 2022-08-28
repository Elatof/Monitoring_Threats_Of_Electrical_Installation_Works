package com.korbiak.service.service;

import com.korbiak.service.cache.WeatherCache;
import com.korbiak.service.clients.OpenWeatherHttpClient;
import com.korbiak.service.model.bing.Option;
import com.korbiak.service.model.weathermodels.WeatherApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final OpenWeatherHttpClient openWeatherHttpClient;
    private final WeatherCache weatherCache;
    private final ElectTreatmentService treatmentService;

    /**
     * Get weather conditional
     * @param lat latitude
     * @param lon longitude
     * @return object of weather info
     */
    @Override
    public WeatherApiResponse getCurrentWeather(String lon, String lat) {
        //call open weather api
        WeatherApiResponse response = openWeatherHttpClient.call(lat, lon);
        if (response == null) {
            throw new RestClientException("Error calling weather api");
        }

        int treatmentLevel = treatmentService.calculateTreatmentLevel(response);
        response.setTreatmentLevel(treatmentLevel);

        Option color = treatmentService.getColor(treatmentLevel);
        response.setTreatmentColor(color.getFillColor());

        return response;
    }

    @Override
    public List<WeatherApiResponse> getAllWeather() {
        return weatherCache.get();
    }
}
