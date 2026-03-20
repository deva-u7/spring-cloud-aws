package com.devau7.sqs.controller;

import com.devau7.sqs.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/api/send/{message}")
    public ResponseEntity<String> sendMessage(@PathVariable String message) {
        return ResponseEntity.ok(messageService.sendMessage(message));
    }
}
