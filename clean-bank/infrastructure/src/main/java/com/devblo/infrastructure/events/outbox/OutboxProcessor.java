package com.devblo.infrastructure.events.outbox;

import com.devblo.infrastructure.messaging.RabbitMqConfig;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxProcessor {
    private final OutboxJpaRepository outboxJpaRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void process(){
        List<OutboxEvent> pending = outboxJpaRepository.findByProcessedFalseOrderByCreatedAtAsc();
        for (OutboxEvent event : pending) {
            try{
                String routingKey = resolveRoutingKey(event.getAggregateType(), event.getEventType());
                rabbitTemplate.convertAndSend(RabbitMqConfig.BANKING_EXCHANGE, routingKey, event.getPayload());
                event.markProcessed();
                outboxJpaRepository.save(event);
                log.info("Outbox event published: type={}, id={}", event.getEventType(), event.getId());
            }catch (Exception e){
                log.error("Failed to publish outbox event id={}: {}",event.getId(), e.getMessage());
            }
        }
    }

    private String resolveRoutingKey(String aggregateType, String eventType){
        return switch (aggregateType.toLowerCase()){
            case "account" -> "account." + eventType;
            case "customer" -> "customer." + eventType;
            case "transaction" -> "transaction." + eventType;
            default -> aggregateType + "."+eventType;
        };
    }
}
