package com.korbiak.service.dto.bing;

import lombok.Data;

import java.util.List;

@Data
public class BingPushPin {
    private List<String> location;
    private PinOption option;
}
