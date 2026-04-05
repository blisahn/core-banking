package com.devblo.infrastructure.persistence.transaction;

import com.devblo.account.AccountId;
import com.devblo.common.PagedResult;
import com.devblo.transaction.TransactionType;
import com.devblo.transaction.repository.ITransactionReadRepository;
import com.devblo.transaction.repository.TransactionStatsSummary;
import com.devblo.transaction.repository.TransactionSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaTransactionReadRepositoryAdapter implements ITransactionReadRepository {

    private final TransactionJpaRepository jpaRepo;
    private final TransactionEntityMapper mapper;

    @Override
    public List<TransactionSummary> findAllByAccountId(AccountId accountId) {
        return jpaRepo.findBySourceAccountIdOrTargetAccountId(accountId.value(), accountId.value())
                .stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    public PagedResult<TransactionSummary> findByAccountId(AccountId accountId, Instant from, Instant to,
                                                            int page, int size) {
        Page<TransactionEntity> result = jpaRepo.findByAccountIdAndDateRange(
                accountId.value(), from, to, PageRequest.of(page, size));
        List<TransactionSummary> content = result.getContent().stream()
                .map(mapper::toSummary)
                .toList();
        return new PagedResult<>(content, result.getNumber(), result.getSize(),
                result.getTotalElements(), result.getTotalPages());
    }

    @Override
    public TransactionStatsSummary getStatsByAccountId(AccountId accountId, Instant from, Instant to) {
        UUID id = accountId.value();
        List<TransactionEntity> transactions = jpaRepo.findByAccountIdAndDateRange(id, from, to);

        BigDecimal totalDeposits = BigDecimal.ZERO;
        BigDecimal totalWithdrawals = BigDecimal.ZERO;
        BigDecimal totalTransfersIn = BigDecimal.ZERO;
        BigDecimal totalTransfersOut = BigDecimal.ZERO;

        for (TransactionEntity tx : transactions) {
            if (tx.getType() == TransactionType.DEPOSIT) {
                totalDeposits = totalDeposits.add(tx.getAmount());
            } else if (tx.getType() == TransactionType.WITHDRAWAL) {
                totalWithdrawals = totalWithdrawals.add(tx.getAmount());
            } else if (tx.getType() == TransactionType.TRANSFER) {
                if (id.equals(tx.getTargetAccountId())) {
                    totalTransfersIn = totalTransfersIn.add(tx.getAmount());
                }
                if (id.equals(tx.getSourceAccountId())) {
                    totalTransfersOut = totalTransfersOut.add(tx.getAmount());
                }
            }
        }

        return new TransactionStatsSummary(totalDeposits, totalWithdrawals,
                totalTransfersIn, totalTransfersOut, transactions.size());
    }
}
