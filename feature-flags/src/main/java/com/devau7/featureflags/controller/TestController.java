package com.devau7.featureflags.controller;

import com.devau7.featureflags.common.FlagProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final FlagProvider flagProvider;

    private static final String COUNTRY_FLAG = "test-flag";
    private static final String TEST_FLAG = "test";

    @GetMapping("/api/test")
    public Boolean testFlagWithCountry(@RequestParam("country") String country) {
        return flagProvider.isFeatureEnable(COUNTRY_FLAG, country);
    }

    @GetMapping("/api/test1")
    public Boolean testFlag() {
        return flagProvider.isFeatureEnable(TEST_FLAG);
    }
}
