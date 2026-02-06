package com.devblo.account;

import com.devblo.common.ValueObject;

import java.util.Objects;
import java.util.regex.Pattern;

//IBAN
public record AccountNumber(String value) implements ValueObject {

    private static final Pattern IBAN_PATTERN = Pattern.compile("^TR\\d{24}$");

    public AccountNumber {
        Objects.requireNonNull(value, "AccountNumber cannot be null");
        String normalized = value.replaceAll("\\s", "").toUpperCase();
        if (!IBAN_PATTERN.matcher(normalized).matches())
            throw new IllegalArgumentException("Invalid IBAN format: " + normalized);
        value = normalized;
    }

    public static AccountNumber of(String value) {
        return new AccountNumber(value);
    }

    public String getBankCode() {
        return value.substring(4, 9);
    }

    @Override
    public String toString() {
        return value;
    }
}
