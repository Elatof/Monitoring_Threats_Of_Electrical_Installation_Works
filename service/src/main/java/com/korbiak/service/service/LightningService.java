package com.korbiak.service.service;

import com.korbiak.service.dto.bing.BingPushPin;

import java.util.List;

public interface LightningService {

    List<BingPushPin> getLightningPushPins(int jumpTime);
}
