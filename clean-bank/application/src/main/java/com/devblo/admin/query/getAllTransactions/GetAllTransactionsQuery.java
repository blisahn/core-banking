package com.devblo.admin.query.getAllTransactions;

import com.devblo.common.IQuery;
import com.devblo.common.PagedResult;
import com.devblo.common.result.Result;
import com.devblo.transaction.repository.TransactionSummary;

public record GetAllTransactionsQuery(int page, int size) implements IQuery<Result<PagedResult<TransactionSummary>>> {
}
