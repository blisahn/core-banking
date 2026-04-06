"use client";

import { ArrowDownLeft, ArrowUpRight, ArrowRight } from 'lucide-react';
import { Badge, Pagination } from '@/components/ui';
import { formatMoney, formatDateTime } from '@/lib/format';
import type { TransactionSummary } from '@/types';

interface AdminTransactionTableProps {
  transactions: TransactionSummary[];
  page?: number;
  totalPages?: number;
  totalElements?: number;
  onPageChange?: (page: number) => void;
}

function getIcon(type: string) {
  switch (type) {
    case 'DEPOSIT': return <ArrowDownLeft className="w-5 h-5" />;
    case 'WITHDRAWAL': return <ArrowUpRight className="w-5 h-5" />;
    default: return <ArrowRight className="w-5 h-5" />;
  }
}

function getColor(type: string) {
  switch (type) {
    case 'DEPOSIT': return 'bg-emerald-500/10 text-emerald-400';
    case 'WITHDRAWAL': return 'bg-rose-500/10 text-rose-400';
    default: return 'bg-primary-500/10 text-primary-400';
  }
}

export default function AdminTransactionTable({ transactions, page = 0, totalPages = 1, totalElements = 0, onPageChange }: AdminTransactionTableProps) {
  return (
    <div className="glass-panel rounded-2xl overflow-hidden">
      <div className="p-6 border-b border-slate-800/50 flex items-center justify-between">
        <h2 className="text-xl font-bold text-slate-100">All Transactions</h2>
        <span className="text-sm text-slate-500">{totalElements} total</span>
      </div>

      <div className="overflow-x-auto">
        <table className="w-full text-left">
          <thead>
            <tr className="bg-slate-900/50 text-slate-400 text-sm">
              <th className="font-medium p-4 pl-6">Type & Description</th>
              <th className="font-medium p-4">Amount</th>
              <th className="font-medium p-4">Source</th>
              <th className="font-medium p-4">Target</th>
              <th className="font-medium p-4">Date</th>
              <th className="font-medium p-4 pr-6">Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-800/50">
            {transactions.length === 0 ? (
              <tr>
                <td colSpan={6} className="p-8 text-center text-slate-500">
                  No transactions found.
                </td>
              </tr>
            ) : (
              transactions.map((tx) => (
                <tr key={tx.id?.value} className="hover:bg-slate-800/30 transition-colors">
                  <td className="p-4 pl-6">
                    <div className="flex items-center">
                      <div className={`w-10 h-10 rounded-full flex items-center justify-center mr-4 shrink-0 ${getColor(tx.type)}`}>
                        {getIcon(tx.type)}
                      </div>
                      <div>
                        <p className="font-medium text-slate-200 capitalize">{tx.type.toLowerCase()}</p>
                        <p className="text-xs text-slate-400">{tx.description}</p>
                      </div>
                    </div>
                  </td>
                  <td className="p-4">
                    <p className="font-semibold text-slate-200">{formatMoney(tx.amount, tx.currency)}</p>
                  </td>
                  <td className="p-4 text-sm text-slate-400 font-mono">
                    {tx.sourceAccountId?.value ? tx.sourceAccountId.value.slice(0, 8) + '...' : '-'}
                  </td>
                  <td className="p-4 text-sm text-slate-400 font-mono">
                    {tx.targetAccountId?.value ? tx.targetAccountId.value.slice(0, 8) + '...' : '-'}
                  </td>
                  <td className="p-4 text-sm text-slate-400">{formatDateTime(tx.timestamp)}</td>
                  <td className="p-4 pr-6"><Badge status={tx.status} /></td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {onPageChange && (
        <Pagination page={page} totalPages={totalPages} totalElements={totalElements} onPageChange={onPageChange} />
      )}
    </div>
  );
}
