"use client";

import type { AccountStatus, CustomerStatus, TransactionStatus } from '@/types';

type BadgeVariant = 'success' | 'warning' | 'danger' | 'info' | 'neutral';

const variantStyles: Record<BadgeVariant, string> = {
  success: 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20',
  warning: 'bg-amber-500/10 text-amber-400 border-amber-500/20',
  danger: 'bg-rose-500/10 text-rose-400 border-rose-500/20',
  info: 'bg-cyan-500/10 text-cyan-400 border-cyan-500/20',
  neutral: 'bg-slate-500/10 text-slate-400 border-slate-500/20',
};

function resolveVariant(status: string): BadgeVariant {
  switch (status) {
    case 'ACTIVE':
    case 'COMPLETED':
      return 'success';
    case 'PENDING':
    case 'SUSPENDED':
      return 'warning';
    case 'CLOSED':
    case 'FAILED':
      return 'danger';
    case 'FROZEN':
      return 'info';
    default:
      return 'neutral';
  }
}

interface BadgeProps {
  status: AccountStatus | CustomerStatus | TransactionStatus | string;
  className?: string;
}

export default function Badge({ status, className = '' }: BadgeProps) {
  const variant = resolveVariant(status);
  return (
    <span className={`text-xs font-semibold px-2.5 py-1 rounded-full border ${variantStyles[variant]} ${className}`}>
      {status}
    </span>
  );
}
