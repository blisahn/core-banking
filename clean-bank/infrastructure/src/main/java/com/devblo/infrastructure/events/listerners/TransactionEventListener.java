package com.devblo.infrastructure.events.listerners;

import com.devblo.infrastructure.events.outbox.OutboxEventService;
import com.devblo.transaction.event.TransactionCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TransactionEventListener {

    private static final Logger log = LoggerFactory.getLogger(TransactionEventListener.class);
    private final OutboxEventService outboxEventService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onTransactionCreated(TransactionCreatedEvent event) {
        log.info("Transaction created: id={}, transactionType={}, transactionStatus={}",
                event.getTransactionId(), event.getTransactionType(), event.getTransactionStatus());
        outboxEventService.saveEvent(event, "transaction", event.getTransactionId().value());
    }
}
