package com.korbiak.service.controller;

import com.korbiak.service.dto.bing.BingPushPin;
import com.korbiak.service.service.LightningService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("service-api/weather/lightning/")
@CrossOrigin(origins = "http://localhost:3000")
public class LightningController {
    private final LightningService lightningService;

    @GetMapping("push-pins")
    public List<BingPushPin> getLightningPushPins(@RequestParam int jumpTime) {
        return lightningService.getLightningPushPins(jumpTime);
    }
}
