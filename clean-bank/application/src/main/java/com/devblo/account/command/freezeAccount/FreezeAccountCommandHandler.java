package com.devblo.account.command.freezeAccount;

import com.devblo.account.Account;
import com.devblo.account.AccountId;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.common.ICommandHandler;
import com.devblo.common.Result;
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
        var optAccount = accountWriteRepository
                .findById(AccountId.of(command.accountId()));
        if (optAccount.isEmpty()) {
            return Result.failure("Account with id " + command.accountId() + " not found");
        }
        try {
            Account account = optAccount.get();
            account.freeze();
            accountWriteRepository.save(account);
        return Result.success(null);

        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }
}
