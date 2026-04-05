package com.devblo.account.query.getTransactionStats;

import com.devblo.common.IQuery;
import com.devblo.common.result.Result;
import com.devblo.transaction.repository.TransactionStatsSummary;

import java.time.Instant;
import java.util.UUID;

public record GetTransactionStatsQuery(
        UUID accountId,
        Instant from,
        Instant to
) implements IQuery<Result<TransactionStatsSummary>> {

    public GetTransactionStatsQuery(UUID accountId) {
        this(accountId, Instant.EPOCH, Instant.now());
    }
}
