package com.devblo.infrastructure.persistence.transaction;

import com.devblo.account.AccountId;
import com.devblo.transaction.Transaction;
import com.devblo.transaction.repository.ITransactionReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaTransactionReadRepository implements ITransactionReadRepository {

    private final TransactionJpaRepository jpaRepo;
    private final TransactionEntityMapper mapper;

    @Override
    public List<Transaction> findAllByAccountId(AccountId accountId) {
        return jpaRepo.findBySourceAccountIdOrTargetAccountId(accountId.value(), accountId.value())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
