package com.devblo.user.repository;

import com.devblo.user.User;
import com.devblo.user.UserId;

import java.util.Optional;

public interface IUserWriteRepository {
    User save(User user);
    Optional<User> findById(UserId id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
