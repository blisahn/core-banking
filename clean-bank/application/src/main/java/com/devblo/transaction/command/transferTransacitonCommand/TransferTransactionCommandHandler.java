package com.devblo.transaction.command.transferTransacitonCommand;

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
public class TransferTransactionCommandHandler implements ICommandHandler<TransferTransactionCommand, Result<TransactionId>> {
    private final ITransactionWriteRepository transactionWriteRepository;

    public TransferTransactionCommandHandler(ITransactionWriteRepository transactionWriteRepository) {
        this.transactionWriteRepository = transactionWriteRepository;
    }

    @Override
    @Transactional
    public Result<TransactionId> handle(TransferTransactionCommand command) {
        Result<Transaction> transactionResult = Transaction.transfer(
                AccountId.of(command.sourceAccountId()),
                AccountId.of(command.targetAccountId()),
                Money.of(command.amount(), command.currency()),
                command.description()
        );

        if (transactionResult.isSuccess()) {
            return Result.failure(transactionResult.getError());
        }
        Transaction transaction = transactionResult.getValue();
        transactionWriteRepository.save(transaction);
        return Result.success(transaction.getId());
    }
}
