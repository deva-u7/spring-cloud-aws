package com.devau7.featureflags.common;

import com.launchdarkly.sdk.LDContext;
import com.launchdarkly.sdk.server.LDClient;

/**
 * Evaluates LaunchDarkly feature flags using the SDK 7 LDContext API.
 * LDContext replaces the deprecated LDUser from SDK 5/6.
 */
public class FlagProvider {

    private final LDClient ldClient;

    public FlagProvider(LDClient ldClient) {
        this.ldClient = ldClient;
    }

    public Boolean isFeatureEnable(String key, String country) {
        LDContext context = LDContext.builder("user")
                .set("country", country)
                .build();
        return ldClient.boolVariation(key, context, false);
    }

    public Boolean isFeatureEnable(String key) {
        LDContext context = LDContext.create("user");
        return ldClient.boolVariation(key, context, false);
    }
}
