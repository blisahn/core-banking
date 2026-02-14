package com.devblo.account.query.getAccaountBalance;

import com.devblo.account.repository.AccountSummary;
import com.devblo.common.IQuery;
import com.devblo.common.result.Result;

import java.util.UUID;

public record GetAccountBalanceQuery(UUID accountId) implements IQuery<Result<AccountSummary>> {
}
