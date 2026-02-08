package com.devblo.account.command.activateAccount;

import com.devblo.account.Account;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.common.ICommandHandler;
import com.devblo.common.Result;
import org.springframework.transaction.annotation.Transactional;

public class ActivateAccountCommandHandler implements ICommandHandler<ActivateAccountCommand, Result<Boolean>> {

    private final IAccountWriteRepository accountWriteRepository;

    public ActivateAccountCommandHandler(IAccountWriteRepository accountWriteRepository) {
        this.accountWriteRepository = accountWriteRepository;
    }

    @Override
    @Transactional
    public Result<Boolean> handle(ActivateAccountCommand command) {
        var optAccount = accountWriteRepository
                .findById(command.accountId());
        if (optAccount.isEmpty())
            return Result.notFound("account with id " + command.accountId() + " not found");
        try {
            Account account = optAccount.get();
            account.activate();
            accountWriteRepository.save(account);
        } catch (Exception e) {
            return Result.unexpected(e.getMessage());
        }

        return Result.ok(true);
    }
}
