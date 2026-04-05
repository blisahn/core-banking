package com.devblo.transaction.repository;

import com.devblo.account.AccountId;
import com.devblo.common.PagedResult;

import java.time.Instant;
import java.util.List;

public interface ITransactionReadRepository {
    List<TransactionSummary> findAllByAccountId(AccountId accountId);

    PagedResult<TransactionSummary> findByAccountId(AccountId accountId, Instant from, Instant to,
                                                     int page, int size);

    TransactionStatsSummary getStatsByAccountId(AccountId accountId, Instant from, Instant to);
}
