package com.devblo.account.command.withdrawMoney;

import com.devblo.account.Account;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.common.ICommandHandler;
import com.devblo.common.Result;
import org.springframework.transaction.annotation.Transactional;

public class WithdrawMoneyCommandHandler implements ICommandHandler<WithdrawMoneyCommand, Result<Boolean>> {
    private final IAccountWriteRepository accountWriteRepository;

    public WithdrawMoneyCommandHandler(IAccountWriteRepository accountWriteRepository) {
        this.accountWriteRepository = accountWriteRepository;
    }


    @Override
    @Transactional
    public Result<Boolean> handle(WithdrawMoneyCommand command) {
        var optAccount = accountWriteRepository
                .findById(command.id());

        if (optAccount.isEmpty()) {
            return  Result.notFound("Account with id " + command.id() + " not found");
        }
        try{
            Account account = optAccount.get();
            account.withdraw(command.money());
            accountWriteRepository.save(account);
        }catch (Exception e){
            return Result.unexpected(e.getMessage());
        }

        return Result.ok(true);
    }

}
