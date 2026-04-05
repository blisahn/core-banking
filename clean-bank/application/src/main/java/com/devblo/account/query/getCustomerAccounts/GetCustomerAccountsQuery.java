package com.devblo.account.query.getCustomerAccounts;

import com.devblo.common.IQuery;
import com.devblo.common.result.Result;
import com.devblo.account.repository.AccountSummary;

import java.util.List;
import java.util.UUID;

public record GetCustomerAccountsQuery(UUID customerId) implements IQuery<Result<List<AccountSummary>>> {
}
