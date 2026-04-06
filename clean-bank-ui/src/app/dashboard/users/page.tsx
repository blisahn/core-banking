"use client";

import { useEffect, useState, useCallback } from 'react';
import { adminService } from '@/lib/services/admin';
import type { UserSummary } from '@/types';
import RoleGuard from '@/components/auth/RoleGuard';
import CreateStaffUserModal from '@/components/admin/CreateStaffUserModal';
import { Spinner, Badge, Button } from '@/components/ui';
import { UserPlus } from 'lucide-react';

export default function UsersPage() {
  const [users, setUsers] = useState<UserSummary[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);

  const fetchUsers = useCallback(async () => {
    try {
      const data = await adminService.getAllUsers();
      setUsers(data || []);
    } catch (error) {
      console.error('Failed to fetch users:', error);
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  return (
    <RoleGuard allowedRoles={["ADMIN"]}>
      <div className="space-y-6">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <h1 className="text-3xl font-bold tracking-tight text-slate-50">
              User <span className="text-gradient">Management</span>
            </h1>
            <p className="text-slate-400 mt-1">Manage system users and staff accounts.</p>
          </div>
          <Button onClick={() => setShowModal(true)} icon={<UserPlus className="w-4 h-4" />}>
            Create Staff User
          </Button>
        </div>

        {isLoading ? (
          <div className="flex h-64 items-center justify-center"><Spinner /></div>
        ) : (
          <div className="glass-panel rounded-2xl overflow-hidden">
            <div className="overflow-x-auto">
              <table className="w-full text-left">
                <thead>
                  <tr className="bg-slate-900/50 text-slate-400 text-sm">
                    <th className="font-medium p-4 pl-6">Email</th>
                    <th className="font-medium p-4">Role</th>
                    <th className="font-medium p-4 pr-6">Customer ID</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-800/50">
                  {users.length === 0 ? (
                    <tr>
                      <td colSpan={3} className="p-8 text-center text-slate-500">No users found.</td>
                    </tr>
                  ) : (
                    users.map((u) => (
                      <tr key={u.id.value} className="hover:bg-slate-800/30 transition-colors">
                        <td className="p-4 pl-6 text-slate-200">{u.email}</td>
                        <td className="p-4"><Badge status={u.role} /></td>
                        <td className="p-4 pr-6 text-sm text-slate-400 font-mono">
                          {u.customerId?.value ? u.customerId.value.slice(0, 8) + '...' : '-'}
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          </div>
        )}

        <CreateStaffUserModal
          isOpen={showModal}
          onClose={() => setShowModal(false)}
          onCreated={fetchUsers}
        />
      </div>
    </RoleGuard>
  );
}
