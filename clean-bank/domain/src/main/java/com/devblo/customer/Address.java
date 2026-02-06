package com.devblo.customer;

import com.devblo.common.ValueObject;

import java.util.Objects;

public record Address(
        String street,
        String district
) implements ValueObject {

    public Address {
        Objects.requireNonNull(street, "Street can not be null");
        Objects.requireNonNull(district, "District is can not be null");
        if (street.isEmpty())
            throw new IllegalArgumentException("Street can not be empty");
        if (district.isEmpty())
            throw new IllegalArgumentException("District can not be empty");
    }

    public static Address of(String street, String district) {
        return new Address(street, district);
    }
}
