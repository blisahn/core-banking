package com.devblo.customer;

import com.devblo.common.ValueObject;
import com.devblo.shared.Email;

import java.time.LocalDate;
import java.util.Objects;

public record PersonalInfo(String firstName, String lastName, Email email,
                           LocalDate dateOfBirth, String password) implements ValueObject {

    public PersonalInfo {
        Objects.requireNonNull(firstName, "First name cannot be null");
        Objects.requireNonNull(lastName, "Last name cannot be null");
        Objects.requireNonNull(email, "Email cannot be null");
        Objects.requireNonNull(dateOfBirth, "Date of birth cannot be null");
        Objects.requireNonNull(password, "Password cannot be null");
        if (firstName.isBlank()) throw new IllegalArgumentException("First name is required");
        if (lastName.isBlank()) throw new IllegalArgumentException("Last name is required");
        if (password.isBlank()) throw new IllegalArgumentException("Password is required");
    }

    public static PersonalInfo of(String firstName, String lastName, Email email,
                                  LocalDate dateOfBirth, String password) {
        return new PersonalInfo(firstName, lastName, email, dateOfBirth, password);
    }

    public static PersonalInfo of(String firstName, String lastName, String email,
                                  LocalDate dateOfBirth, String password) {
        return new PersonalInfo(firstName, lastName, Email.of(email), dateOfBirth, password);
    }

    public static PersonalInfo of(String firstName, String lastName, String email,
                                  String dateOfBirth, String password) {
        return new PersonalInfo(firstName, lastName, Email.of(email), LocalDate.parse(dateOfBirth), password);
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " " + this.dateOfBirth.toString();
    }
}
