package com.devblo.customer;

import com.devblo.common.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record CustomerId(UUID value) implements ValueObject {
    public CustomerId {
        Objects.requireNonNull(value, "CustomerId cannot be null");
    }
    public static CustomerId generate() {
        return new CustomerId(UUID.randomUUID());
    }
    public static CustomerId of(UUID value) {
        return new CustomerId(value);
    }
    public static CustomerId of(String value) {
        return new CustomerId(UUID.fromString(value));
    }
    @Override
    public String toString() {
        return value.toString();
    }
}
