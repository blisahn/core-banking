package com.devblo.user.repository;

import com.devblo.user.UserId;

import java.util.List;
import java.util.Optional;

public interface IUserReadRepository {
    Optional<UserSummary> findSummaryById(UserId id);

    List<UserSummary> findAll();

    Optional<UserSummary> findUserByEmail(String email);
}
