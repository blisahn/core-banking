"use client";

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { api } from '@/lib/api';
import { Input, Button, Alert } from '@/components/ui';
import Link from 'next/link';

export default function RegisterForm() {
  const [formData, setFormData] = useState({ firstName: '', lastName: '', email: '', dateOfBirth: '', street: '', district: '', password: '' });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const router = useRouter();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(''); setSuccess('');
    setIsLoading(true);
    try {
      await api.post('/auth/register', formData);
      setSuccess('Registration successful! Redirecting to login...');
      setTimeout(() => router.push('/login'), 2000);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Registration failed.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="w-full max-w-xl p-8 glass-panel rounded-2xl">
      <div className="text-center mb-8">
        <h1 className="text-3xl font-bold tracking-tight mb-2 text-gradient">Create Account</h1>
        <p className="text-slate-400 text-sm">Join CleanBank for a premium experience</p>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
        {error && <Alert type="error" message={error} />}
        {success && <Alert type="success" message={success} />}

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <Input id="firstName" name="firstName" label="First Name" required value={formData.firstName} onChange={handleChange} placeholder="John" />
          <Input id="lastName" name="lastName" label="Last Name" required value={formData.lastName} onChange={handleChange} placeholder="Doe" />
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <Input id="email" name="email" label="Email" type="email" required value={formData.email} onChange={handleChange} placeholder="john@example.com" />
          <Input id="dateOfBirth" name="dateOfBirth" label="Date of Birth" type="date" required value={formData.dateOfBirth} onChange={handleChange} />
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <Input id="street" name="street" label="Street Address" required value={formData.street} onChange={handleChange} placeholder="123 Main St" />
          <Input id="district" name="district" label="District" required value={formData.district} onChange={handleChange} placeholder="Downtown" />
        </div>
        <Input id="password" name="password" label="Password" type="password" required value={formData.password} onChange={handleChange} placeholder="••••••••" />
        <Button type="submit" fullWidth loading={isLoading} loadingText="Creating Account..." disabled={!!success}>Register</Button>
        <p className="text-center text-slate-400 text-sm mt-6">
          Already have an account?{' '}
          <Link href="/login" className="text-emerald-400 hover:text-emerald-300 font-medium transition-colors">Sign in</Link>
        </p>
      </form>
    </div>
  );
}
