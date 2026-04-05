package com.devblo.infrastructure.messaging.consumers;

import com.devblo.infrastructure.messaging.NotificationService;
import com.devblo.infrastructure.messaging.RabbitMqConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountEventConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMqConfig.ACCOUNT_EVENTS_QUEUE)
    public void handleAccountEvent(String payload) {
        log.info("Received account event: {}", payload);
        try {
            if (payload.contains("AccountOpenedEvent")) {
                notificationService.sendAccountOpenedNotification(payload);
            } else if (payload.contains("MoneyDepositedEvent")) {
                notificationService.sendDepositNotification(payload);
            } else if (payload.contains("MoneyWithdrawEvent")) {
                notificationService.sendWithdrawalNotification(payload);
            } else {
                log.debug("Unhandled account event type in payload");
            }
        } catch (Exception e) {
            log.error("Failed to process account event: {}", e.getMessage(), e);
            throw e;
        }
    }
}
