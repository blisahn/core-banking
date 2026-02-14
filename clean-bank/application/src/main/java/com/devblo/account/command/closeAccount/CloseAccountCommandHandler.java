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
        Result<Account> optAccount = accountWriteRepository
                .findById(AccountId.of(command.accountId()))
                .map(Result::success)
                .orElseGet(() -> Result.failure("Account not found"));
        if (optAccount.isFailure()) {
            return Result.failure(optAccount.getError());
        }
        Account account = optAccount.getValue();
        Result<Void> closeAccountResult = account.close();
        if (closeAccountResult.isFailure()) {
            return Result.failure(closeAccountResult.getError());
        }
        accountWriteRepository.save(account);
        return Result.success();

    }
}
