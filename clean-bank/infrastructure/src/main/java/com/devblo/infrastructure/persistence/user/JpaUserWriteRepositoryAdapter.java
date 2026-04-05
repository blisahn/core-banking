package com.devblo.infrastructure.persistence.user;

import com.devblo.infrastructure.events.DomainEventPublisher;
import com.devblo.user.User;
import com.devblo.user.UserId;
import com.devblo.user.repository.IUserWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaUserWriteRepositoryAdapter implements IUserWriteRepository {

    private final UserJpaRepository jpaRepo;
    private final UserEntityMapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        jpaRepo.save(entity);
        eventPublisher.publishAll(user);
        return user;
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepo.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepo.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepo.existsByEmail(email);
    }
}
