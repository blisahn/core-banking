package com.devblo.infrastructure.persistence.account;

import com.devblo.account.Account;
import com.devblo.account.AccountId;
import com.devblo.account.AccountNumber;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.customer.CustomerId;
import com.devblo.infrastructure.events.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaAccountWriteRepository implements IAccountWriteRepository {

    private final AccountJpaRepositoryAdapter jpaRepo;
    private final AccountEntityMapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    public Optional<Account> findById(AccountId id) {
        return jpaRepo.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<Account> findByAccountNumber(AccountNumber accountNumber) {
        return jpaRepo.findByAccountNumber(accountNumber.value()).map(mapper::toDomain);
    }

    @Override
    public List<Account> findByCustomerId(CustomerId customerId) {
        return jpaRepo.findByCustomerId(customerId.value())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Account save(Account account) {
        AccountEntity entity = mapper.toEntity(account);
        AccountEntity saved = jpaRepo.save(entity);
        eventPublisher.publishAll(account);
        return mapper.toDomain(saved);
    }

    @Override
    public void delete(Account account) {
        jpaRepo.deleteById(account.getId().value());
    }

    @Override
    public boolean existsById(AccountId id) {
        return jpaRepo.existsById(id.value());
    }

    @Override
    public boolean existsByAccountNumber(AccountNumber accountNumber) {
        return jpaRepo.existsByAccountNumber(accountNumber.value());
    }
}
