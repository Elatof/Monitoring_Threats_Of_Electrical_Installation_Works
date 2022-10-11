package com.korbiak.service.service.impl;

import com.korbiak.service.config.TreatmentConfig;
import com.korbiak.service.config.WeatherCondTreatConfig;
import com.korbiak.service.config.WeatherCoordinateConfig;
import com.korbiak.service.dto.bing.BingMapPolygon;
import com.korbiak.service.dto.bing.Option;
import com.korbiak.service.model.entities.WeatherConditions;
import com.korbiak.service.repos.WeatherCondRepo;
import com.korbiak.service.service.ElectTreatmentService;
import com.korbiak.service.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElectTreatmentServiceImpl implements ElectTreatmentService {
    private final TreatmentConfig treatmentConfig;
    private final WeatherCondRepo weatherCondRepo;
    private final WeatherCoordinateConfig weatherCoordinateConfig;
    private final WeatherCondTreatConfig weatherCondTreatConfig;

    @Override
    public List<BingMapPolygon> getAllMapPolygons(int jumpTime) {
        List<Timestamp> allDateTimes = weatherCondRepo.getAllDateTimes();
        Timestamp targetTime = ServiceUtils.calculateTargetTime(allDateTimes, jumpTime);
        if (targetTime == null) {
            return new ArrayList<>();
        }
        List<WeatherConditions> responseList = weatherCondRepo.getAllByDateTime(targetTime);
        return convertTo(responseList);
    }

    @Override
    public List<BingMapPolygon> convertTo(List<WeatherConditions> apiResponses) {
        return apiResponses.stream()
                .map(this::convertTo)
                .collect(Collectors.toList());
    }

    @Override
    public BingMapPolygon convertTo(WeatherConditions apiResponse) {
        BingMapPolygon bingMapPolygon = new BingMapPolygon();
        bingMapPolygon.setRadius(treatmentConfig.getRadius());
        bingMapPolygon.setPoints(treatmentConfig.getPoints());
        bingMapPolygon.setCenter(List.of(apiResponse.getCoordinate().getLat(),
                apiResponse.getCoordinate().getLon()));

        //setting treatment level with color
        bingMapPolygon.setOption(getColor(apiResponse.getThreatLevel()));


        return bingMapPolygon;
    }
    public Option getColor(int treatmentLevel) {
        Option option = new Option();
        Map<Integer, String> levelsColor = treatmentConfig.getLevelsColor();
        option.setFillColor(levelsColor.get(treatmentLevel));
        option.setStrokeColor(levelsColor.get(treatmentLevel));
        return option;
    }
}
