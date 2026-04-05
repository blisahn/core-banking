"use client";

import { CreditCard, ArrowDownLeft, ArrowUpRight, ArrowRight } from 'lucide-react';
import Link from 'next/link';
import { Badge } from '@/components/ui';
import { formatMoney } from '@/lib/format';
import type { AccountSummary } from '@/types';

interface AccountCardProps {
  account: AccountSummary;
  onDeposit: () => void;
  onWithdraw: () => void;
  onTransfer: () => void;
}

export default function AccountCard({ account, onDeposit, onWithdraw, onTransfer }: AccountCardProps) {
  const isActive = account.status === 'ACTIVE';

  return (
    <div className="glass-card rounded-xl p-6 relative overflow-hidden group">
      <div className="flex justify-between items-start mb-6">
        <div className="flex items-center space-x-4">
          <div className="w-12 h-12 rounded-xl bg-slate-800 flex items-center justify-center border border-slate-700">
            <CreditCard className="w-6 h-6 text-primary-400" />
          </div>
          <div>
            <h3 className="text-lg font-bold text-slate-100">{account.type} Account</h3>
            <p className="text-sm text-slate-400 font-mono">{account.accountNumber}</p>
          </div>
        </div>
        <Badge status={account.status} />
      </div>

      <div className="mb-6">
        <p className="text-sm text-slate-400 mb-1">Available Balance</p>
        <p className="text-3xl font-bold text-slate-50">
          {formatMoney(account.balance.amount, account.balance.currency)}
        </p>
      </div>

      <div className="grid grid-cols-3 gap-3 pt-6 border-t border-slate-800/50">
        <ActionButton icon={<ArrowDownLeft className="w-5 h-5" />} label="Deposit" color="emerald" disabled={!isActive} onClick={onDeposit} />
        <ActionButton icon={<ArrowUpRight className="w-5 h-5" />} label="Withdraw" color="rose" disabled={!isActive} onClick={onWithdraw} />
        <ActionButton icon={<ArrowRight className="w-5 h-5" />} label="Transfer" color="primary" disabled={!isActive} onClick={onTransfer} />
      </div>

      <Link href={`/dashboard/accounts/${account.id.value}`} className="absolute inset-0 z-0" />
    </div>
  );
}

function ActionButton({ icon, label, color, disabled, onClick }: {
  icon: React.ReactNode; label: string; color: string; disabled: boolean; onClick: () => void;
}) {
  return (
    <button
      disabled={disabled}
      onClick={(e) => { e.preventDefault(); e.stopPropagation(); onClick(); }}
      className={`relative z-10 flex flex-col items-center justify-center space-y-1.5 p-2 rounded-lg hover:bg-slate-800 transition-colors disabled:opacity-50 disabled:cursor-not-allowed group/btn text-${color}-400`}
    >
      {icon}
      <span className="text-xs font-medium text-slate-300">{label}</span>
    </button>
  );
}
