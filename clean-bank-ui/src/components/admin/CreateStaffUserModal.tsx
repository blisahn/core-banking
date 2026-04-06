"use client";

import { useState } from 'react';
import { adminService } from '@/lib/services/admin';
import { Modal, Input, Select, Button } from '@/components/ui';
import toast from 'react-hot-toast';

interface CreateStaffUserModalProps {
  isOpen: boolean;
  onClose: () => void;
  onCreated: () => void;
}

export default function CreateStaffUserModal({ isOpen, onClose, onCreated }: CreateStaffUserModalProps) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('EMPLOYEE');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      await adminService.createStaffUser({ email, password, role });
      toast.success('Staff user created successfully');
      setEmail('');
      setPassword('');
      setRole('EMPLOYEE');
      onCreated();
      onClose();
    } catch (error: any) {
      toast.error(error.message || 'Failed to create user');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal open={isOpen} onClose={onClose} title="Create Staff User">
      <form onSubmit={handleSubmit} className="space-y-4">
        <Input
          label="Email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <Input
          label="Password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <Select
          label="Role"
          value={role}
          onChange={(e) => setRole(e.target.value)}
          options={[
            { label: 'Employee', value: 'EMPLOYEE' },
            { label: 'Admin', value: 'ADMIN' },
          ]}
        />
        <div className="flex justify-end space-x-3 pt-2">
          <Button variant="ghost" onClick={onClose} type="button">Cancel</Button>
          <Button type="submit" loading={isSubmitting}>Create User</Button>
        </div>
      </form>
    </Modal>
  );
}
