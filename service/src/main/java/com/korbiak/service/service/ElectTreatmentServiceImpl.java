package com.korbiak.service.service;

import com.korbiak.service.cache.WeatherCache;
import com.korbiak.service.config.PointsCoordinateConfig;
import com.korbiak.service.config.TreatmentConfig;
import com.korbiak.service.config.WeatherCondTreatConfig;
import com.korbiak.service.model.bing.BingMapPolygon;
import com.korbiak.service.model.bing.Option;
import com.korbiak.service.model.weathermodels.Coord;
import com.korbiak.service.model.weathermodels.Weather;
import com.korbiak.service.model.weathermodels.WeatherApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElectTreatmentServiceImpl implements ElectTreatmentService {
    private final TreatmentConfig treatmentConfig;
    private final WeatherCache cache;
    private final PointsCoordinateConfig pointsCoordinateConfig;
    private final WeatherCondTreatConfig weatherCondTreatConfig;

    @Override
    public List<BingMapPolygon> getAllMapPolygons() {
        List<WeatherApiResponse> responseList = cache.get();

        //add mock responses for another parts of country if needed
        if (!pointsCoordinateConfig.isAcceptWestern()) {
            List<List<String>> westernPoints = pointsCoordinateConfig.getWesternPoints();
            List<WeatherApiResponse> collect = buildMockWeatherResponses(westernPoints);
            responseList.addAll(collect);
        }
        if (!pointsCoordinateConfig.isAcceptCentral()) {
            List<List<String>> centralPoints = pointsCoordinateConfig.getCentralPoints();
            List<WeatherApiResponse> collect = buildMockWeatherResponses(centralPoints);
            responseList.addAll(collect);
        }
        if (!pointsCoordinateConfig.isAcceptEastern()) {
            List<List<String>> easternPoints = pointsCoordinateConfig.getEasternPoints();
            List<WeatherApiResponse> collect = buildMockWeatherResponses(easternPoints);
            responseList.addAll(collect);
        }
        return convertTo(responseList);
    }

    /**
     * Build mock responses dor not included part of country
     *
     * @param centralPoints coordinates
     * @return list of mock WeatherApiResponse responses
     */
    private List<WeatherApiResponse> buildMockWeatherResponses(List<List<String>> centralPoints) {
        return centralPoints.stream()
                .map(coordinates -> {
                    double lat = Double.parseDouble(coordinates.get(0));
                    double lon = Double.parseDouble(coordinates.get(1));
                    WeatherApiResponse response = new WeatherApiResponse();
                    Coord coord = new Coord();
                    coord.setLat(lat);
                    coord.setLon(lon);
                    response.setCoord(coord);
                    return response;
                }).collect(Collectors.toList());
    }

    @Override
    public List<BingMapPolygon> convertTo(List<WeatherApiResponse> apiResponses) {
        return apiResponses.stream()
                .map(this::convertTo)
                .collect(Collectors.toList());
    }

    @Override
    public BingMapPolygon convertTo(WeatherApiResponse apiResponse) {
        BingMapPolygon bingMapPolygon = new BingMapPolygon();
        bingMapPolygon.setRadius(treatmentConfig.getRadius());
        bingMapPolygon.setPoints(treatmentConfig.getPoints());
        bingMapPolygon.setCenter(List.of(apiResponse.getCoord().getLat(),
                apiResponse.getCoord().getLon()));

        //setting treatment level with color
        int treatmentLevel = calculateTreatmentLevel(apiResponse);
        bingMapPolygon.setOption(getColor(treatmentLevel));


        return bingMapPolygon;
    }

    /**
     * Calculate treatment level from api response
     *
     * @param apiResponse OpenApiWeather response
     * @return 1-10 integer value
     */
    public int calculateTreatmentLevel(WeatherApiResponse apiResponse) {
        List<Weather> weather = apiResponse.getWeather();
        int treatmentLevel = 0;

        //mock response
        if (Objects.isNull(weather)) {
            return treatmentLevel;
        }
        Map<Integer, WeatherCondTreatConfig.WeatherCondition> weatherConditions = weatherCondTreatConfig.getWeatherConditions();
        treatmentLevel = weather.stream().mapToInt(value -> {
            double valueId = value.getId();
            if (weatherConditions.containsKey((int) valueId)) {
                return weatherConditions.get((int) valueId).getTreatmentLevel();
            }
            return 0;
        }).sum();

        if (treatmentLevel == 0) {
            treatmentLevel ++;
        }
        if (apiResponse.getWind().getSpeed() > weatherCondTreatConfig.getCriticalWindSpeed()) {
            treatmentLevel ++;
        }

        return Math.min(treatmentLevel, 10);
    }

    public Option getColor(int treatmentLevel) {
        Option option = new Option();
        Map<Integer, String> levelsColor = treatmentConfig.getLevelsColor();
        option.setFillColor(levelsColor.get(treatmentLevel));
        return option;
    }
}
