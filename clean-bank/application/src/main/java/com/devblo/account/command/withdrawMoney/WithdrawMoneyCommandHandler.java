package com.devblo.account.command.withdrawMoney;

import com.devblo.account.Account;
import com.devblo.account.AccountId;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
import com.devblo.shared.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WithdrawMoneyCommandHandler implements ICommandHandler<WithdrawMoneyCommand, Result<Void>> {
    private final IAccountWriteRepository accountWriteRepository;

    public WithdrawMoneyCommandHandler(IAccountWriteRepository accountWriteRepository) {
        this.accountWriteRepository = accountWriteRepository;
    }


    @Override
    @Transactional
    public Result<Void> handle(WithdrawMoneyCommand command) {
        Result<Account> optAccount = accountWriteRepository
                .findById(AccountId.of(command.id()))
                .map(Result::success)
                .orElseGet(() -> Result.failure("Account not found"));
        if (optAccount.isFailure()) {
            return Result.failure("Account with id " + command.id() + " not found");
        }

        Account account = optAccount.getValue();
        Result<Void> witrhdrawResult = account.withdraw(Money.of(
                command.amount(),
                command.currency()
        ));
        if (witrhdrawResult.isFailure()) {
            return witrhdrawResult;
        }
        accountWriteRepository.save(account);
        return Result.success(null);
    }

}
