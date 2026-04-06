package com.devblo.admin.query.getAllTransactions;

import com.devblo.common.IQueryHandler;
import com.devblo.common.PagedResult;
import com.devblo.common.result.Result;
import com.devblo.transaction.repository.ITransactionReadRepository;
import com.devblo.transaction.repository.TransactionSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllTransactionsQueryHandler implements IQueryHandler<GetAllTransactionsQuery, Result<PagedResult<TransactionSummary>>> {

    private final ITransactionReadRepository readRepository;

    @Override
    public Result<PagedResult<TransactionSummary>> handle(GetAllTransactionsQuery query) {
        PagedResult<TransactionSummary> result = readRepository.findAll(query.page(), query.size());
        return Result.success(result);
    }
}
