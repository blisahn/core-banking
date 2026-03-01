package com.devblo.infrastructure.persistence.account;

import com.devblo.account.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

//    Optional<Account> findById(AccountId id);
//
//    Optional<Account> findByAccountNumber(AccountNumber accountNumber);
//
//    Account save(Account account);
//
//    void delete(Account account);
//
//    boolean existsById(AccountId id);
//
//    boolean existsByAccountNumber(AccountNumber accountNumber);
public interface AccountJpaRepository extends JpaRepository<AccountEntity, UUID> {
    Optional<AccountEntity> findByAccountNumber(String accountNumber);
    List<AccountEntity> findByCustomerId(UUID customerId);
    List<AccountEntity> findByStatus(AccountStatus status);
    boolean existsByAccountNumber(String accountNumber);
}
