package com.devau7.secretsmanager.controller;

import com.devau7.secretsmanager.dto.SmsReminderDTO;
import com.devau7.secretsmanager.service.SmsReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SmsReminderController {

    private final SmsReminderService smsReminderService;

    @GetMapping("/api/sms/{id}")
    public ResponseEntity<SmsReminderDTO> getSmsById(@PathVariable Long id) {
        return ResponseEntity.ok(smsReminderService.getSmsById(id));
    }
}
