"use client";

import { useEffect, useState } from 'react';
import { adminService } from '@/lib/services/admin';
import { Users, UserCheck, ArrowLeftRight, ArrowRight } from 'lucide-react';
import Link from 'next/link';
import { Spinner } from '@/components/ui';
import StatsCard from '@/components/dashboard/StatsCard';

export default function AdminDashboard() {
  const [userCount, setUserCount] = useState(0);
  const [customerCount, setCustomerCount] = useState(0);
  const [transactionCount, setTransactionCount] = useState(0);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [users, customers, transactions] = await Promise.all([
          adminService.getAllUsers(),
          adminService.getAllCustomers(),
          adminService.getAllTransactions(0, 1),
        ]);
        setUserCount(users?.length || 0);
        setCustomerCount(customers?.length || 0);
        setTransactionCount(transactions?.totalElements || 0);
      } catch (error) {
        console.error('Failed to fetch admin stats:', error);
      } finally {
        setIsLoading(false);
      }
    };
    fetchStats();
  }, []);

  if (isLoading) {
    return <div className="flex h-64 items-center justify-center"><Spinner /></div>;
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold tracking-tight text-slate-50">
          Admin <span className="text-gradient">Dashboard</span>
        </h1>
        <p className="text-slate-400 mt-1">System overview and management.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <StatsCard title="Total Users" icon={<Users className="w-5 h-5" />} color="primary">
          <p className="text-3xl font-bold text-slate-50">{userCount}</p>
        </StatsCard>
        <StatsCard title="Total Customers" icon={<UserCheck className="w-5 h-5" />} color="emerald">
          <p className="text-3xl font-bold text-slate-50">{customerCount}</p>
        </StatsCard>
        <StatsCard title="Total Transactions" icon={<ArrowLeftRight className="w-5 h-5" />} color="amber">
          <p className="text-3xl font-bold text-slate-50">{transactionCount}</p>
        </StatsCard>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <QuickLink href="/dashboard/transactions" title="View Transactions" description="Browse all system transactions" />
        <QuickLink href="/dashboard/users" title="Manage Users" description="Create and manage staff users" />
        <QuickLink href="/dashboard/customers" title="Manage Customers" description="View and manage customer accounts" />
      </div>
    </div>
  );
}

function QuickLink({ href, title, description }: { href: string; title: string; description: string }) {
  return (
    <Link href={href} className="glass-card p-6 rounded-xl hover:border-primary-500/50 hover:bg-primary-500/5 transition-all group">
      <div className="flex items-center justify-between mb-2">
        <h3 className="font-semibold text-slate-200 group-hover:text-primary-300 transition-colors">{title}</h3>
        <ArrowRight className="w-4 h-4 text-slate-500 group-hover:text-primary-400 group-hover:translate-x-1 transition-all" />
      </div>
      <p className="text-sm text-slate-500">{description}</p>
    </Link>
  );
}
