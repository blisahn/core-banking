package com.devblo.transaction.repository;

import java.math.BigDecimal;

public record TransactionStatsSummary(
        BigDecimal totalDeposits,
        BigDecimal totalWithdrawals,
        BigDecimal totalTransfersIn,
        BigDecimal totalTransfersOut,
        long transactionCount
) {
    public BigDecimal netFlow() {
        return totalDeposits.add(totalTransfersIn).subtract(totalWithdrawals).subtract(totalTransfersOut);
    }
}
