"use client";

import { useAuth } from '@/contexts/AuthContext';
import RoleGuard from '@/components/auth/RoleGuard';
import CustomerList from '@/components/admin/CustomerList';
import CustomerPanel from '@/components/employee/CustomerPanel';

export default function CustomersPage() {
  const { isAdmin } = useAuth();

  return (
    <RoleGuard allowedRoles={["ADMIN", "EMPLOYEE"]}>
      {isAdmin ? <CustomerList /> : <CustomerPanel />}
    </RoleGuard>
  );
}
