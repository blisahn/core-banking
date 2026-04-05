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
public class CustomerEventConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMqConfig.CUSTOMER_EVENTS_QUEUE)
    public void handleCustomerEvent(String payload) {
        log.info("Received customer event: {}", payload);
        try {
            if (payload.contains("CustomerRegisteredEvent")) {
                notificationService.sendCustomerRegisteredNotification(payload);
            } else if (payload.contains("CustomerStatusChangedEvent")) {
                notificationService.sendCustomerStatusChangedNotification(payload);
            } else {
                log.debug("Unhandled customer event type in payload");
            }
        } catch (Exception e) {
            log.error("Failed to process customer event: {}", e.getMessage(), e);
            throw e;
        }
    }
}
