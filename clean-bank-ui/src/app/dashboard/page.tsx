"use client";

import { useAuth } from '@/contexts/AuthContext';
import AdminDashboard from '@/components/dashboard/AdminDashboard';
import EmployeeDashboard from '@/components/dashboard/EmployeeDashboard';
import CustomerDashboard from '@/components/dashboard/CustomerDashboard';
import { Spinner } from '@/components/ui';

export default function DashboardOverview() {
  const { user, isLoading } = useAuth();

  if (isLoading) {
    return <div className="flex h-64 items-center justify-center"><Spinner /></div>;
  }

  if (user?.role === 'ADMIN') return <AdminDashboard />;
  if (user?.role === 'EMPLOYEE') return <EmployeeDashboard />;
  return <CustomerDashboard />;
}
