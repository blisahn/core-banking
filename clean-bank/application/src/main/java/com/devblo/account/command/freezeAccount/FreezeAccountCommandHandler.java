package com.devblo.account.command.freezeAccount;

import com.devblo.account.Account;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.common.ICommandHandler;
import com.devblo.common.Result;
import org.springframework.transaction.annotation.Transactional;

public class FreezeAccountCommandHandler implements ICommandHandler<FreezeAccountCommand, Result<Boolean>> {

    private final IAccountWriteRepository accountWriteRepository;

    public FreezeAccountCommandHandler(IAccountWriteRepository accountWriteRepository) {
        this.accountWriteRepository = accountWriteRepository;
    }

    @Override
    @Transactional
    public Result<Boolean> handle(FreezeAccountCommand command) {
        var optAccount = accountWriteRepository
                .findById(command.accountId());
        if (optAccount.isEmpty()) {
            return Result.notFound("Account with id " + command.accountId() + " not found");
        }
        try {
            Account account = optAccount.get();
            account.freeze();
            accountWriteRepository.save(account);
        } catch (Exception e) {
            return Result.unexpected(e.getMessage());
        }
        return Result.ok(true);
    }
}
