package com.devblo.account.command.activateAccount;

import com.devblo.account.Account;
import com.devblo.account.AccountId;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivateAccountCommandHandler implements ICommandHandler<ActivateAccountCommand, Result<Void>> {

    private final IAccountWriteRepository accountWriteRepository;

    public ActivateAccountCommandHandler(IAccountWriteRepository accountWriteRepository) {
        this.accountWriteRepository = accountWriteRepository;
    }

    @Override
    @Transactional
    public Result<Void> handle(ActivateAccountCommand command) {
        var optAccount = accountWriteRepository
                .findById(AccountId.of(command.accountId()));
        if (optAccount.isEmpty())
            return Result.failure("account with id " + command.accountId() + " not found");
        try {
            Account account = optAccount.get();
            account.activate();
            accountWriteRepository.save(account);
            return Result.success(null);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }
}
