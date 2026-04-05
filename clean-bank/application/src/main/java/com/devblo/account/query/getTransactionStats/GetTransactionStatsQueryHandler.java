package com.devblo.account.query.getTransactionStats;

import com.devblo.account.AccountId;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.common.IQueryHandler;
import com.devblo.common.result.Result;
import com.devblo.transaction.repository.ITransactionReadRepository;
import com.devblo.transaction.repository.TransactionStatsSummary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetTransactionStatsQueryHandler
        implements IQueryHandler<GetTransactionStatsQuery, Result<TransactionStatsSummary>> {

    private final ITransactionReadRepository transactionReadRepository;
    private final IAccountWriteRepository accountWriteRepository;

    public GetTransactionStatsQueryHandler(ITransactionReadRepository transactionReadRepository,
                                           IAccountWriteRepository accountWriteRepository) {
        this.transactionReadRepository = transactionReadRepository;
        this.accountWriteRepository = accountWriteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Result<TransactionStatsSummary> handle(GetTransactionStatsQuery query) {
        AccountId accountId = AccountId.of(query.accountId());
        if (!accountWriteRepository.existsById(accountId)) {
            return Result.failure("Account not found");
        }
        TransactionStatsSummary stats = transactionReadRepository.getStatsByAccountId(
                accountId, query.from(), query.to());
        return Result.success(stats);
    }
}
