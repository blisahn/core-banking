package com.devblo.infrastructure.persistence.account;

import com.devblo.account.AccountId;
import com.devblo.account.AccountStatus;
import com.devblo.account.repository.AccountSummary;
import com.devblo.account.repository.IAccountReadRepository;
import com.devblo.customer.CustomerId;
import com.devblo.shared.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaAccountReadRepository implements IAccountReadRepository {

    private final AccountJpaRepositoryAdapter jpaRepo;
    private final AccountEntityMapper mapper;

    @Override
    public Optional<AccountSummary> findSummaryById(AccountId id) {
        return jpaRepo.findById(id.value()).map(mapper::toSummary);
    }

    @Override
    public List<AccountSummary> findSummariesByCustomerId(CustomerId customerId) {
        return jpaRepo.findByCustomerId(customerId.value())
                .stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    public List<AccountSummary> findActiveAccounts() {
        return jpaRepo.findByStatus(AccountStatus.ACTIVE)
                .stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    public List<AccountSummary> findAccountsWithBalanceGreaterThan(Money amount) {
        // All accounts filtered in memory — for production, use a JPQL query
        return jpaRepo.findAll()
                .stream()
                .filter(e -> e.getBalanceAmount().compareTo(amount.amount()) > 0)
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    public long countByCustomerId(CustomerId customerId) {
        return jpaRepo.findByCustomerId(customerId.value()).size();
    }
}
