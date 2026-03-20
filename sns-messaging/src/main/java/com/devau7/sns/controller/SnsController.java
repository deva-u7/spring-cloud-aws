package com.devau7.sns.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

@RestController
@RequiredArgsConstructor
public class SnsController {

    private final SnsClient snsClient;

    @Value("${sns.topic-arn}")
    private String topicArn;

    @PostMapping("/subscribe/{email}")
    public String subscribe(@PathVariable String email) {
        snsClient.subscribe(SubscribeRequest.builder()
                .topicArn(topicArn)
                .protocol("email")
                .endpoint(email)
                .build());
        return "Subscription confirmation sent to: " + email;
    }

    @PostMapping("/publish")
    public String publish(@RequestParam String message) {
        snsClient.publish(PublishRequest.builder()
                .topicArn(topicArn)
                .message(message)
                .subject("Notification")
                .build());
        return "Message published to topic";
    }
}
