package com.devblo.infrastructure.persistence.user;

import com.devblo.customer.CustomerId;
import com.devblo.shared.Email;
import com.devblo.user.User;
import com.devblo.user.UserId;
import com.devblo.user.repository.UserSummary;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

    public UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId().value());
        entity.setEmail(user.getEmail().value());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole());
        entity.setCustomerId(user.getCustomerId() != null ? user.getCustomerId().value() : null);
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        return entity;
    }

    public User toDomain(UserEntity entity) {
        return User.reconstitute(
                UserId.of(entity.getId()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                Email.of(entity.getEmail()),
                entity.getPassword(),
                entity.getRole(),
                entity.getCustomerId() != null ? CustomerId.of(entity.getCustomerId()) : null
        );
    }

    public UserSummary toSummary(UserEntity entity) {
        return new UserSummary(
                UserId.of(entity.getId()),
                entity.getEmail(),
                entity.getRole(),
                entity.getCustomerId() != null ? CustomerId.of(entity.getCustomerId()) : null
        );
    }
}
