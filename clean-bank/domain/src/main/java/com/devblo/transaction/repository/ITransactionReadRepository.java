package com.devblo.transaction.repository;

import com.devblo.account.AccountId;
import com.devblo.transaction.Transaction;

import java.util.List;

public interface ITransactionReadRepository {
    List<Transaction> findAllByAccountId(AccountId accountId);
}
