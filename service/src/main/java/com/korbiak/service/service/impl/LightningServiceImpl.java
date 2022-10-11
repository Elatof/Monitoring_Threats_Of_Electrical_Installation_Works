package com.korbiak.service.service.impl;

import com.korbiak.service.config.LightningApiConfig;
import com.korbiak.service.dto.bing.BingPushPin;
import com.korbiak.service.dto.bing.PinOption;
import com.korbiak.service.model.entities.LightningCoordinate;
import com.korbiak.service.repos.LightningCoordinateRepo;
import com.korbiak.service.repos.WeatherCondRepo;
import com.korbiak.service.service.LightningService;
import com.korbiak.service.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LightningServiceImpl implements LightningService {

    private final LightningApiConfig lightningApiConfig;

    private final LightningCoordinateRepo lightningCoordinateRepo;

    private final WeatherCondRepo weatherRepo;

    @Override
    public List<BingPushPin> getLightningPushPins(int jumpTime) {
        List<Timestamp> allDateTimes = weatherRepo.getAllDateTimes();
        Timestamp targetTimestamp = ServiceUtils.calculateTargetTime(allDateTimes, jumpTime);
        List<LightningCoordinate> allByDateTime = lightningCoordinateRepo.findAllByDateTime(targetTimestamp);

        if (jumpTime > 0) {
            return new ArrayList<>();
        }
        return allByDateTime.stream().map(lightningCoordinate -> {
            BingPushPin bingPushPin = new BingPushPin();
            bingPushPin.setLocation(List.of(String.valueOf(lightningCoordinate.getLat()),
                    String.valueOf(lightningCoordinate.getLon())));
            PinOption pinOption = new PinOption();
            pinOption.setIcon(lightningApiConfig.getIconImage());
            bingPushPin.setOption(pinOption);
            return bingPushPin;
        }).collect(Collectors.toList());
    }
}
