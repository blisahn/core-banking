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
        Result<Account> optAccount = accountWriteRepository
                .findById(AccountId.of(cmd.id()))
                .map(Result::success)
                .orElseGet(() -> Result.failure("Account not found"));
        if (optAccount.isFailure()) {
            return Result.failure(optAccount.getError());
        }
        Account account = optAccount.getValue();
        Result<Void> depositResult = account.deposit(Money.of(
                cmd.amount(),
                cmd.currency()
        ));
        if (depositResult.isFailure()) {
            return Result.failure(depositResult.getError());
        }
        accountWriteRepository.save(account);
        return Result.success();
    }
}

