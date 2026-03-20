package com.devau7.secretsmanager.service;

import com.devau7.secretsmanager.dto.SmsReminderDTO;
import com.devau7.secretsmanager.entity.SmsReminderEntity;
import com.devau7.secretsmanager.repository.SmsReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsReminderService {

    private final SmsReminderRepository smsReminderRepository;

    public SmsReminderDTO getSmsById(long id) {
        SmsReminderEntity entity = smsReminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SMS Reminder not found for id: " + id));
        return mapToDTO(entity);
    }

    private SmsReminderDTO mapToDTO(SmsReminderEntity entity) {
        return SmsReminderDTO.builder()
                .id(entity.getId())
                .accountNumber(entity.getAccountNumber())
                .customerNumber(entity.getCustomerNumber())
                .message(entity.getMessage())
                .msisdn(entity.getMsisdn())
                .messageId(entity.getMessageId())
                .isWeekly(entity.getIsWeekly())
                .frequency(entity.getFrequency())
                .createdBy(entity.getCreatedBy())
                .lastModifiedBy(entity.getLastModifiedBy())
                .createdDate(entity.getCreatedDate())
                .lastModifiedDate(entity.getLastModifiedDate())
                .build();
    }
}
