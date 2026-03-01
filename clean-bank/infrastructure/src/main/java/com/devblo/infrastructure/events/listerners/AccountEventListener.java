package com.devblo.infrastructure.events.listerners;

import com.devblo.account.event.AccountOpenedEvent;
import com.devblo.account.event.MoneyDepositedEvent;
import com.devblo.account.event.MoneyWithdrawEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AccountEventListener {

    private static final Logger log = LoggerFactory.getLogger(AccountEventListener.class);

    @EventListener
    public void onAccountOpened(AccountOpenedEvent event) {
        log.info("Account opened: id={}, type={}, customerId={}",
                event.getAccountId(), event.getAccountType(), event.getCustomerId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMoneyDeposited(MoneyDepositedEvent event) {
        log.info("Money deposited: accountId={}, amount={}, newBalance={}",
                event.getAccountId(), event.getAmount(), event.getBalance());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMoneyWithdrawn(MoneyWithdrawEvent event) {
        log.info("Money withdrawn: accountId={}, amount={}, newBalance={}",
                event.getAccountId(), event.getAmount(), event.getNewBalance());
    }
}
