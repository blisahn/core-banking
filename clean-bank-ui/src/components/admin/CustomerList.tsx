"use client";

import { useEffect, useState, useCallback } from 'react';
import { adminService } from '@/lib/services/admin';
import { employeeService } from '@/lib/services/employee';
import type { CustomerSummary } from '@/types';
import { Spinner, Badge, Button } from '@/components/ui';
import { UserPlus } from 'lucide-react';
import RegisterCustomerModal from '@/components/employee/RegisterCustomerModal';
import toast from 'react-hot-toast';

export default function CustomerList() {
  const [customers, setCustomers] = useState<CustomerSummary[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [showRegister, setShowRegister] = useState(false);
  const [actionLoading, setActionLoading] = useState<string | null>(null);

  const fetchCustomers = useCallback(async () => {
    try {
      const data = await adminService.getAllCustomers();
      setCustomers(data || []);
    } catch (error) {
      console.error('Failed to fetch customers:', error);
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchCustomers();
  }, [fetchCustomers]);

  const handleAction = async (id: string, action: 'suspend' | 'activate' | 'close') => {
    setActionLoading(`${id}-${action}`);
    try {
      if (action === 'suspend') await employeeService.suspendCustomer(id);
      else if (action === 'activate') await employeeService.activateCustomer(id);
      else await employeeService.closeCustomer(id);
      toast.success(`Customer ${action}d successfully`);
      fetchCustomers();
    } catch (error: any) {
      toast.error(error.message || `Failed to ${action} customer`);
    } finally {
      setActionLoading(null);
    }
  };

  if (isLoading) {
    return <div className="flex h-64 items-center justify-center"><Spinner /></div>;
  }

  return (
    <>
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-6">
        <div>
          <h1 className="text-3xl font-bold tracking-tight text-slate-50">
            Customer <span className="text-gradient">Management</span>
          </h1>
          <p className="text-slate-400 mt-1">View and manage all customers.</p>
        </div>
        <Button onClick={() => setShowRegister(true)} icon={<UserPlus className="w-4 h-4" />}>
          Register Customer
        </Button>
      </div>

      <div className="glass-panel rounded-2xl overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr className="bg-slate-900/50 text-slate-400 text-sm">
                <th className="font-medium p-4 pl-6">Name</th>
                <th className="font-medium p-4">Customer ID</th>
                <th className="font-medium p-4">Status</th>
                <th className="font-medium p-4 pr-6">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/50">
              {customers.length === 0 ? (
                <tr>
                  <td colSpan={4} className="p-8 text-center text-slate-500">No customers found.</td>
                </tr>
              ) : (
                customers.map((c) => (
                  <tr key={c.id.value} className="hover:bg-slate-800/30 transition-colors">
                    <td className="p-4 pl-6 text-slate-200 font-medium">{c.firstName} {c.lastName}</td>
                    <td className="p-4 text-sm text-slate-400 font-mono">{c.id.value.slice(0, 8)}...</td>
                    <td className="p-4"><Badge status={c.status} /></td>
                    <td className="p-4 pr-6">
                      <div className="flex space-x-2">
                        {c.status === 'ACTIVE' && (
                          <Button
                            variant="ghost"
                            className="text-sm py-1.5 px-3"
                            onClick={() => handleAction(c.id.value, 'suspend')}
                            loading={actionLoading === `${c.id.value}-suspend`}
                          >
                            Suspend
                          </Button>
                        )}
                        {c.status === 'SUSPENDED' && (
                          <Button
                            variant="success"
                            className="text-sm py-1.5 px-3"
                            onClick={() => handleAction(c.id.value, 'activate')}
                            loading={actionLoading === `${c.id.value}-activate`}
                          >
                            Activate
                          </Button>
                        )}
                        {c.status !== 'CLOSED' && (
                          <Button
                            variant="danger"
                            className="text-sm py-1.5 px-3"
                            onClick={() => handleAction(c.id.value, 'close')}
                            loading={actionLoading === `${c.id.value}-close`}
                          >
                            Close
                          </Button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      <RegisterCustomerModal
        isOpen={showRegister}
        onClose={() => setShowRegister(false)}
        onCreated={fetchCustomers}
      />
    </>
  );
}
