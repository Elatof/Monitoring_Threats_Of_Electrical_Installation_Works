package com.korbiak.service.model.bing;

import lombok.Data;

import java.util.List;

@Data
public class BingMapPolygon {
    private List<Double> center;
    private int radius;
    private int points;
    private Option option;
}
