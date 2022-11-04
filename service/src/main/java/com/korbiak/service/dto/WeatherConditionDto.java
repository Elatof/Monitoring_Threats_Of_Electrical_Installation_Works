package com.korbiak.service.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class WeatherConditionDto {
    private int id;
    private double weatherId;
    private String main;
    private String description;
    private String icon;
    private double windSpeed;
    private Timestamp dateTime;
    private int threatLevel;
    private CoordinateDto coordinate;
}
