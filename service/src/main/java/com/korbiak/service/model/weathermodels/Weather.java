package com.korbiak.service.model.weathermodels;

import lombok.Data;

@Data
public class Weather {
    private double id;
    private String main;
    private String description;
    private String icon;
    private int criticalLevel;
}
