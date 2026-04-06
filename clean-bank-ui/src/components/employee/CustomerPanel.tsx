"use client";

import { useState } from 'react';
import { employeeService } from '@/lib/services/employee';
import { Button, Input } from '@/components/ui';
import { UserPlus } from 'lucide-react';
import RegisterCustomerModal from '@/components/employee/RegisterCustomerModal';
import toast from 'react-hot-toast';

export default function CustomerPanel() {
  const [showRegister, setShowRegister] = useState(false);
  const [customerId, setCustomerId] = useState('');
  const [accountId, setAccountId] = useState('');
  const [customerActionLoading, setCustomerActionLoading] = useState<string | null>(null);
  const [accountActionLoading, setAccountActionLoading] = useState<string | null>(null);

  const handleCustomerAction = async (action: 'suspend' | 'activate' | 'close') => {
    if (!customerId.trim()) { toast.error('Please enter a Customer ID'); return; }
    setCustomerActionLoading(action);
    try {
      if (action === 'suspend') await employeeService.suspendCustomer(customerId.trim());
      else if (action === 'activate') await employeeService.activateCustomer(customerId.trim());
      else await employeeService.closeCustomer(customerId.trim());
      toast.success(`Customer ${action}d successfully`);
    } catch (error: any) {
      toast.error(error.message || `Failed to ${action} customer`);
    } finally {
      setCustomerActionLoading(null);
    }
  };

  const handleAccountAction = async (action: 'freeze' | 'activate' | 'close') => {
    if (!accountId.trim()) { toast.error('Please enter an Account ID'); return; }
    setAccountActionLoading(action);
    try {
      if (action === 'freeze') await employeeService.freezeAccount(accountId.trim());
      else if (action === 'activate') await employeeService.activateAccount(accountId.trim());
      else await employeeService.closeAccount(accountId.trim());
      toast.success(`Account ${action}d successfully`);
    } catch (error: any) {
      toast.error(error.message || `Failed to ${action} account`);
    } finally {
      setAccountActionLoading(null);
    }
  };

  return (
    <>
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-6">
        <div>
          <h1 className="text-3xl font-bold tracking-tight text-slate-50">
            Customer <span className="text-gradient">Management</span>
          </h1>
          <p className="text-slate-400 mt-1">Register customers and manage accounts.</p>
        </div>
        <Button onClick={() => setShowRegister(true)} icon={<UserPlus className="w-4 h-4" />}>
          Register Customer
        </Button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Customer Management */}
        <div className="glass-panel p-6 rounded-xl space-y-4">
          <h2 className="text-lg font-bold text-slate-100">Manage Customer</h2>
          <p className="text-sm text-slate-400">Enter a customer ID to perform actions.</p>
          <Input
            label="Customer ID"
            value={customerId}
            onChange={(e) => setCustomerId(e.target.value)}
            placeholder="Enter customer UUID"
          />
          <div className="flex flex-wrap gap-2">
            <Button
              variant="ghost"
              className="text-sm py-1.5 px-3"
              onClick={() => handleCustomerAction('suspend')}
              loading={customerActionLoading === 'suspend'}
            >
              Suspend
            </Button>
            <Button
              variant="success"
              className="text-sm py-1.5 px-3"
              onClick={() => handleCustomerAction('activate')}
              loading={customerActionLoading === 'activate'}
            >
              Activate
            </Button>
            <Button
              variant="danger"
              className="text-sm py-1.5 px-3"
              onClick={() => handleCustomerAction('close')}
              loading={customerActionLoading === 'close'}
            >
              Close
            </Button>
          </div>
        </div>

        {/* Account Management */}
        <div className="glass-panel p-6 rounded-xl space-y-4">
          <h2 className="text-lg font-bold text-slate-100">Manage Account</h2>
          <p className="text-sm text-slate-400">Enter an account ID to perform actions.</p>
          <Input
            label="Account ID"
            value={accountId}
            onChange={(e) => setAccountId(e.target.value)}
            placeholder="Enter account UUID"
          />
          <div className="flex flex-wrap gap-2">
            <Button
              variant="ghost"
              className="text-sm py-1.5 px-3"
              onClick={() => handleAccountAction('freeze')}
              loading={accountActionLoading === 'freeze'}
            >
              Freeze
            </Button>
            <Button
              variant="success"
              className="text-sm py-1.5 px-3"
              onClick={() => handleAccountAction('activate')}
              loading={accountActionLoading === 'activate'}
            >
              Activate
            </Button>
            <Button
              variant="danger"
              className="text-sm py-1.5 px-3"
              onClick={() => handleAccountAction('close')}
              loading={accountActionLoading === 'close'}
            >
              Close
            </Button>
          </div>
        </div>
      </div>

      <RegisterCustomerModal
        isOpen={showRegister}
        onClose={() => setShowRegister(false)}
      />
    </>
  );
}
