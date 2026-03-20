package com.devau7.sqs.service;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final SqsTemplate sqsTemplate;

    @Value("${sqs.queue-name:DemoQueue}")
    private String queueName;

    public String sendMessage(String message) {
        sqsTemplate.send(queueName, message);
        return "Message sent to queue: " + queueName;
    }

    @SqsListener("${sqs.queue-name:DemoQueue}")
    public void receiveMessage(String message) {
        log.info("Message received from SQS: {}", message);
    }
}
