package com.devblo.infrastructure.events.audit;

import com.devblo.account.event.AccountOpenedEvent;
import com.devblo.account.event.MoneyDepositedEvent;
import com.devblo.account.event.MoneyWithdrawEvent;
import com.devblo.common.event.BaseDomainEvent;
import com.devblo.customer.CustomerStatus;
import com.devblo.customer.event.CustomerAddressUpdatedEvent;
import com.devblo.customer.event.CustomerPersonalInfoUpdatedEvent;
import com.devblo.customer.event.CustomerRegisteredEvent;
import com.devblo.customer.event.CustomerStatusChangedEvent;
import com.devblo.transaction.event.TransactionCreatedEvent;

import java.util.UUID;

public final class AuditEventDescriptor {

    private AuditEventDescriptor() {
    }

    public static String resolveSeverity(BaseDomainEvent event) {
        return switch (event) {
            case CustomerStatusChangedEvent e when e.getNewStatus() == CustomerStatus.CLOSED -> "CRITICAL";
            case CustomerStatusChangedEvent e when e.getNewStatus() == CustomerStatus.SUSPENDED -> "WARNING";
            case MoneyWithdrawEvent ignored -> "WARNING";
            default -> "INFO";
        };
    }

    public static String buildSummary(BaseDomainEvent event) {
        return switch (event) {
            case AccountOpenedEvent e -> "Yeni %s hesap açıldı (%s)".formatted(e.getAccountType(), e.getCurrency());
            case MoneyDepositedEvent e -> "%s %s yatırıldı".formatted(e.getAmount().amount(), e.getAmount().currency());
            case MoneyWithdrawEvent e -> "%s %s çekildi".formatted(e.getAmount().amount(), e.getAmount().currency());
            case CustomerRegisteredEvent e -> "Yeni müşteri kaydı: %s %s".formatted(
                    e.getPersonalInfo().firstName(), e.getPersonalInfo().lastName());
            case CustomerStatusChangedEvent e ->
                    "Müşteri durumu değişti: %s → %s".formatted(e.getOldStatus(), e.getNewStatus());
            case CustomerAddressUpdatedEvent ignored -> "Müşteri adres bilgisi güncellendi";
            case CustomerPersonalInfoUpdatedEvent ignored -> "Müşteri kişisel bilgileri güncellendi";
            case TransactionCreatedEvent e -> "Yeni işlem oluşturuldu: %s".formatted(e.getTransactionType());
            default -> event.getClass().getSimpleName().replace("Event", "")
                    .replaceAll("([a-z])([A-Z])", "$1 $2");
        };
    }

    public static String resolveAggregateType(BaseDomainEvent event) {
        return switch (event) {
            case AccountOpenedEvent ignored -> "account";
            case MoneyDepositedEvent ignored -> "account";
            case MoneyWithdrawEvent ignored -> "account";
            case CustomerRegisteredEvent ignored -> "customer";
            case CustomerStatusChangedEvent ignored -> "customer";
            case CustomerAddressUpdatedEvent ignored -> "customer";
            case CustomerPersonalInfoUpdatedEvent ignored -> "customer";
            case TransactionCreatedEvent ignored -> "transaction";
            default -> "unknown";
        };
    }

    public static UUID resolveAggregateId(BaseDomainEvent event) {
        return switch (event) {
            case AccountOpenedEvent e -> e.getAccountId().value();
            case MoneyDepositedEvent e -> e.getAccountId().value();
            case MoneyWithdrawEvent e -> e.getAccountId().value();
            case CustomerRegisteredEvent e -> e.getCustomerId().value();
            case CustomerStatusChangedEvent e -> e.getCustomerId().value();
            case CustomerAddressUpdatedEvent e -> e.getCustomerId().value();
            case CustomerPersonalInfoUpdatedEvent e -> e.getCustomerId().value();
            case TransactionCreatedEvent e -> e.getTransactionId().value();
            default -> null;
        };
    }
}
