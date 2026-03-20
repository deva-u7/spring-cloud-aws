package com.devau7.secretsmanager.repository;

import com.devau7.secretsmanager.entity.SmsReminderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsReminderRepository extends JpaRepository<SmsReminderEntity, Long> {
}
