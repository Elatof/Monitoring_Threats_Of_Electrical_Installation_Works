package com.korbiak.lightningmockservice.controller;

import com.korbiak.lightningmockservice.model.LightningResponse;
import com.korbiak.lightningmockservice.service.LightningService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LightningController {

    private final LightningService lightningService;

    @GetMapping("lightning/hits")
    public List<LightningResponse> findAllHits(@RequestParam double lat,
                                               @RequestParam double lon,
                                               @RequestParam int weatherCode) {
        return lightningService.findLighting(lat, lon, weatherCode);
    }
}
