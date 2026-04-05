package com.devblo.account.query.getAccountTransactions;

import com.devblo.common.IQuery;
import com.devblo.common.PagedResult;
import com.devblo.common.result.Result;
import com.devblo.transaction.repository.TransactionSummary;

import java.time.Instant;
import java.util.UUID;

public record GetAccountTransactionsQuery(
        UUID accountId,
        Instant from,
        Instant to,
        int page,
        int size
) implements IQuery<Result<PagedResult<TransactionSummary>>> {

    public GetAccountTransactionsQuery(UUID accountId) {
        this(accountId, Instant.EPOCH, Instant.now(), 0, 20);
    }
}

