package com.devblo.account.command.freezeAccount;

import com.devblo.account.Account;
import com.devblo.account.AccountId;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FreezeAccountCommandHandler implements ICommandHandler<FreezeAccountCommand, Result<Void>> {

    private final IAccountWriteRepository accountWriteRepository;

    public FreezeAccountCommandHandler(IAccountWriteRepository accountWriteRepository) {
        this.accountWriteRepository = accountWriteRepository;
    }

    @Override
    @Transactional
    public Result<Void> handle(FreezeAccountCommand command) {
        Result<Account> optAccount = accountWriteRepository
                .findById(AccountId.of(command.accountId()))
                .map(Result::success)
                .orElseGet(() -> Result.failure("Account not found"));
        if (optAccount.isFailure()) {
            return Result.failure(optAccount.getError());
        }

        Account account = optAccount.getValue();
        Result<Void> freezeResult = account.freeze();
        if (freezeResult.isFailure()) {
            return Result.failure(freezeResult.getError());
        }
        accountWriteRepository.save(account);
        return Result.success();


    }
}
