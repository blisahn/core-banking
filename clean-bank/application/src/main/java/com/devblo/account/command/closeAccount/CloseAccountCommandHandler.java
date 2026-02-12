package com.devblo.account.command.closeAccount;

import com.devblo.account.Account;
import com.devblo.account.AccountId;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CloseAccountCommandHandler implements ICommandHandler<CloseAccountCommand, Result<Void>> {
    private final IAccountWriteRepository accountWriteRepository;

    public CloseAccountCommandHandler(IAccountWriteRepository accountWriteRepository) {
        this.accountWriteRepository = accountWriteRepository;
    }

    @Override
    @Transactional
    public Result<Void> handle(CloseAccountCommand command) {
        var optAccount = accountWriteRepository
                .findById(AccountId.of(command.accountId()));
        if (optAccount.isEmpty()) {
            return Result.failure("Account with id " + command.accountId() + " not found");
        }
        try {
            Account account = optAccount.get();
            account.close();
            accountWriteRepository.save(account);
            return Result.success(null);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }
}
