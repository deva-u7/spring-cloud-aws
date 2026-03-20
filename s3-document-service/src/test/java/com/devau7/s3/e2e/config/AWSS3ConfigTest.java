package com.devau7.s3.e2e.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
@Profile("test")
public class AWSS3ConfigTest {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public static LocalStackContainer localStackContainer() {
        return new LocalStackContainer(
                DockerImageName.parse("localstack/localstack:latest"))
                .withServices(LocalStackContainer.Service.S3);
    }

    @Bean("S3Client")
    public S3Client s3Client(LocalStackContainer localStackContainer) {
        URI endpoint = localStackContainer.getEndpointOverride(LocalStackContainer.Service.S3);
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                localStackContainer.getAccessKey(),
                localStackContainer.getSecretKey());
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(localStackContainer.getRegion()))
                .endpointOverride(endpoint)
                .build();
    }
}
