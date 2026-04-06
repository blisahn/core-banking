"use client";

import { useEffect, useState } from 'react';
import { adminService } from '@/lib/services/admin';
import type { TransactionSummary, PagedResult } from '@/types';
import RoleGuard from '@/components/auth/RoleGuard';
import AdminTransactionTable from '@/components/transactions/AdminTransactionTable';
import { Spinner } from '@/components/ui';

export default function AdminTransactionsPage() {
  const [data, setData] = useState<PagedResult<TransactionSummary> | null>(null);
  const [page, setPage] = useState(0);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchTransactions = async () => {
      setIsLoading(true);
      try {
        const result = await adminService.getAllTransactions(page, 20);
        setData(result);
      } catch (error) {
        console.error('Failed to fetch transactions:', error);
      } finally {
        setIsLoading(false);
      }
    };
    fetchTransactions();
  }, [page]);

  return (
    <RoleGuard allowedRoles={["ADMIN"]}>
      <div className="space-y-6">
        <div>
          <h1 className="text-3xl font-bold tracking-tight text-slate-50">
            System <span className="text-gradient">Transactions</span>
          </h1>
          <p className="text-slate-400 mt-1">View all transactions across the system.</p>
        </div>

        {isLoading ? (
          <div className="flex h-64 items-center justify-center"><Spinner /></div>
        ) : data ? (
          <AdminTransactionTable
            transactions={data.content}
            page={data.page}
            totalPages={data.totalPages}
            totalElements={data.totalElements}
            onPageChange={setPage}
          />
        ) : (
          <p className="text-slate-500">Failed to load transactions.</p>
        )}
      </div>
    </RoleGuard>
  );
}
