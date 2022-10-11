package com.korbiak.service.clients;

import com.korbiak.service.model.lightningmodels.LightningResponse;

import java.util.HashMap;
import java.util.List;

public interface LightningApiHttpClient {
    List<HashMap<String, HashMap>> call(String lat, String lon, String weatherId);
}
