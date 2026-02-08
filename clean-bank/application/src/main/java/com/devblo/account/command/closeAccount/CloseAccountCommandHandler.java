package com.devblo.account.command.closeAccount;

import com.devblo.account.Account;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.common.ICommandHandler;
import com.devblo.common.Result;
import org.springframework.transaction.annotation.Transactional;

public class CloseAccountCommandHandler implements ICommandHandler<CloseAccountCommand, Result<Boolean>> {
    private final IAccountWriteRepository accountWriteRepository;

    public CloseAccountCommandHandler(IAccountWriteRepository accountWriteRepository) {
        this.accountWriteRepository = accountWriteRepository;
    }

    @Override
    @Transactional
    public Result<Boolean> handle(CloseAccountCommand command) {
        var optAccount = accountWriteRepository
                .findById(command.accountId());
        if (optAccount.isEmpty()) {
            return Result.notFound("Account with id " + command.accountId() + " not found");
        }
        try {
            Account account = optAccount.get();
            account.close();
            accountWriteRepository.save(account);
        } catch (Exception e) {
            return Result.unexpected(e.getMessage());
        }
        return Result.ok(true);
    }
}
