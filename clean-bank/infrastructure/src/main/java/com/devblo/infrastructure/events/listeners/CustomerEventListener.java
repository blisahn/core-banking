package com.devblo.infrastructure.events.listeners;

import com.devblo.account.Account;
import com.devblo.account.AccountStatus;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.customer.CustomerStatus;
import com.devblo.customer.event.CustomerAddressUpdatedEvent;
import com.devblo.customer.event.CustomerPersonalInfoUpdatedEvent;
import com.devblo.customer.event.CustomerRegisteredEvent;
import com.devblo.customer.event.CustomerStatusChangedEvent;
import com.devblo.infrastructure.events.outbox.OutboxEventService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerEventListener {

    private static final Logger log = LoggerFactory.getLogger(CustomerEventListener.class);
    private final OutboxEventService outboxEventService;
    private final IAccountWriteRepository accountWriteRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onCustomerRegistered(CustomerRegisteredEvent event) {
        log.info("Customer registered: id={}, personalInfo={}",
                event.getCustomerId(), event.getPersonalInfo());
        outboxEventService.saveEvent(event, "customer", event.getCustomerId().value());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onCustomerAddressUpdated(CustomerAddressUpdatedEvent event) {
        log.info("Customer addressUpdated: customerId={}, newAddress={}",
                event.getCustomerId(), event.getNewAddress() != null ? event.getNewAddress() : "");
        outboxEventService.saveEvent(event, "customer", event.getCustomerId().value());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onCustomerPersonalInfoUpdated(CustomerPersonalInfoUpdatedEvent event) {
        log.info("Customer personalInfoUpdated: id={}, oldInfo={}",
                event.getCustomerId(), event.getNewInfo() != null ? event.getNewInfo() : "");
        outboxEventService.saveEvent(event, "customer", event.getCustomerId().value());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onCustomerStatusChanged(CustomerStatusChangedEvent event) {
        log.info("Customer statusChanged: id={}, {} -> {}",
                event.getCustomerId(), event.getOldStatus(), event.getNewStatus());

        List<Account> accounts = accountWriteRepository.findByCustomerId(event.getCustomerId());

        if (event.getNewStatus() == CustomerStatus.SUSPENDED || event.getNewStatus() == CustomerStatus.CLOSED) {
            accounts.stream()
                    .filter(a -> a.getStatus() == AccountStatus.ACTIVE)
                    .forEach(a -> {
                        a.freeze();
                        accountWriteRepository.save(a);
                        log.info("Account frozen due to customer status change: accountId={}", a.getId());
                    });
        } else if (event.getNewStatus() == CustomerStatus.ACTIVE) {
            accounts.stream()
                    .filter(a -> a.getStatus() == AccountStatus.FROZEN)
                    .forEach(a -> {
                        a.activate();
                        accountWriteRepository.save(a);
                        log.info("Account reactivated due to customer reactivation: accountId={}", a.getId());
                    });
        }

        outboxEventService.saveEvent(event, "customer", event.getCustomerId().value());
    }
}
