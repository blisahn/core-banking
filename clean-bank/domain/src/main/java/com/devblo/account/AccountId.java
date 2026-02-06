package com.devblo.account;

import com.devblo.common.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record AccountId(UUID value) implements ValueObject {
    public AccountId {
        Objects.requireNonNull(value, "AccountId cannot be null");
    }

    public static AccountId generate() {
        return new AccountId(UUID.randomUUID());
    }

    public static AccountId of(UUID value) {
        return new AccountId(value);
    }

    public static AccountId of(String value) {
        return new AccountId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
