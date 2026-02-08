package com.devblo.account.query.getAccaountBalance;

import com.devblo.account.AccountId;
import com.devblo.common.IQuery;
import com.devblo.common.Result;
import com.devblo.shared.Money;

public record GetAccountBalanceQuery(AccountId accountId) implements IQuery<Result<Money>> {
}
