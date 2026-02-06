package com.devblo.account.repository;

import com.devblo.account.Account;
import com.devblo.account.AccountId;
import com.devblo.account.AccountNumber;

import java.util.Optional;

public interface IAccountWriteRepository {
    Optional<Account> findById(AccountId id);

    Optional<Account> findByAccountNumber(AccountNumber accountNumber);

    Account save(Account account);

    void delete(Account account);

    boolean existsById(AccountId id);

    boolean existsByAccountNumber(AccountNumber accountNumber);
}
