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
public class TransactionEventConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMqConfig.TRANSACTION_EVENTS_QUEUE)
    public void handleTransactionEvent(String payload) {
        log.info("Received transaction event: {}", payload);
        try {
            notificationService.sendTransferNotification(payload);
        } catch (Exception e) {
            log.error("Failed to process transaction event: {}", e.getMessage(), e);
            throw e;
        }
    }
}
