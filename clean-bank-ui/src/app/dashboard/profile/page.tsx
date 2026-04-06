"use client";

import { useEffect, useState } from 'react';
import { api } from '@/lib/api';
import type { CustomerSummary } from '@/types';
import { useAuth } from '@/contexts/AuthContext';
import { User, Mail, Calendar, MapPin, Building, Save } from 'lucide-react';
import { Spinner, Badge, Input, Button, Alert } from '@/components/ui';
import toast from 'react-hot-toast';

export default function ProfilePage() {
  const { user } = useAuth();
  const [customer, setCustomer] = useState<CustomerSummary | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [dateOfBirth, setDateOfBirth] = useState('');
  const [street, setStreet] = useState('');
  const [district, setDistrict] = useState('');
  const [isUpdating, setIsUpdating] = useState({ personal: false, address: false, status: false });

  const fetchCustomer = async () => {
    if (!user) return;
    try {
      const data = await api.get<CustomerSummary>(`/customers/${user.customerId}`);
      if (data) {
        setCustomer(data);
        setFirstName(data.firstName);
        setLastName(data.lastName);
        if (user.email) setEmail(user.email);
      }
    } catch {
      toast.error('Failed to load profile');
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchCustomer();
  }, [user]);

  const handleUpdatePersonal = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user) return;
    setIsUpdating(prev => ({ ...prev, personal: true }));
    try {
      await api.put(`/customers/${user.customerId}/personal-info`, { firstName, lastName, email, dateOfBirth });
      toast.success('Personal info updated');
      fetchCustomer();
    } catch (err: unknown) {
      toast.error(err instanceof Error ? err.message : 'Failed to update personal info');
    } finally {
      setIsUpdating(prev => ({ ...prev, personal: false }));
    }
  };

  const handleUpdateAddress = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user) return;
    setIsUpdating(prev => ({ ...prev, address: true }));
    try {
      await api.put(`/customers/${user.customerId}/address`, { street, district });
      toast.success('Address updated');
    } catch (err: unknown) {
      toast.error(err instanceof Error ? err.message : 'Failed to update address');
    } finally {
      setIsUpdating(prev => ({ ...prev, address: false }));
    }
  };

  const handleStatusChange = async (action: 'suspend' | 'activate' | 'close') => {
    if (!user) return;
    setIsUpdating(prev => ({ ...prev, status: true }));
    try {
      await api.patch(`/customers/${user.customerId}/${action}`);
      toast.success(`Profile ${action}d successfully`);
      fetchCustomer();
    } catch (err: unknown) {
      toast.error(err instanceof Error ? err.message : `Failed to ${action} profile`);
    } finally {
      setIsUpdating(prev => ({ ...prev, status: false }));
    }
  };

  if (isLoading) {
    return <div className="flex h-64 items-center justify-center"><Spinner /></div>;
  }

  return (
    <div className="space-y-6 max-w-4xl mx-auto">
      <div>
        <h1 className="text-3xl font-bold tracking-tight text-slate-50">Profile Settings</h1>
        <p className="text-slate-400 mt-1">Manage your personal information and account status.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {/* Status & Actions */}
        <div className="space-y-6">
          <div className="glass-panel p-6 rounded-xl">
            <div className="flex flex-col items-center text-center pb-6 border-b border-slate-800/50">
              <div className="w-24 h-24 rounded-full bg-slate-800 border border-slate-700 flex items-center justify-center mb-4">
                <User className="w-10 h-10 text-slate-400" />
              </div>
              <h2 className="text-xl font-bold text-slate-100">{customer?.firstName} {customer?.lastName}</h2>
              <p className="text-sm text-slate-400 font-mono mt-1 break-all">{customer?.id.value}</p>
              <div className="mt-4">
                <Badge status={customer?.status || 'UNKNOWN'} />
              </div>
            </div>

            <div className="pt-6 space-y-3">
              <h3 className="text-sm font-medium text-slate-400 mb-3 uppercase tracking-wider">Account Actions</h3>

              {customer?.status === 'ACTIVE' && (
                <Button
                  variant="ghost"
                  fullWidth
                  disabled={isUpdating.status}
                  onClick={() => {
                    if (window.confirm('Are you sure you want to suspend your profile?')) {
                      handleStatusChange('suspend');
                    }
                  }}
                >
                  Suspend Profile
                </Button>
              )}

              {customer?.status === 'SUSPENDED' && (
                <Button variant="success" fullWidth disabled={isUpdating.status} onClick={() => handleStatusChange('activate')}>
                  Activate Profile
                </Button>
              )}

              {customer?.status !== 'CLOSED' && (
                <Button
                  variant="danger"
                  fullWidth
                  disabled={isUpdating.status}
                  onClick={() => {
                    if (window.confirm('DANGER: Are you sure you want to permanently close your profile?')) {
                      handleStatusChange('close');
                    }
                  }}
                >
                  Close Profile Permanently
                </Button>
              )}
            </div>
          </div>
        </div>

        {/* Forms */}
        <div className="md:col-span-2 space-y-6">
          <div className="glass-panel p-6 rounded-xl">
            <h2 className="text-xl font-bold text-slate-100 mb-6 flex items-center">
              <User className="w-5 h-5 mr-2 text-primary-400" /> Personal Information
            </h2>

            <form onSubmit={handleUpdatePersonal} className="space-y-5">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
                <Input id="firstName" label="First Name" required value={firstName} onChange={e => setFirstName(e.target.value)} />
                <Input id="lastName" label="Last Name" required value={lastName} onChange={e => setLastName(e.target.value)} />
              </div>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
                <Input id="email" label="Email" type="email" required value={email} onChange={e => setEmail(e.target.value)} icon={<Mail className="w-4 h-4" />} />
                <Input id="dateOfBirth" label="Date of Birth" type="date" required value={dateOfBirth} onChange={e => setDateOfBirth(e.target.value)} icon={<Calendar className="w-4 h-4" />} />
              </div>
              <div className="flex justify-end pt-2">
                <Button type="submit" loading={isUpdating.personal} loadingText="Saving...">
                  <Save className="w-4 h-4 mr-2" /> Save Changes
                </Button>
              </div>
            </form>
          </div>

          <div className="glass-panel p-6 rounded-xl">
            <h2 className="text-xl font-bold text-slate-100 mb-6 flex items-center">
              <MapPin className="w-5 h-5 mr-2 text-emerald-400" /> Address Details
            </h2>

            <form onSubmit={handleUpdateAddress} className="space-y-5">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
                <Input id="street" label="Street" required value={street} onChange={e => setStreet(e.target.value)} placeholder="123 Main St" icon={<Building className="w-4 h-4" />} />
                <Input id="district" label="District" required value={district} onChange={e => setDistrict(e.target.value)} placeholder="Downtown" />
              </div>
              <div className="flex justify-end pt-2">
                <Button type="submit" variant="success" loading={isUpdating.address} loadingText="Saving...">
                  <Save className="w-4 h-4 mr-2" /> Update Address
                </Button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
