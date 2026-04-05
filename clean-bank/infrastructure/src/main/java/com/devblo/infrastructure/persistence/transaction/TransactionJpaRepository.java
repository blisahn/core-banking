package com.devblo.infrastructure.persistence.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findBySourceAccountIdOrTargetAccountId(UUID sourceAccountId, UUID targetAccountId);

    @Query("SELECT t FROM TransactionEntity t " +
           "WHERE (t.sourceAccountId = :accountId OR t.targetAccountId = :accountId) " +
           "AND t.timestamp >= :from AND t.timestamp <= :to " +
           "ORDER BY t.timestamp DESC")
    Page<TransactionEntity> findByAccountIdAndDateRange(
            @Param("accountId") UUID accountId,
            @Param("from") Instant from,
            @Param("to") Instant to,
            Pageable pageable);

    @Query("SELECT t FROM TransactionEntity t " +
           "WHERE (t.sourceAccountId = :accountId OR t.targetAccountId = :accountId) " +
           "AND t.timestamp >= :from AND t.timestamp <= :to")
    List<TransactionEntity> findByAccountIdAndDateRange(
            @Param("accountId") UUID accountId,
            @Param("from") Instant from,
            @Param("to") Instant to);
}
