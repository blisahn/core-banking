package com.devblo.infrastructure.persistence.account;

import com.devblo.account.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountJpaRepositoryAdapter extends JpaRepository<AccountEntity, UUID> {
    Optional<AccountEntity> findByAccountNumber(String accountNumber);
    List<AccountEntity> findByCustomerId(UUID customerId);
    Page<AccountEntity> findByCustomerId(UUID customerId, Pageable pageable);
    List<AccountEntity> findByStatus(AccountStatus status);
    boolean existsByAccountNumber(String accountNumber);
}
