package com.devblo.account.command.transferMoney;

import com.devblo.account.Account;
import com.devblo.account.AccountId;
import com.devblo.account.AccountNumber;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
import com.devblo.shared.Money;
import com.devblo.transaction.Transaction;
import com.devblo.transaction.repository.ITransactionWriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferMoneyCommandHandler implements ICommandHandler<TransferMoneyCommand, Result<Void>> {

    private final IAccountWriteRepository accountWriteRepository;
    private final ITransactionWriteRepository transactionWriteRepository;

    public TransferMoneyCommandHandler(IAccountWriteRepository accountWriteRepository,
            ITransactionWriteRepository transactionWriteRepository) {
        this.accountWriteRepository = accountWriteRepository;
        this.transactionWriteRepository = transactionWriteRepository;
    }

    @Override
    @Transactional
    public Result<Void> handle(TransferMoneyCommand command) {
        AccountId sourceId = AccountId.of(command.sourceAccountId());
        Money amount = Money.of(command.amount(), command.currency());

        Account sourceAccount = accountWriteRepository.findById(sourceId).orElse(null);
        if (sourceAccount == null) {
            return Result.failure("Source account not found");
        }

        AccountNumber targetIban;
        try {
            targetIban = AccountNumber.of(command.targetAccountNumber());
        } catch (IllegalArgumentException e) {
            return Result.failure("Invalid target IBAN format");
        }

        Account targetAccount = accountWriteRepository.findByAccountNumber(targetIban).orElse(null);
        if (targetAccount == null) {
            return Result.failure("Target account not found for IBAN: " + command.targetAccountNumber());
        }

        if (sourceAccount.getId().equals(targetAccount.getId())) {
            return Result.failure("Cannot transfer to the same account");
        }

        // Withdraw from source
        Result<Void> withdrawResult = sourceAccount.withdraw(amount);
        if (withdrawResult.isFailure()) {
            return withdrawResult;
        }

        // Deposit to target
        Result<Void> depositResult = targetAccount.deposit(amount);
        if (depositResult.isFailure()) {
            return depositResult;
        }

        // Transaction audit log
        Result<Transaction> txResult = Transaction.transfer(
                sourceId, targetAccount.getId(), amount, command.description());
        if (txResult.isFailure()) {
            return Result.failure(txResult.getError());
        }

        // Save in consistent order (by UUID) to prevent deadlocks
        AccountId firstId = sourceId.value().compareTo(targetAccount.getId().value()) < 0
                ? sourceId
                : targetAccount.getId();
        Account first = firstId.equals(sourceId) ? sourceAccount : targetAccount;
        Account second = firstId.equals(sourceId) ? targetAccount : sourceAccount;
        accountWriteRepository.save(first);
        accountWriteRepository.save(second);
        transactionWriteRepository.save(txResult.getValue());
        return Result.success();
    }
}
