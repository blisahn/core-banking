package com.devblo.account;

import com.devblo.account.event.AccountOpenedEvent;
import com.devblo.account.event.MoneyDepositedEvent;
import com.devblo.account.event.MoneyWithdrawEvent;
import com.devblo.common.BaseAggregateRoot;
import com.devblo.common.result.Result;
import com.devblo.customer.CustomerId;
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

    // BUSINESS METHODS
    public Result<Void> deposit(Money amount) {
        Result<Void> activeValidationResult = validateActive();
        if (activeValidationResult.isFailure()) {
            return activeValidationResult;
        }
        Result<Void> amountValidation = validatePositiveAmount(amount);
        if (amountValidation.isFailure()) {
            return amountValidation;
        }
        this.balance = this.balance.add(amount);
        markUpdated();
        registerEvent(new MoneyDepositedEvent(getId(), amount, this.balance));
        return Result.success();
    }

    public Result<Void> withdraw(Money amount) {
        Result<Void> activeValidationResult = validateActive();
        if (activeValidationResult.isFailure()) {
            return activeValidationResult;
        }
        Result<Void> amountValidation = validatePositiveAmount(amount);
        if (amountValidation.isFailure()) {
            return amountValidation;
        }
        Result<Void> sufficientBalanceValidation = validateSufficientBalance(amount);
        if (sufficientBalanceValidation.isFailure()) {
            return sufficientBalanceValidation;
        }
        this.balance = this.balance.subtract(amount);
        markUpdated();
        registerEvent(new MoneyWithdrawEvent(getId(), amount, this.balance));
        return Result.success();
    }

    public Result<Void> freeze() {
        if (this.status == AccountStatus.CLOSED) {
            return Result.failure("Can not freeze an account that is closed.");
        }
        if (this.status == AccountStatus.FROZEN) {
            return Result.success();
        }
        this.status = AccountStatus.FROZEN;
        markUpdated();
        return Result.success();
    }

    public Result<Void> activate() {
        if (this.status == AccountStatus.CLOSED) {
            return Result.failure("Can not activate an account that is closed.");
        }
        if (this.status == AccountStatus.ACTIVE) {
            return Result.success();
        }
        this.status = AccountStatus.ACTIVE;
        markUpdated();
        return Result.success();
    }


    public Result<Void> close() {
        if (!this.balance.isZero()) {
            return Result.failure("Can not close an account with the money");
        }
        if (this.status == AccountStatus.CLOSED) {
            return Result.success();
        }
        this.status = AccountStatus.CLOSED;
        markUpdated();
        return Result.success();
    }

    // VALIDATORS
    private Result<Void> validatePositiveAmount(Money amount) {
        if (amount.isZero())
            return Result.failure("Amount must not be zero");
        return Result.success();
    }


    private Result<Void> validateActive() {
        if (this.status != AccountStatus.ACTIVE)
            return Result.failure("Account is not active");
        return Result.success();
    }

    private Result<Void> validateSufficientBalance(Money amount) {
        if (this.balance.isLessThan(amount)) {
            return Result.failure("Amount must be greater than balance");
        }
        return Result.success();
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
