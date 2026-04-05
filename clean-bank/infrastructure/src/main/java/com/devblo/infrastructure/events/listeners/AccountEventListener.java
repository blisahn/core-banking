package com.devblo.infrastructure.events.listeners;

import com.devblo.account.event.AccountOpenedEvent;
import com.devblo.account.event.MoneyDepositedEvent;
import com.devblo.account.event.MoneyWithdrawEvent;
import com.devblo.infrastructure.events.outbox.OutboxEventService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AccountEventListener {

    private static final Logger log = LoggerFactory.getLogger(AccountEventListener.class);
    private final OutboxEventService outboxEventService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onAccountOpened(AccountOpenedEvent event) {
        log.info("Account opened: id={}, type={}, customerId={}",
                event.getAccountId(), event.getAccountType(), event.getCustomerId());
        outboxEventService.saveEvent(event, "account",event.getAccountId().value());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onMoneyDeposited(MoneyDepositedEvent event) {
        log.info("Money deposited: accountId={}, amount={}, newBalance={}",
                event.getAccountId(), event.getAmount(), event.getBalance());
        outboxEventService.saveEvent(event, "account",event.getAccountId().value());

    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onMoneyWithdrawn(MoneyWithdrawEvent event) {
        log.info("Money withdrawn: accountId={}, amount={}, newBalance={}",
                event.getAccountId(), event.getAmount(), event.getNewBalance());
        outboxEventService.saveEvent(event, "account",event.getAccountId().value());

    }
}
