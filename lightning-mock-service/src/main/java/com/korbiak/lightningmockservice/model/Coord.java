package com.korbiak.lightningmockservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Coord {
    private double lon;
    private double lat;
}
