package com.devblo.account.command.depositMoney;

import com.devblo.account.Account;
import com.devblo.account.AccountId;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
import com.devblo.shared.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepositMoneyCommandHandler implements ICommandHandler<DepositMoneyCommand, Result<Void>> {

    private final IAccountWriteRepository accountWriteRepository;

    public DepositMoneyCommandHandler(IAccountWriteRepository accountWriteRepository) {
        this.accountWriteRepository = accountWriteRepository;
    }

    @Override
    @Transactional
    public Result<Void> handle(DepositMoneyCommand cmd) {
        var optAccount = accountWriteRepository
                .findById(AccountId.of(cmd.id()));
        if (optAccount.isEmpty()) {
            return Result.failure("Account with id " + cmd.id() + " not found");
        }
        try {
            Account account = optAccount.get();
            account.deposit(Money.of(
                    cmd.amount(),
                    cmd.currency()
            ));
            accountWriteRepository.save(account);
            return Result.success(null);
        } catch (RuntimeException e) {
            return Result.failure(e.getMessage());
        }
    }
}

