package com.devblo.account;

import com.devblo.account.event.AccountOpenedEvent;
import com.devblo.account.event.MoneyDepositedEvent;
import com.devblo.account.event.MoneyWithdrawEvent;
import com.devblo.common.BaseAggregateRoot;
import com.devblo.customer.CustomerId;
import com.devblo.exception.*;
import com.devblo.shared.Money;

import java.time.Instant;
import java.util.Currency;
import java.util.Objects;

public class Account extends BaseAggregateRoot<AccountId> {

    private AccountNumber accountNumber;
    private CustomerId customerId;
    private Money balance;
    private AccountType type;
    private AccountStatus status;

    private Account(AccountId accountId, AccountNumber accountNumber,
                    CustomerId customerId, Money balance,
                    AccountType accountType, AccountStatus accountStatus) {
        super(accountId);
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = balance;
        this.type = accountType;
        this.status = accountStatus;
    }

    private Account(AccountId id, AccountNumber accountNumber, CustomerId customerId,
                    Money balance, AccountType type, AccountStatus status,
                    Instant createdAt, Instant updatedAt) {
        super(id, createdAt, updatedAt);
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = balance;
        this.type = type;
        this.status = status;
    }

    /**
     *
     * @param accountNumber
     * @param customerId
     * @param type
     * @param currency
     * @return new account
     * @implNote Only way to access account
     */
    public static Account open(AccountNumber accountNumber, CustomerId customerId,
                               AccountType type, Currency currency) {
        Objects.requireNonNull(accountNumber, "AccountNumber must not be null");
        Objects.requireNonNull(customerId, "CustomerId must not be null");
        Objects.requireNonNull(type, "AccountType must not be null");
        Objects.requireNonNull(currency, "Currency must not be null");

        AccountId id = AccountId.generate();
        Money initialBalance = Money.zero(currency);
        Account account = new Account(
                id,
                accountNumber,
                customerId,
                initialBalance,
                type,
                AccountStatus.ACTIVE
        );

        account.registerEvent(new AccountOpenedEvent(id, type, customerId, currency));

        return account;
    }

    /***
     *
     * @param id
     * @param accountnumber
     * @param customerId
     * @param balance
     * @param type
     * @param status
     * @param createdAt
     * @param updatedAt
     * @return a new account for retrieving from db
     */
    public static Account reconstitute(AccountId id, AccountNumber accountnumber,
                                       CustomerId customerId, Money balance,
                                       AccountType type, AccountStatus status,
                                       Instant createdAt, Instant updatedAt) {
        return new Account(id, accountnumber, customerId, balance, type, status, createdAt, updatedAt);
    }

    // BUSINESS METHODS
    public void deposit(Money amount) {
        validateActive();
        validatePositiveAmount(amount);
        this.balance = this.balance.add(amount);
        markUpdated();
        registerEvent(new MoneyDepositedEvent(getId(), amount, this.balance));
    }

    public void withdraw(Money amount) {
        validateActive();
        validatePositiveAmount(amount);
        validateSufficientBalance(amount);
        this.balance = this.balance.subtract(amount);
        markUpdated();
        registerEvent(new MoneyWithdrawEvent(getId(), amount, this.balance));
    }

    public void freeze() {
        if (this.status == AccountStatus.CLOSED) {
            throw new BusinessRuleViolationException("Cannot freeze a closed account");
        }
        if (this.status == AccountStatus.FROZEN) {
            return;
        }
        this.status = AccountStatus.FROZEN;
        markUpdated();
    }

    public void activate() {
        if (this.status == AccountStatus.CLOSED) {
            throw new BusinessRuleViolationException("Cannot activate an account that has been closed");
        }
        if (this.status == AccountStatus.ACTIVE) {
            return;
        }
        this.status = AccountStatus.ACTIVE;
        markUpdated();
    }


    public void close() {
        if (!this.balance.isZero()) {
            throw new AccountBalanceNotZeroException(this.balance);
        }
        if (this.status == AccountStatus.CLOSED) {
            return;
        }
        this.status = AccountStatus.CLOSED;
        markUpdated();
    }

    // VALIDATORS
    private void validatePositiveAmount(Money amount) {
        if (amount.isZero())
            throw new InvalidMoneyException();
    }

    private void validateActive() {
        if (this.status != AccountStatus.ACTIVE)
            throw new AccountNotActiveException(this.status);
    }

    private void validateSufficientBalance(Money amount) {
        if (this.balance.isLessThan(amount)) {
            throw new InsufficientFundsException(amount, this.balance);
        }
    }


    public AccountNumber getAccountNumber() {
        return accountNumber;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getBalance() {
        return balance;
    }

    public AccountType getType() {
        return type;
    }

    public AccountStatus getStatus() {
        return status;
    }
}
