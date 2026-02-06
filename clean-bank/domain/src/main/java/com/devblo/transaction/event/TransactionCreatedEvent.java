package com.devblo.transaction.event;

import com.devblo.common.event.BaseDomainEvent;
import com.devblo.transaction.TransactionId;
import com.devblo.transaction.TransactionStatus;
import com.devblo.transaction.TransactionType;

import java.time.Instant;
import java.util.UUID;

public class TransactionCreatedEvent extends BaseDomainEvent {
    private final TransactionId transactionId;
    private final TransactionType transactionType;
    private final TransactionStatus transactionStatus;

    public TransactionCreatedEvent(TransactionId transactionId, TransactionType transactionType, TransactionStatus transactionStatus) {
        super(UUID.randomUUID(), Instant.now());
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
    }

    public TransactionId getTransactionId() {
        return transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }
}
