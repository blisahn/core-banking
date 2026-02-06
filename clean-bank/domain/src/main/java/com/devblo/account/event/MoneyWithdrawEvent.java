package com.devblo.account.event;

import com.devblo.account.AccountId;
import com.devblo.common.event.BaseDomainEvent;
import com.devblo.shared.Money;

import java.time.Instant;
import java.util.UUID;

public class MoneyWithdrawEvent extends BaseDomainEvent {
    private final AccountId accountId;
    private final Money amount;
    private final Money newBalance;

    public MoneyWithdrawEvent(AccountId accountId, Money amount, Money newBalance) {
        super(UUID.randomUUID(), Instant.now());
        this.accountId = accountId;
        this.amount = amount;
        this.newBalance = newBalance;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public Money getAmount() {
        return amount;
    }

    public Money getNewBalance() {
        return newBalance;
    }
}
