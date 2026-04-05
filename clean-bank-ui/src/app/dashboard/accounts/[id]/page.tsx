"use client";

import { useEffect, useState, use } from 'react';
import { api } from '@/lib/api';
import type { AccountSummary, TransactionSummary, PagedResult } from '@/types';
import { ArrowLeft, Lock, Unlock, XCircle } from 'lucide-react';
import Link from 'next/link';
import { Spinner, Badge, Button } from '@/components/ui';
import { formatMoney } from '@/lib/format';
import TransactionTable from '@/components/transactions/TransactionTable';
import toast from 'react-hot-toast';

export default function AccountDetailPage({ params }: { params: Promise<{ id: string }> }) {
  const accountId = use(params).id;
  const [account, setAccount] = useState<AccountSummary | null>(null);
  const [transactions, setTransactions] = useState<TransactionSummary[]>([]);
  const [txPage, setTxPage] = useState(0);
  const [txTotalPages, setTxTotalPages] = useState(0);
  const [txTotalElements, setTxTotalElements] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');
  const [isActionLoading, setIsActionLoading] = useState(false);

  const fetchAccount = async () => {
    const data = await api.get<AccountSummary>(`/accounts/${accountId}/balance`);
    if (data) setAccount(data);
  };

  const fetchTransactions = async (page: number = 0) => {
    const data = await api.get<PagedResult<TransactionSummary>>(`/accounts/${accountId}/transactions?page=${page}&size=10`);
    if (data) {
      setTransactions(data.content);
      setTxPage(data.page);
      setTxTotalPages(data.totalPages);
      setTxTotalElements(data.totalElements);
    }
  };

  useEffect(() => {
    const load = async () => {
      try {
        await Promise.all([fetchAccount(), fetchTransactions(0)]);
      } catch {
        setError('Could not load account details');
      } finally {
        setIsLoading(false);
      }
    };
    load();
  }, [accountId]);

  const handleStatusChange = async (action: 'freeze' | 'activate' | 'close') => {
    setIsActionLoading(true);
    try {
      await api.patch(`/accounts/${accountId}/${action}`);
      toast.success(`Account ${action}d successfully`);
      await fetchAccount();
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : `Failed to ${action} account`;
      toast.error(msg);
    } finally {
      setIsActionLoading(false);
    }
  };

  if (isLoading) {
    return <div className="flex h-64 items-center justify-center"><Spinner /></div>;
  }

  if (error || !account) {
    return (
      <div className="glass-panel p-8 rounded-xl text-center">
        <p className="text-rose-400 mb-4">{error || 'Account not found'}</p>
        <Link href="/dashboard/accounts" className="text-primary-400 hover:text-primary-300">Back to Accounts</Link>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <Link href="/dashboard/accounts" className="inline-flex items-center text-sm font-medium text-slate-400 hover:text-slate-200 transition-colors">
        <ArrowLeft className="w-4 h-4 mr-1" /> Back to Accounts
      </Link>

      <div className="glass-card rounded-2xl p-8 relative overflow-hidden">
        <div className="absolute top-0 right-0 w-64 h-64 bg-primary-500/5 rounded-full blur-[80px] -mr-32 -mt-32" />

        <div className="flex flex-col md:flex-row md:justify-between md:items-start gap-6 relative z-10">
          <div>
            <div className="flex items-center space-x-3 mb-2">
              <Badge status={account.status} />
              <span className="text-sm text-slate-400 font-medium">Created: {new Date(account.createdAt).toLocaleDateString()}</span>
            </div>
            <h1 className="text-3xl font-bold text-slate-50">{account.type} Account</h1>
            <p className="text-slate-400 font-mono mt-1 text-lg">{account.accountNumber}</p>
          </div>

          <div className="text-left md:text-right">
            <p className="text-sm font-medium text-slate-400 mb-1">Available Balance</p>
            <p className="text-5xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-primary-400 to-emerald-400 inline-block">
              {formatMoney(account.balance.amount, account.balance.currency)}
            </p>
          </div>
        </div>

        <div className="flex flex-wrap gap-3 mt-8 pt-6 border-t border-slate-800/50">
          {account.status === 'ACTIVE' && (
            <Button variant="ghost" onClick={() => handleStatusChange('freeze')} disabled={isActionLoading}>
              <Lock className="w-4 h-4 mr-2" /> Freeze Account
            </Button>
          )}
          {account.status === 'FROZEN' && (
            <Button variant="success" onClick={() => handleStatusChange('activate')} disabled={isActionLoading}>
              <Unlock className="w-4 h-4 mr-2" /> Unfreeze Account
            </Button>
          )}
          {account.status !== 'CLOSED' && (
            <Button
              variant="danger"
              className="ml-auto"
              disabled={isActionLoading}
              onClick={() => {
                if (window.confirm('Are you sure you want to close this account? This cannot be undone.')) {
                  handleStatusChange('close');
                }
              }}
            >
              <XCircle className="w-4 h-4 mr-2" /> Close Account
            </Button>
          )}
        </div>
      </div>

      <TransactionTable
        transactions={transactions}
        currentAccountId={accountId}
        page={txPage}
        totalPages={txTotalPages}
        totalElements={txTotalElements}
        onPageChange={fetchTransactions}
      />
    </div>
  );
}
