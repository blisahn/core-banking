package com.devblo.exception;

import com.devblo.account.AccountStatus;
import com.devblo.common.exception.DomainException;

public class AccountNotActiveException extends DomainException {
    private final AccountStatus status;

    public AccountNotActiveException(AccountStatus status) {
        super(String.format("Account is not active. Current status is %s", status));
        this.status = status;
    }

    public AccountStatus getStatus() {
        return status;
    }
}
