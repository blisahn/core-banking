"use client";

import { useEffect, useState } from 'react';
import { api } from '@/lib/api';
import type { AccountSummary, CustomerSummary } from '@/types';
import { useAuth } from '@/contexts/AuthContext';
import { Wallet, CreditCard, Activity, ArrowRight, ArrowDownLeft, ArrowUpRight } from 'lucide-react';
import Link from 'next/link';
import { Spinner, Badge } from '@/components/ui';
import StatsCard from '@/components/dashboard/StatsCard';
import { formatMoney } from '@/lib/format';

export default function CustomerDashboard() {
  const { user } = useAuth();
  const [customer, setCustomer] = useState<CustomerSummary | null>(null);
  const [accounts, setAccounts] = useState<AccountSummary[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    if (!user) return;
    const fetchData = async () => {
      try {
        const [customerData, accountsData] = await Promise.all([
          api.get<CustomerSummary>(`/customers/${user.customerId}`),
          api.get<AccountSummary[]>('/accounts'),
        ]);
        if (customerData) setCustomer(customerData);
        if (accountsData) setAccounts(accountsData);
      } catch (error) {
        console.error('Failed to fetch dashboard data:', error);
      } finally {
        setIsLoading(false);
      }
    };
    fetchData();
  }, [user]);

  if (isLoading) {
    return <div className="flex h-64 items-center justify-center"><Spinner /></div>;
  }

  const totalBalance = accounts.reduce((sum, a) => sum + a.balance.amount, 0);
  const mainCurrency = accounts[0]?.balance.currency || 'TRY';

  return (
    <div className="space-y-6">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight text-slate-50">
            Welcome back, <span className="text-gradient">{customer?.firstName || 'User'}</span>!
          </h1>
          <p className="text-slate-400 mt-1">Here&apos;s what&apos;s happening with your money today.</p>
        </div>
        <Link href="/dashboard/accounts" className="flex items-center space-x-2 bg-slate-800 hover:bg-slate-700 text-slate-200 px-4 py-2 rounded-lg transition-colors border border-slate-700 hover:border-slate-600">
          <span>View All Accounts</span>
        </Link>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <StatsCard title="Total Balance" icon={<Wallet className="w-5 h-5" />} color="primary">
          <p className="text-3xl font-bold text-slate-50">{formatMoney(totalBalance, mainCurrency)}</p>
        </StatsCard>
        <StatsCard title="Active Accounts" icon={<CreditCard className="w-5 h-5" />} color="emerald">
          <p className="text-3xl font-bold text-slate-50">{accounts.filter(a => a.status === 'ACTIVE').length}</p>
        </StatsCard>
        <StatsCard title="Account Status" icon={<Activity className="w-5 h-5" />} color="amber">
          <Badge status={customer?.status || 'UNKNOWN'} />
        </StatsCard>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Accounts list */}
        <div className="glass-panel p-6 rounded-xl flex flex-col h-[400px]">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-xl font-bold text-slate-100">Your Accounts</h2>
            <Link href="/dashboard/accounts" className="text-sm text-primary-400 hover:text-primary-300 flex items-center group">
              See all <ArrowRight className="w-4 h-4 ml-1 group-hover:translate-x-1 transition-transform" />
            </Link>
          </div>
          <div className="flex-1 overflow-y-auto space-y-3 pr-2">
            {accounts.length === 0 ? (
              <div className="h-full flex flex-col items-center justify-center text-slate-500">
                <Wallet className="w-12 h-12 mb-3 opacity-20" />
                <p>No accounts found.</p>
                <Link href="/dashboard/accounts" className="mt-4 text-primary-500 hover:text-primary-400 text-sm font-medium">Open an account</Link>
              </div>
            ) : (
              accounts.map((account) => (
                <Link key={account.id.value} href={`/dashboard/accounts/${account.id.value}`} className="block">
                  <div className="glass-card p-4 rounded-lg flex items-center justify-between group active:scale-[0.98] transition-transform">
                    <div className="flex items-center space-x-4">
                      <div className="w-10 h-10 rounded-full bg-slate-800 flex items-center justify-center border border-slate-700 group-hover:border-primary-500/50 transition-all">
                        <CreditCard className="w-5 h-5 text-slate-400 group-hover:text-primary-400" />
                      </div>
                      <div>
                        <h4 className="font-medium text-slate-200 group-hover:text-primary-300 transition-colors">{account.type}</h4>
                        <p className="text-sm text-slate-500 font-mono mt-0.5">{account.accountNumber}</p>
                      </div>
                    </div>
                    <div className="text-right">
                      <p className="font-semibold text-slate-100">{formatMoney(account.balance.amount, account.balance.currency)}</p>
                      <Badge status={account.status} className="mt-1" />
                    </div>
                  </div>
                </Link>
              ))
            )}
          </div>
        </div>

        {/* Quick actions */}
        <div className="glass-panel p-6 rounded-xl flex flex-col h-[400px]">
          <h2 className="text-xl font-bold text-slate-100 mb-6">Quick Actions</h2>
          <div className="grid grid-cols-2 gap-4">
            <QuickAction href="/dashboard/accounts" icon={<ArrowDownLeft className="w-6 h-6" />} color="emerald" title="Deposit" subtitle="Add funds" />
            <QuickAction href="/dashboard/accounts" icon={<ArrowUpRight className="w-6 h-6" />} color="rose" title="Withdraw" subtitle="Get cash" />
            <Link href="/dashboard/accounts" className="glass-card hover:border-primary-500/50 hover:bg-primary-500/5 p-6 rounded-lg flex flex-col items-center justify-center text-center space-y-3 col-span-2 transition-all">
              <div className="w-12 h-12 bg-primary-500/20 rounded-full flex items-center justify-center text-primary-400">
                <ArrowRight className="w-6 h-6" />
              </div>
              <div>
                <h3 className="font-medium text-slate-200">Transfer Money</h3>
                <p className="text-xs text-slate-500 mt-1">Send to another account by IBAN</p>
              </div>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}

function QuickAction({ href, icon, color, title, subtitle }: { href: string; icon: React.ReactNode; color: string; title: string; subtitle: string }) {
  return (
    <Link href={href} className={`glass-card hover:border-${color}-500/50 hover:bg-${color}-500/5 p-6 rounded-lg flex flex-col items-center justify-center text-center space-y-3 transition-all`}>
      <div className={`w-12 h-12 bg-${color}-500/20 rounded-full flex items-center justify-center text-${color}-400`}>{icon}</div>
      <div>
        <h3 className="font-medium text-slate-200">{title}</h3>
        <p className="text-xs text-slate-500 mt-1">{subtitle}</p>
      </div>
    </Link>
  );
}
