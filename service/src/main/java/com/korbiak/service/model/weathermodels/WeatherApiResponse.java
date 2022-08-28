package com.korbiak.service.model.weathermodels;

import lombok.Data;

import java.util.List;

@Data
public class WeatherApiResponse {
    private Coord coord;
    private List<Weather> weather;
    private String base;
    private Main main;
    private int visibility;
    private Wind wind;
    private Clouds clouds;
    private int dt;
    private Sys sys;
    private int timezone;
    private int id;
    private String name;
    private int cod;
    private int treatmentLevel;
    private String treatmentColor;
}
