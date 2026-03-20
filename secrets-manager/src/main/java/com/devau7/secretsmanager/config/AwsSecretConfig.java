package com.devau7.secretsmanager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.util.Map;

/**
 * Loads secrets from AWS Secrets Manager and injects them into the Spring
 * environment before the application context is created. This means the secret
 * values are available as regular Spring properties (e.g. datasource.password).
 *
 * Registered via META-INF/spring.factories.
 */
public class AwsSecretConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication application) {
        String secretName = env.getProperty("aws.secret");
        String region = env.getProperty("aws.region", "eu-west-1");

        if (secretName == null || secretName.isBlank()) {
            return;
        }

        try (SecretsManagerClient client = SecretsManagerClient.builder()
                .region(Region.of(region))
                .build()) {

            GetSecretValueResponse response = client.getSecretValue(
                    GetSecretValueRequest.builder().secretId(secretName).build());

            Map<String, Object> values = new ObjectMapper()
                    .readValue(response.secretString(), Map.class);

            env.getPropertySources().addFirst(
                    new MapPropertySource("aws-secrets-" + secretName, values));

        } catch (Exception e) {
            throw new RuntimeException("Error fetching secrets from AWS Secrets Manager: " + e.getMessage(), e);
        }
    }
}
