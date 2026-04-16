package com.devblo.infrastructure.events.listeners;

import com.devblo.account.event.AccountOpenedEvent;
import com.devblo.account.event.MoneyDepositedEvent;
import com.devblo.account.event.MoneyWithdrawEvent;
import com.devblo.customer.event.CustomerAddressUpdatedEvent;
import com.devblo.customer.event.CustomerPersonalInfoUpdatedEvent;
import com.devblo.customer.event.CustomerRegisteredEvent;
import com.devblo.customer.event.CustomerStatusChangedEvent;
import com.devblo.infrastructure.events.audit.AuditEventPersistenceService;
import com.devblo.transaction.event.TransactionCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AuditEventListener {
    private final AuditEventPersistenceService auditEventPersistenceService;

    /**
     * TODO: FIX DRY AND KISS PROBLEMS
     *
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onAccountOpened(AccountOpenedEvent event) {
        auditEventPersistenceService.saveAuditEvent(event, "account", event.getAccountId().value());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onMoneyDeposited(MoneyDepositedEvent event) {
        auditEventPersistenceService.saveAuditEvent(event, "account", event.getAccountId().value());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onMoneyWithdrawn(MoneyWithdrawEvent event) {
        auditEventPersistenceService.saveAuditEvent(event, "account", event.getAccountId().value());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onCustomerRegistered(CustomerRegisteredEvent event) {
        auditEventPersistenceService.saveAuditEvent(event, "customer", event.getCustomerId().value());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onCustomerAddressUpdated(CustomerAddressUpdatedEvent event) {
        auditEventPersistenceService.saveAuditEvent(event, "customer", event.getCustomerId().value());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onCustomerPersonalInfoUpdated(CustomerPersonalInfoUpdatedEvent event) {
        auditEventPersistenceService.saveAuditEvent(event, "customer", event.getCustomerId().value());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onCustomerStatusChanged(CustomerStatusChangedEvent event) {
        auditEventPersistenceService.saveAuditEvent(event, "customer", event.getCustomerId().value());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onTransactionCreated(TransactionCreatedEvent event) {
        auditEventPersistenceService.saveAuditEvent(event, "transaction", event.getTransactionId().value());
    }
}
