package com.devblo.transaction;

import com.devblo.account.AccountId;
import com.devblo.common.BaseAggregateRoot;
import com.devblo.exception.InvalidMoneyException;
import com.devblo.shared.Money;
import com.devblo.transaction.event.TransactionCreatedEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

//Immutable Aggreagte
// Fixed: Added final tags
public class Transaction extends BaseAggregateRoot<TransactionId> {
    private final AccountId sourceAccountId;
    private final AccountId targetAccountId;
    private final Money amount;
    private final TransactionType type;
    private final TransactionStatus status;
    private final String description;
    private final Instant timestamp;

    private Transaction(TransactionId transactionId, AccountId sourceAccountId, AccountId targetAccountId,
                        Money amount, TransactionType type, TransactionStatus status,
                        String description, Instant timestamp) {
        super(transactionId);
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.description = description;
        this.timestamp = timestamp;
    }

    private Transaction(TransactionId id, AccountId sourceAccountId, AccountId targetAccountId,
                        Money amount, TransactionType type, TransactionStatus status,
                        String description, Instant timestamp, Instant createdAt, Instant updatedAt) {
        super(id, createdAt, updatedAt);
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.description = description;
        this.timestamp = timestamp;
    }

    // Reconstruction
    public static Transaction reconstitute(TransactionId id, AccountId sourceAccountId, AccountId targetAccountId,
                                           Money amount, TransactionType type, TransactionStatus status,
                                           String description, Instant timestamp, Instant createdAt, Instant updatedAt) {
        return new Transaction(id, sourceAccountId, targetAccountId, amount, type, status, description, timestamp, createdAt, updatedAt);
    }


    public static Transaction deposit(AccountId targetAccountId, Money amount, String description) {
        Objects.requireNonNull(targetAccountId, "TargetAccountId cannot be null");
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        if (amount.isZero() || amount.amount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMoneyException();
        }
        TransactionId id = TransactionId.generate();
        Transaction transaction = new Transaction(
                id,
                null,
                targetAccountId,
                amount,
                TransactionType.DEPOSIT,
                TransactionStatus.DONE,
                description,
                Instant.now()
        );

        transaction.registerEvent(new TransactionCreatedEvent(id, TransactionType.DEPOSIT, TransactionStatus.DONE));
        return transaction;
    }

    public static Transaction withdraw(AccountId sourceAccountId, Money amount, String description) {
        Objects.requireNonNull(sourceAccountId, "SourceAccountId cannot be null");
        Objects.requireNonNull(amount, "Amount cannot be null");
        TransactionId id = TransactionId.generate();
        Transaction transaction = new Transaction(
                id,
                sourceAccountId,
                null,
                amount,
                TransactionType.WITHDRAWAL,
                TransactionStatus.DONE,
                description,
                Instant.now()
        );
        transaction.registerEvent(new TransactionCreatedEvent(id, TransactionType.WITHDRAWAL, TransactionStatus.DONE));
        return transaction;
    }

    public static Transaction transfer(AccountId sourceAccountId, AccountId targetAccountId, Money amount, String description) {
        Objects.requireNonNull(sourceAccountId, "SourceAccountId cannot be null");
        Objects.requireNonNull(targetAccountId, "TargetAccountId cannot be null");
        Objects.requireNonNull(amount, "Amount cannot be null");
        TransactionId id = TransactionId.generate();
        Transaction transaction = new Transaction(
                id,
                sourceAccountId,
                targetAccountId,
                amount,
                TransactionType.TRANSFER,
                TransactionStatus.DONE,
                description,
                Instant.now()
        );
        transaction.registerEvent(new TransactionCreatedEvent(id, TransactionType.TRANSFER, TransactionStatus.DONE));
        return transaction;
    }


    // Getters
    public AccountId getSourceAccountId() {
        return sourceAccountId;
    }

    public AccountId getTargetAccountId() {
        return targetAccountId;
    }

    public Money getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
