package com.korbiak.service.controller;

import com.korbiak.service.config.TreatmentConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("service-api/meta/")
@CrossOrigin(origins = "http://localhost:3000")
public class MetaInfoController {

    private final TreatmentConfig treatmentConfig;

    @GetMapping("health-check")
    public String healthCheck() {
        return "Health";
    }

    @GetMapping("treatment-config")
    public Map<Integer, String> getTreatmentConfig() {
        return treatmentConfig.getLevelsColor();
    }
}
