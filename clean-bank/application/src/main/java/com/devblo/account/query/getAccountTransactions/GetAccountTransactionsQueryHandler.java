package com.devblo.account.query.getAccountTransactions;

import com.devblo.account.AccountId;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.common.IQueryHandler;
import com.devblo.common.PagedResult;
import com.devblo.common.result.Result;
import com.devblo.transaction.repository.ITransactionReadRepository;
import com.devblo.transaction.repository.TransactionSummary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetAccountTransactionsQueryHandler
        implements IQueryHandler<GetAccountTransactionsQuery, Result<PagedResult<TransactionSummary>>> {

    private final ITransactionReadRepository transactionReadRepository;
    private final IAccountWriteRepository accountWriteRepository;

    public GetAccountTransactionsQueryHandler(ITransactionReadRepository transactionReadRepository,
                                              IAccountWriteRepository accountWriteRepository) {
        this.transactionReadRepository = transactionReadRepository;
        this.accountWriteRepository = accountWriteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Result<PagedResult<TransactionSummary>> handle(GetAccountTransactionsQuery query) {
        AccountId accountId = AccountId.of(query.accountId());
        if (!accountWriteRepository.existsById(accountId)) {
            return Result.failure("Account not found");
        }
        PagedResult<TransactionSummary> transactions = transactionReadRepository.findByAccountId(
                accountId, query.from(), query.to(), query.page(), query.size());
        return Result.success(transactions);
    }
}
