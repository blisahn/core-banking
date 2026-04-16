package com.devblo.account.command.withdrawMoney;

import com.devblo.account.Account;
import com.devblo.account.AccountId;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
import com.devblo.shared.Money;
import com.devblo.transaction.Transaction;
import com.devblo.transaction.repository.ITransactionWriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WithdrawMoneyCommandHandler implements ICommandHandler<WithdrawMoneyCommand, Result<Void>> {

    private final IAccountWriteRepository accountWriteRepository;
    private final ITransactionWriteRepository transactionWriteRepository;

    public WithdrawMoneyCommandHandler(IAccountWriteRepository accountWriteRepository,
            ITransactionWriteRepository transactionWriteRepository) {
        this.accountWriteRepository = accountWriteRepository;
        this.transactionWriteRepository = transactionWriteRepository;
    }

    @Override
    @Transactional
    public Result<Void> handle(WithdrawMoneyCommand command) {
        Result<Account> optAccount = accountWriteRepository
                .findById(AccountId.of(command.id()))
                .map(Result::success)
                .orElseGet(() -> Result.failure("Account not found"));
        if (optAccount.isFailure()) {
            return Result.failure(optAccount.getError());
        }

        Account account = optAccount.getValue();
        Money amount = Money.of(command.amount(), command.currency());

        Result<Void> withdrawResult = account.withdraw(amount);
        if (withdrawResult.isFailure()) {
            return withdrawResult;
        }

        // Transaction audit log
        Result<Transaction> txResult = Transaction.withdraw(
                AccountId.of(command.id()), amount, command.description());
        if (txResult.isFailure()) {
            return Result.failure(txResult.getError());
        }

        accountWriteRepository.save(account);
        transactionWriteRepository.save(txResult.getValue());
        return Result.success();
    }
}
