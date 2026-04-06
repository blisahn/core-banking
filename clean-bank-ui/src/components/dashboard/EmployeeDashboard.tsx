"use client";

import { UserCheck, UserPlus, ArrowRight } from 'lucide-react';
import Link from 'next/link';
import StatsCard from '@/components/dashboard/StatsCard';

export default function EmployeeDashboard() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold tracking-tight text-slate-50">
          Employee <span className="text-gradient">Dashboard</span>
        </h1>
        <p className="text-slate-400 mt-1">Customer and account management.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <StatsCard title="Customer Management" icon={<UserCheck className="w-5 h-5" />} color="primary">
          <p className="text-sm text-slate-400 mb-4">Manage customer and account states.</p>
          <Link href="/dashboard/customers" className="inline-flex items-center space-x-2 text-primary-400 hover:text-primary-300 text-sm font-medium group">
            <span>Go to Customers</span>
            <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
          </Link>
        </StatsCard>
        <StatsCard title="Register Customer" icon={<UserPlus className="w-5 h-5" />} color="emerald">
          <p className="text-sm text-slate-400 mb-4">Register a new walk-in customer.</p>
          <Link href="/dashboard/customers" className="inline-flex items-center space-x-2 text-emerald-400 hover:text-emerald-300 text-sm font-medium group">
            <span>Register Now</span>
            <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
          </Link>
        </StatsCard>
      </div>
    </div>
  );
}
