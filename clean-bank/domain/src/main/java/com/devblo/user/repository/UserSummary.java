package com.devblo.user.repository;

import com.devblo.customer.CustomerId;
import com.devblo.user.Role;
import com.devblo.user.UserId;

public record UserSummary(
        UserId id,
        String email,
        Role role,
        CustomerId customerId
) {
}
