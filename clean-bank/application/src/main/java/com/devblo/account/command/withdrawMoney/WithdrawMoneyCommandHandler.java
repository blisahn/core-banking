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
        var optAccount = accountWriteRepository
                .findById(AccountId.of(command.id()));

        if (optAccount.isEmpty()) {
            return Result.failure("Account with id " + command.id() + " not found");
        }
        try {
            Account account = optAccount.get();
            account.withdraw(Money.of(
                    command.amount(),
                    command.currency()
            ));
            accountWriteRepository.save(account);
            return Result.success(null);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }

    }

}
