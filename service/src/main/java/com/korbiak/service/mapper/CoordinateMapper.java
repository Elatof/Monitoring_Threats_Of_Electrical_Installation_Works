package com.korbiak.service.mapper;

import com.korbiak.service.dto.CoordinateDto;
import com.korbiak.service.model.entities.Coordinate;
import org.springframework.stereotype.Component;

@Component
public class CoordinateMapper implements Mapper<CoordinateDto, Coordinate> {
    @Override
    public CoordinateDto toDto(Coordinate input) {
        return CoordinateDto.builder()
                .lat(input.getLat())
                .lon(input.getLon())
                .build();
    }

    @Override
    public Coordinate toEntity(CoordinateDto input) {
        return new Coordinate();
    }
}
