package com.devblo.infrastructure.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void sendAccountOpenedNotification(String eventPayload) {
        log.info("[NOTIFICATION] Account opened - payload: {}", eventPayload);
        // TODO: integrate with email/SMS provider
    }

    public void sendDepositNotification(String eventPayload) {
        log.info("[NOTIFICATION] Money deposited - payload: {}", eventPayload);
    }

    public void sendWithdrawalNotification(String eventPayload) {
        log.info("[NOTIFICATION] Money withdrawn - payload: {}", eventPayload);
    }

    public void sendTransferNotification(String eventPayload) {
        log.info("[NOTIFICATION] Transfer completed - payload: {}", eventPayload);
    }

    public void sendCustomerRegisteredNotification(String eventPayload) {
        log.info("[NOTIFICATION] Customer registered - payload: {}", eventPayload);
    }

    public void sendCustomerStatusChangedNotification(String eventPayload) {
        log.info("[NOTIFICATION] Customer status changed - payload: {}", eventPayload);
    }
}
