package com.devblo.api.config.security;

import com.devblo.account.Account;
import com.devblo.account.AccountId;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.customer.CustomerId;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountOwnershipService {

    private final IAccountWriteRepository accountWriteRepository;

    public AccountOwnershipService(IAccountWriteRepository accountWriteRepository) {
        this.accountWriteRepository = accountWriteRepository;
    }

    public boolean isOwner(UUID accountId, UUID customerId) {
        if (customerId == null) return false;
        Optional<Account> account = accountWriteRepository.findById(AccountId.of(accountId));
        return account.isPresent()
                && account.get().getCustomerId().equals(CustomerId.of(customerId));
    }
}
