package com.devblo.exception;

import com.devblo.common.exception.DomainException;
import com.devblo.shared.Money;

public class AccountBalanceNotZeroException extends DomainException {
    
    public AccountBalanceNotZeroException(Money balance) {
        super(String.format("Cannot close account with non-zero balance: %s", balance));
    }
}
