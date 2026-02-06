package com.devblo.transaction.repository;

import com.devblo.transaction.Transaction;
import com.devblo.transaction.TransactionId;

import java.util.Optional;

public interface ITransactionWriteRepository {
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(TransactionId id);
}
