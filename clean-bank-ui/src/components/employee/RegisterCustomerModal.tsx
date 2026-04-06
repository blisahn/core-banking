"use client";

import { useState } from 'react';
import { employeeService } from '@/lib/services/employee';
import { Modal, Input, Button } from '@/components/ui';
import toast from 'react-hot-toast';

interface RegisterCustomerModalProps {
  isOpen: boolean;
  onClose: () => void;
  onCreated?: () => void;
}

export default function RegisterCustomerModal({ isOpen, onClose, onCreated }: RegisterCustomerModalProps) {
  const [form, setForm] = useState({
    firstName: '', lastName: '', email: '',
    dateOfBirth: '', street: '', district: '', password: '',
  });
  const [isSubmitting, setIsSubmitting] = useState(false);

  const update = (field: string) => (e: React.ChangeEvent<HTMLInputElement>) =>
    setForm(prev => ({ ...prev, [field]: e.target.value }));

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      await employeeService.registerCustomer(form);
      toast.success('Customer registered successfully');
      setForm({ firstName: '', lastName: '', email: '', dateOfBirth: '', street: '', district: '', password: '' });
      onCreated?.();
      onClose();
    } catch (error: any) {
      toast.error(error.message || 'Failed to register customer');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal open={isOpen} onClose={onClose} title="Register New Customer">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="grid grid-cols-2 gap-4">
          <Input label="First Name" value={form.firstName} onChange={update('firstName')} required />
          <Input label="Last Name" value={form.lastName} onChange={update('lastName')} required />
        </div>
        <Input label="Email" type="email" value={form.email} onChange={update('email')} required />
        <Input label="Date of Birth" type="date" value={form.dateOfBirth} onChange={update('dateOfBirth')} required />
        <div className="grid grid-cols-2 gap-4">
          <Input label="Street" value={form.street} onChange={update('street')} required />
          <Input label="District" value={form.district} onChange={update('district')} required />
        </div>
        <Input label="Password" type="password" value={form.password} onChange={update('password')} required />
        <div className="flex justify-end space-x-3 pt-2">
          <Button variant="ghost" onClick={onClose} type="button">Cancel</Button>
          <Button type="submit" loading={isSubmitting}>Register Customer</Button>
        </div>
      </form>
    </Modal>
  );
}
