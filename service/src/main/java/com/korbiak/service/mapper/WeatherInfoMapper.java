package com.korbiak.service.mapper;

import com.korbiak.service.dto.WeatherConditionDto;
import com.korbiak.service.model.entities.WeatherConditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeatherInfoMapper implements Mapper<WeatherConditionDto, WeatherConditions> {
    private final CoordinateMapper coordinateMapper;

    @Override
    public WeatherConditionDto toDto(WeatherConditions input) {
        return WeatherConditionDto.builder()
                .id(input.getId())
                .weatherId(input.getWeatherId())
                .dateTime(input.getDateTime())
                .icon(input.getIcon())
                .description(input.getDescription())
                .threatLevel(input.getThreatLevel())
                .windSpeed(input.getWindSpeed())
                .coordinate(coordinateMapper.toDto(input.getCoordinate()))
                .build();
    }

    @Override
    public WeatherConditions toEntity(WeatherConditionDto input) {
        //no need to implement
        return new WeatherConditions();
    }
}
