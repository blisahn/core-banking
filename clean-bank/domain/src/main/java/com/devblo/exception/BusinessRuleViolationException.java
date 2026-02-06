package com.devblo.exception;

import com.devblo.common.exception.DomainException;

public class BusinessRuleViolationException extends DomainException {
    
    public BusinessRuleViolationException(String message) {
        super(message);
    }
}
