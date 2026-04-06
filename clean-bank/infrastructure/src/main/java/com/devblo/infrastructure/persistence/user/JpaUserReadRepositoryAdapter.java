package com.devblo.infrastructure.persistence.user;

import com.devblo.user.UserId;
import com.devblo.user.repository.IUserReadRepository;
import com.devblo.user.repository.UserSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaUserReadRepositoryAdapter implements IUserReadRepository {

    private final UserJpaRepository jpaRepo;
    private final UserEntityMapper mapper;

    @Override
    public Optional<UserSummary> findSummaryById(UserId id) {
        return jpaRepo.findById(id.value()).map(mapper::toSummary);
    }

    @Override
    public List<UserSummary> findAll() {
        return jpaRepo.findAll().stream().map(mapper::toSummary).toList();
    }

    @Override
    public Optional<UserSummary> findUserByEmail(String email) {
        return jpaRepo.findByEmail(email).map(mapper::toSummary);
    }
}
