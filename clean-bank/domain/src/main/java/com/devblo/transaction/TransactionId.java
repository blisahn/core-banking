package com.devblo.transaction;

import com.devblo.common.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record TransactionId(UUID value) implements ValueObject {

    public TransactionId {
        Objects.requireNonNull(value, "TransactionId cannot be null");
    }

    public static TransactionId generate() {
        return new TransactionId(UUID.randomUUID());
    }

    public static TransactionId of(UUID value) {
        return new TransactionId(value);
    }

    public static TransactionId of(String value) {
        return new TransactionId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
