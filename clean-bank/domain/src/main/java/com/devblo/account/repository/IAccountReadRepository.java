package com.devblo.account.repository;

import com.devblo.account.AccountId;
import com.devblo.customer.CustomerId;
import com.devblo.shared.Money;

import java.util.List;
import java.util.Optional;

public interface IAccountReadRepository {

    Optional<AccountSummary> findSummaryById(AccountId id);

    List<AccountSummary> findSummariesByCustomerId(CustomerId customerId);

    List<AccountSummary> findActiveAccounts();

    List<AccountSummary> findAccountsWithBalanceGreaterThan(Money amount);

    long countByCustomerId(CustomerId customerId);
}
