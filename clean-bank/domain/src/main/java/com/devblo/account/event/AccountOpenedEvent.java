package com.devblo.account.event;

import com.devblo.account.AccountId;
import com.devblo.account.AccountType;
import com.devblo.common.event.BaseDomainEvent;
import com.devblo.customer.CustomerId;

import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

public class AccountOpenedEvent extends BaseDomainEvent {

    private final AccountId accountId;
    private final AccountType accountType;
    private final CustomerId customerId;
    private final Currency currency;

    public AccountOpenedEvent(AccountId accountId, AccountType accountType, CustomerId customerId, Currency currency) {
        super(UUID.randomUUID(), Instant.now());
        this.accountId = accountId;
        this.accountType = accountType;
        this.customerId = customerId;
        this.currency = currency;
    }

    public AccountId getAccountId() {
        return accountId;
    }
    public AccountType getAccountType() {
        return accountType;
    }
    public CustomerId getCustomerId() {
        return customerId;
    }
    public Currency getCurrency() {
        return currency;
    }
}
