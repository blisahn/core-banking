package com.devblo.account.query.getAccaountBalance;

import com.devblo.common.IQuery;
import com.devblo.common.result.Result;
import com.devblo.shared.Money;

import java.util.UUID;

public record GetAccountBalanceQuery(UUID accountId) implements IQuery<Result<Money>> {
}
