package com.devblo.infrastructure.persistence.transaction;

import com.devblo.infrastructure.events.DomainEventPublisher;
import com.devblo.transaction.Transaction;
import com.devblo.transaction.TransactionId;
import com.devblo.transaction.repository.ITransactionWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaTransactionWriteRepository implements ITransactionWriteRepository {

    private final TransactionJpaRepository jpaRepo;
    private final TransactionEntityMapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = mapper.toEntity(transaction);
        TransactionEntity saved = jpaRepo.save(entity);
        eventPublisher.publishAll(transaction);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Transaction> findById(TransactionId id) {
        return jpaRepo.findById(id.value()).map(mapper::toDomain);
    }
}
