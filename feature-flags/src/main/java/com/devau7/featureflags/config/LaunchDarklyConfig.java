package com.devau7.featureflags.config;

import com.devau7.featureflags.common.FlagProvider;
import com.launchdarkly.sdk.server.LDClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LaunchDarklyConfig {

    @Value("${launch-darkly.key}")
    private String sdkKey;

    @Bean
    public LDClient ldClient() {
        return new LDClient(sdkKey);
    }

    @Bean
    public FlagProvider flagProvider(LDClient ldClient) {
        return new FlagProvider(ldClient);
    }
}
