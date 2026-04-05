package com.devblo.user;

import com.devblo.common.BaseAggregateRoot;
import com.devblo.customer.CustomerId;
import com.devblo.shared.Email;

import java.time.Instant;
import java.util.Objects;

public class User extends BaseAggregateRoot<UserId> {

    private Email email;
    private String password;
    private Role role;
    private CustomerId customerId;

    private User(UserId userId, Email email, String password, Role role, CustomerId customerId) {
        super(userId);
        this.email = email;
        this.password = password;
        this.role = role;
        this.customerId = customerId;
    }

    private User(UserId userId, Instant createdAt, Instant updatedAt,
                 Email email, String password, Role role, CustomerId customerId) {
        super(userId, createdAt, updatedAt);
        this.email = email;
        this.password = password;
        this.role = role;
        this.customerId = customerId;
    }

    public static User create(Email email, String encodedPassword, Role role, CustomerId customerId) {
        Objects.requireNonNull(email, "Email must not be null");
        Objects.requireNonNull(encodedPassword, "Password must not be null");
        Objects.requireNonNull(role, "Role must not be null");

        return new User(UserId.generate(), email, encodedPassword, role, customerId);
    }

    public static User reconstitute(UserId userId, Instant createdAt, Instant updatedAt,
                                    Email email, String password, Role role, CustomerId customerId) {
        return new User(userId, createdAt, updatedAt, email, password, role, customerId);
    }

    public void linkToCustomer(CustomerId customerId) {
        this.customerId = customerId;
        markUpdated();
    }

    public Email getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }
}
