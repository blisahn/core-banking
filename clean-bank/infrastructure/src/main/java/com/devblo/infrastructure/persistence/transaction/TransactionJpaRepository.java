package com.devblo.infrastructure.persistence.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findBySourceAccountIdOrTargetAccountId(UUID sourceAccountId, UUID targetAccountId);
}
