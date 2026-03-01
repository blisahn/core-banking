package com.devblo.infrastructure.persistence.account;

import com.devblo.account.*;
import com.devblo.account.repository.AccountSummary;
import com.devblo.customer.CustomerId;
import com.devblo.shared.Money;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class AccountEntityMapper {

    public AccountEntity toEntity(Account account) {
        AccountEntity entity = new AccountEntity();
        entity.setId(account.getId().value());
        entity.setAccountNumber(account.getAccountNumber().value());
        entity.setCustomerId(account.getCustomerId().value());
        entity.setBalanceAmount(account.getBalance().amount());
        entity.setBalanceCurrency(account.getBalance().currency().getCurrencyCode());
        entity.setType(account.getType());
        entity.setStatus(account.getStatus());
        entity.setCreatedAt(account.getCreatedAt());
        entity.setUpdatedAt(account.getUpdatedAt());
        return entity;
    }

    public Account toDomain(AccountEntity entity) {
        return Account.reconstitute(
                AccountId.of(entity.getId()),
                AccountNumber.of(entity.getAccountNumber()),
                CustomerId.of(entity.getCustomerId()),
                new Money(entity.getBalanceAmount(), Currency.getInstance(entity.getBalanceCurrency())),
                entity.getType(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public AccountSummary toSummary(AccountEntity entity) {
        return new AccountSummary(
                AccountId.of(entity.getId()),
                entity.getAccountNumber(),
                new Money(entity.getBalanceAmount(), Currency.getInstance(entity.getBalanceCurrency())),
                null, // customerName — requires join, populated separately
                entity.getType(),
                entity.getStatus(),
                entity.getCreatedAt());
    }
}
