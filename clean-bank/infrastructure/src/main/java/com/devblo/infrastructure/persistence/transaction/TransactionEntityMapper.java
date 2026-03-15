package com.devblo.infrastructure.persistence.transaction;

import com.devblo.account.AccountId;
import com.devblo.shared.Money;
import com.devblo.transaction.Transaction;
import com.devblo.transaction.TransactionId;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class TransactionEntityMapper {

    public TransactionEntity toEntity(Transaction transaction) {
        TransactionEntity entity = new TransactionEntity();
        entity.setId(transaction.getId().value());
        entity.setSourceAccountId(
                transaction.getSourceAccountId() != null ? transaction.getSourceAccountId().value() : null);
        entity.setTargetAccountId(
                transaction.getTargetAccountId() != null ? transaction.getTargetAccountId().value() : null);
        entity.setAmount(transaction.getAmount().amount());
        entity.setCurrency(transaction.getAmount().currency().getCurrencyCode());
        entity.setType(transaction.getType());
        entity.setStatus(transaction.getStatus());
        entity.setDescription(transaction.getDescription());
        entity.setTimestamp(transaction.getTimestamp());
        entity.setCreatedAt(transaction.getCreatedAt());
        entity.setUpdatedAt(transaction.getUpdatedAt());
        return entity;
    }

    public Transaction toDomain(TransactionEntity entity) {
        AccountId sourceAccountId = entity.getSourceAccountId() != null
                ? AccountId.of(entity.getSourceAccountId()) : null;
        AccountId targetAccountId = entity.getTargetAccountId() != null
                ? AccountId.of(entity.getTargetAccountId()) : null;
        Money amount = new Money(entity.getAmount(), Currency.getInstance(entity.getCurrency()));
        return Transaction.reconstitute(
                TransactionId.of(entity.getId()),
                sourceAccountId,
                targetAccountId,
                amount,
                entity.getType(),
                entity.getStatus(),
                entity.getDescription(),
                entity.getTimestamp(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
