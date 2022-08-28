package com.korbiak.service.model.weathermodels;

import lombok.Data;

@Data
public class Wind {
    private double speed;
    private double deg;
    private double gust;
}
