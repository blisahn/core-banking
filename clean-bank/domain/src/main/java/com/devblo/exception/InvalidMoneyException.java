package com.devblo.exception;

import com.devblo.common.exception.DomainException;

public class InvalidMoneyException extends DomainException {
    public InvalidMoneyException() {
        super("Amount must be greater than zero");
    }
}
