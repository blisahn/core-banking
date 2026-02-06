package com.devblo.exception;

import com.devblo.common.exception.DomainException;

public class InvalidTransactionException extends DomainException {
    
    public InvalidTransactionException(String message) {
        super(message);
    }
}
