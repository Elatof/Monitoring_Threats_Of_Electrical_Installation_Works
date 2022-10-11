package com.korbiak.lightningmockservice.service;

import com.korbiak.lightningmockservice.model.LightningResponse;

import java.util.List;

public interface LightningService {

    List<LightningResponse> findLighting(double lat, double lon, int weatherCode);
}
