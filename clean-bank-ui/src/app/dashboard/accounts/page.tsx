"use client";

import { useEffect, useState } from 'react';
import { api } from '@/lib/api';
import type { AccountSummary } from '@/types';
import { useAuth } from '@/contexts/AuthContext';
import { Wallet, Plus } from 'lucide-react';
import { Spinner, Button } from '@/components/ui';
import AccountCard from '@/components/accounts/AccountCard';
import AccountActionModal from '@/components/accounts/AccountActionModal';

type ModalState = { type: 'OPEN' | 'DEPOSIT' | 'WITHDRAW' | 'TRANSFER'; accountId?: string } | null;

export default function AccountsPage() {
  const { user } = useAuth();
  const [accounts, setAccounts] = useState<AccountSummary[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [modal, setModal] = useState<ModalState>(null);

  const fetchAccounts = async () => {
    try {
      const data = await api.get<AccountSummary[]>('/accounts');
      if (data) setAccounts(data);
    } catch (err) {
      console.error('Failed to fetch accounts:', err);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchAccounts();
  }, []);

  if (isLoading) {
    return <div className="flex h-64 items-center justify-center"><Spinner /></div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight text-slate-50">Accounts</h1>
          <p className="text-slate-400 mt-1">Manage your accounts and perform quick actions.</p>
        </div>
        <Button onClick={() => setModal({ type: 'OPEN' })} variant="primary">
          <Plus className="w-4 h-4 mr-2" />
          Open Account
        </Button>
      </div>

      {accounts.length === 0 ? (
        <div className="glass-panel p-12 rounded-xl flex flex-col items-center justify-center text-slate-500">
          <Wallet className="w-16 h-16 mb-4 opacity-20" />
          <h2 className="text-xl font-semibold mb-2 text-slate-300">No Accounts Found</h2>
          <p className="mb-6 text-center max-w-sm">You haven&apos;t opened any accounts yet. Get started by opening your first checking or savings account.</p>
          <Button variant="outline" onClick={() => setModal({ type: 'OPEN' })}>
            Open Account Now
          </Button>
        </div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {accounts.map(account => (
            <AccountCard
              key={account.id.value}
              account={account}
              onDeposit={() => setModal({ type: 'DEPOSIT', accountId: account.id.value })}
              onWithdraw={() => setModal({ type: 'WITHDRAW', accountId: account.id.value })}
              onTransfer={() => setModal({ type: 'TRANSFER', accountId: account.id.value })}
            />
          ))}
        </div>
      )}

      <AccountActionModal
        type={modal?.type || 'OPEN'}
        accountId={modal?.accountId}
        open={!!modal}
        onClose={() => setModal(null)}
        onSuccess={fetchAccounts}
      />
    </div>
  );
}
