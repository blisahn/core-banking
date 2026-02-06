package com.devblo.exception;

import com.devblo.common.exception.DomainException;
import com.devblo.shared.Money;

public class InsufficientFundsException extends DomainException {
    private Money available;
    private Money required;

    public InsufficientFundsException(Money required, Money available) {
        super(String.format("Insufficient funds. Required: %s, Available: %s", required, available));
        this.required = required;
        this.available = available;
    }

}
