package com.devblo.transaction.command.depositTransactionCommand;

import com.devblo.account.AccountId;
import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
import com.devblo.shared.Money;
import com.devblo.transaction.Transaction;
import com.devblo.transaction.TransactionId;
import com.devblo.transaction.repository.ITransactionWriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepositTransactionCommandHandler implements ICommandHandler<DepositTransactionCommand, Result<TransactionId>> {

    private final ITransactionWriteRepository transactionWriteRepository;

    public DepositTransactionCommandHandler(ITransactionWriteRepository transactionWriteRepository) {
        this.transactionWriteRepository = transactionWriteRepository;
    }

    @Override
    @Transactional
    public Result<TransactionId> handle(DepositTransactionCommand command) {
        Result<Transaction> transactionResult = Transaction.deposit(
                AccountId.of(command.targetAccountId()),
                Money.of(command.amount(), command.currency()),
                command.description()
        );
        if (transactionResult.isFailure()) {
            return Result.failure(transactionResult.getError());
        }
        Transaction transaction = transactionResult.getValue();
        transactionWriteRepository.save(transaction);
        return Result.success(transaction.getId());
    }
}
