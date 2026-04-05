package com.devblo.account.repository;

import com.devblo.account.Account;
import com.devblo.account.AccountId;
import com.devblo.account.AccountNumber;
import com.devblo.customer.CustomerId;

import java.util.List;
import java.util.Optional;


public interface IAccountWriteRepository {
    Optional<Account> findById(AccountId id);

    Optional<Account> findByAccountNumber(AccountNumber accountNumber);

    List<Account> findByCustomerId(CustomerId customerId);

    Account save(Account account);

    void delete(Account account);

    boolean existsById(AccountId id);

    boolean existsByAccountNumber(AccountNumber accountNumber);
}
